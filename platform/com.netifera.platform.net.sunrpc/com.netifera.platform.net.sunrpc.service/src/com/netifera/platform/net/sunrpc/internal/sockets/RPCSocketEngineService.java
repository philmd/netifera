package com.netifera.platform.net.sunrpc.internal.sockets;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.osgi.service.component.ComponentContext;

import com.netifera.platform.api.log.ILogManager;
import com.netifera.platform.api.log.ILogger;
import com.netifera.platform.net.sockets.AsynchronousSocketChannel;
import com.netifera.platform.net.sockets.CompletionHandler;
import com.netifera.platform.net.sockets.ISocketEngineService;
import com.netifera.platform.net.sockets.TCPChannel;
import com.netifera.platform.net.sockets.UDPChannel;
import com.netifera.platform.net.sockets.internal.SocketEngineService;
import com.netifera.platform.net.sunrpc.packets.RpcCall;
import com.netifera.platform.net.sunrpc.packets.RpcReply;
import com.netifera.platform.net.sunrpc.packets.XdrBuffer;
import com.netifera.platform.net.sunrpc.sockets.IRPCChannel;
import com.netifera.platform.net.sunrpc.sockets.IRPCSocketEngineService;
import com.netifera.platform.net.sunrpc.sockets.RPCException;
import com.netifera.platform.net.sunrpc.sockets.RpcSocketLocator;
import com.netifera.platform.util.addresses.inet.InternetAddress;
import com.netifera.platform.util.locators.InetSocketLocator;
import com.netifera.platform.util.locators.UDPSocketLocator;

// TODO
// - if not  yet selected/written, add data in same channelbuffer
// - deadline/timeouts

public class RPCSocketEngineService implements IRPCSocketEngineService {
	private static final boolean DEBUG = true;
	private static final long CACHE_FLUSH_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(5 * 60); // 5 min
	
	SocketEngineService socketEngine;
	private ILogger logger;
	private int lastXID;
	private Random randomGenerator = new Random(); // TODO only for DEBUG
	private Boolean engineState;
	
	private class CachedSocketChannelContext {
		protected final AsynchronousSocketChannel socketChannel;
		private int useCount = 1;
		long deadline = 0;
		CachedSocketChannelContext(AsynchronousSocketChannel socketChannel) {
			this.socketChannel = socketChannel;
		}
		void incrementUse() {
			useCount++;
		}
		void decrementUse() {
			if (useCount == 0) {
				throw new IllegalStateException("AsynchronousSocketChannel not used");
			}
			useCount--;
			if (useCount == 0) {
				deadline = System.currentTimeMillis() + CACHE_FLUSH_TIMEOUT_MS;
				synchronized (socketCache) {
					socketCache.notify();
				}
			}
		}
		boolean isUsed() {
			return useCount > 0;
		}
		@Override
		public String toString() {
			return socketChannel.toString() + " useCount:" + useCount;
		}
	}
	
	void addCachedSocket(InetSocketLocator locatorKey, AsynchronousSocketChannel socketChannel) {
		CachedSocketChannelContext ctx = new CachedSocketChannelContext(socketChannel);
		synchronized (socketCache) {
			socketCache.put(locatorKey, ctx);
			socketCache.notify();
		}
		setupCacheFlusher();
	}
	
	private final Map<InetSocketLocator, CachedSocketChannelContext> socketCache = new HashMap<InetSocketLocator, CachedSocketChannelContext>();
	private final Set<RPCChannel> openedChannels = new HashSet<RPCChannel>();
	private Thread cacheFlusherThread;
	
	private void setupCacheFlusher() {
		if (cacheFlusherThread != null) {
			return;
		}
		cacheFlusherThread = new Thread(new Runnable() {

			public void run() {
				while (!Thread.interrupted()) {
					if (socketCache.isEmpty()) {
						try {
							synchronized (socketCache) {
								socketCache.wait(CACHE_FLUSH_TIMEOUT_MS);
							}
						} catch (InterruptedException e) {
							// a socket got added to the cache queue,
							// TODO engine desactivated
						}
						if (socketCache.isEmpty()) {
							// cache not used, freeing resources
							cacheFlusherThread = null;
							return;
						}
					}
					long now = System.currentTimeMillis();
					synchronized (socketCache) {
						for (Entry<InetSocketLocator, CachedSocketChannelContext> entry : socketCache.entrySet()) {
							CachedSocketChannelContext ctx = entry.getValue();
							if (ctx.deadline > 0 && ctx.deadline < now) {
								// expired
								try {
									ctx.socketChannel.close();
								} catch (IOException e) {
									logger.error("closing cached socket", e);
								}
								socketCache.remove(entry.getKey());
							}
						}
					}
				}				
			}
			
		}, "RPC Socket Cache Manager");
		cacheFlusherThread.setDaemon(true);
		cacheFlusherThread.start();
	}
	
	public int nextXID() {
		if (DEBUG) {
			return ++lastXID;
		}
		return randomGenerator.nextInt();
	}
	
	public RPCChannel openRPC() throws IOException {
		return openRPC(IRPCChannel.FLAG_CACHED);
	}
	
	public RPCChannel openRPC(int flags) throws IOException {
		RPCChannel channel = new RPCChannel(this, flags);
		openedChannels.add(channel);
		return channel;
	}

	void close(RPCChannel channel) throws IOException {
		if (channel.isCached()) {
			socketCache.get(channel.locator).decrementUse();
		} else {
			channel.socketChannel.close();
		}
		openedChannels.remove(channel);
	}
	
	// FIXME CALLIT + socketCache hash ?
	boolean connect(final RPCChannel channel, RpcSocketLocator remote, long timeout, TimeUnit unit) throws IOException, InterruptedException {
		InetSocketLocator locatorKey = remote.getInetSocketLocator();
		if (channel.isCached()) {
			synchronized (socketCache) {
				/* try to get a cached socket */
				if (socketCache.containsKey(locatorKey)) {
					CachedSocketChannelContext ctx = socketCache.get(locatorKey);
					channel.socketChannel = ctx.socketChannel;
					ctx.incrementUse();
					return true;
				}
			}
		}
		boolean success = false;
		if (remote.isUDP()) {
			channel.socketChannel = socketEngine.openUDP();
			if (channel.isPrivileged()) {
				boolean bound = false;
				for (int port = 1023; port > 0; port--) {
					UDPSocketLocator localLocator = new UDPSocketLocator(InternetAddress.fromString("0.0.0.0"), 1023); // XXX ip
					try {
						((UDPChannel)channel.socketChannel).bind(localLocator);
					} catch (SocketException e) {
						continue;
					}
					bound = true;
					break;
				}
				if (!bound) {
					logger.error("can not bind privileged socket");
					return false;
				}
			}
			success = ((UDPChannel)channel.socketChannel).connect(remote.getUdp());
		} else if (remote.isTCP()) {
			channel.socketChannel = socketEngine.openTCP();
			// TODO bind
			Future<Boolean> retval = ((TCPChannel)channel.socketChannel).connect(remote.getTcp(), timeout, unit, null, new CompletionHandler<Boolean, Void>() {

				public void cancelled(Void attachment) {
					logger.warning("connect cancelled");
				}
				public void completed(Boolean result, Void attachment) {
					
				}
				public void failed(Throwable exc, Void attachment) {
					logger.error("connecting " + channel, exc);
				}
			});
			try {
				success = retval.get().booleanValue();
			} catch (ExecutionException e) {
				logger.error("error connecting (FUTURE)", e);
				//return false;
			}
		} else {
			throw new IllegalArgumentException("unknown transport protocol " + remote.getProtocol());
		}
		if (success) {
			addCachedSocket(locatorKey, channel.socketChannel);
		}
		return success;
	}
	
	private Map<Integer, RpcReply> repliesMap = new ConcurrentHashMap<Integer, RpcReply>();	
	
	// TODO portmap redirect
	// TODO deadline
	<A> Future<RpcReply> asynchronousCall(final RPCChannel channel, final RpcCall request, final long timeout,
			final TimeUnit unit, final A attachment, final CompletionHandler<RpcReply, ? super A> rpcHandler) throws RPCException {

		//final long deadline = System.currentTimeMillis() + unit.toMillis(timeout);
		
		FutureTask<RpcReply> future = new FutureTask<RpcReply>(new Callable<RpcReply>() {

			public RpcReply call() throws Exception {
				Integer expectedXID = request.getXID();
				synchronized (repliesMap) {
					while (engineState.booleanValue()) {
						repliesMap.wait();
						// TODO if expired return null;
						if (repliesMap.containsKey(expectedXID)) {
							RpcReply reply = repliesMap.remove(expectedXID);
							rpcHandler.completed(reply, attachment);
							return reply;
						}
					}
					return null;
				}
			}}) {

		};
		
		socketEngine.getExecutor().execute(future);
		
		socketEngine.asynchronousWrite(channel.socketChannel, writeBuffer(channel, request), timeout, unit, attachment, new CompletionHandler<Integer, A>() {

			public void cancelled(A attachment) {
				logger.warning("call cancelled");
				rpcHandler.cancelled(attachment);
			}

			public void completed(Integer result, A attachment) {
				rpcRead(channel, timeout, unit, attachment, rpcHandler);
			}

			public void failed(Throwable exc, A attachment) {
				logger.error("calling " + channel, exc);
				rpcHandler.failed(exc, attachment);
			}
		});

		return future;
	}
	
	private <A> Future<Integer> rpcRead(final RPCChannel channel, final long timeout, final TimeUnit unit, final A attachment, final CompletionHandler<RpcReply, ? super A> rpcHandler) {
		final ByteBuffer tempBuffer = ByteBuffer.allocate(8192);
		final ByteBuffer readBuffer = channel.readBuffer;
		return socketEngine.asynchronousRead(channel.socketChannel, tempBuffer, timeout, unit, attachment, new CompletionHandler<Integer, A>() {

			public void cancelled(A attachment) {
				logger.warning("read cancelled");
				rpcHandler.cancelled(attachment);
			}

			public void completed(Integer result, A attachment) {
				
				tempBuffer.flip();
				if (channel.socketChannel instanceof TCPChannel) {
					boolean lastFrag = false;// = true;
					while (!lastFrag && tempBuffer.hasRemaining()) {
						tempBuffer.mark();
						int fragHdr = tempBuffer.getInt();
						int fragLength = fragHdr & 0x7fffffff;
						lastFrag = fragHdr >> 31 != 0;
						if (tempBuffer.remaining() < fragLength) {
							tempBuffer.reset();
							rpcRead(channel, timeout, unit, attachment, rpcHandler);
							return;
						}
						tempBuffer.get(readBuffer.array(), readBuffer.position(), fragLength);
						readBuffer.position(readBuffer.position() + fragLength);
					}
				} else {
					readBuffer.put(tempBuffer);
				}
				
				readBuffer.flip();
				RpcReply reply = new RpcReply();
				reply.unpack(readBuffer);
				readBuffer.clear();
				synchronized (repliesMap) {
					// TODO verify repliesMap size, cancel timeouted replies
					repliesMap.put(reply.getXID(), reply);
					repliesMap.notifyAll();
				}
			}

			public void failed(Throwable exc, A attachment) {
				logger.error("reading " + channel, exc);
				rpcHandler.failed(exc, attachment);
			}
		});
	}

	// TODO write fragmentation
	private ByteBuffer writeBuffer(RPCChannel channel, RpcCall request) {
		XdrBuffer pkt = request.pack();
		ByteBuffer writeBuffer = ByteBuffer.allocate(pkt.length() + 4);
		
		if (channel.socketChannel instanceof TCPChannel) {
			writeBuffer.putInt((1 << 31) + pkt.length());
		}
		
		writeBuffer.put(pkt.rawBuffer());
		writeBuffer.flip();
		
		return writeBuffer;
	}
	
	protected void setSocketEngine(ISocketEngineService socketEngine) {
		this.socketEngine = (SocketEngineService) socketEngine;
	}
	
	protected void unsetSocketEngine(ISocketEngineService socketEngine) {
		this.socketEngine = null;
	}
		
	protected void setLogManager(ILogManager logManager) {
		logger = logManager.getLogger("RPC Socket Engine");
	}
	
	protected void unsetLogManager(ILogManager logManager) {
		//logger = null;
	}

	protected void activate(ComponentContext context) {
		engineState = Boolean.TRUE;
		
		if (DEBUG) {
			lastXID = 0;
		}
	}
	
	protected void deactivate(ComponentContext context) {
		repliesMap.clear();
		
		// TODO cancel FutureTasks
		engineState = Boolean.FALSE;
		repliesMap.notifyAll();
		
		for (RPCChannel channel : openedChannels) {
			try {
				channel.close();
			} catch (IOException e) {
				logger.error("closing " + channel, e);
			}
		}
		synchronized (socketCache) {
			socketCache.clear();
			socketCache.notify();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(128);
		if (!openedChannels.isEmpty()) {
			sb.append("open channel");
			if (openedChannels.size() > 1) {
				sb.append('s');
			}
			sb.append(": ");
			sb.append(openedChannels.toString());
		} else {
			sb.append("no channel opened");
		}
		if (!socketCache.isEmpty()) {
			sb.append("\ncached socket");
			if (socketCache.size() > 1) {
				sb.append('s');
			}
			sb.append(": ");
			sb.append(socketCache.keySet().toString());
		}
		return sb.toString();
	}
}
