package com.netifera.platform.net.sunrpc.tools;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.netifera.platform.api.probe.IProbe;
import com.netifera.platform.api.tools.IToolContext;
import com.netifera.platform.api.tools.ToolException;
import com.netifera.platform.net.sockets.CompletionHandler;
import com.netifera.platform.net.sunrpc.RpcServiceEntryTable;
import com.netifera.platform.net.sunrpc.internal.tools.Activator;
import com.netifera.platform.net.sunrpc.packets.RpcReply;
import com.netifera.platform.net.sunrpc.packets.portmap.PortmapCall;
import com.netifera.platform.net.sunrpc.sockets.IRPCChannel;
import com.netifera.platform.net.sunrpc.sockets.RPCException;
import com.netifera.platform.net.sunrpc.sockets.RpcSocketLocator;
import com.netifera.platform.tools.RequiredOptionMissingException;

public class RpcDiscovery extends AbstractRpcTool {
	
	int timeout;
	int maxProgramTested = 10000;
	int maxProgramFound = 0;
	private int maxVersion = 5;
	private Integer[] programList = RpcServiceEntryTable.registeredNumbers();;
	
	public void toolRun(IToolContext context) throws ToolException {
		this.context = context;

		// XXX hardcode local probe as realm
		IProbe probe = Activator.getInstance().getProbeManager().getLocalProbe();
		realm = probe.getEntity().getId();
		
		setupToolOptions();

		RpcSocketLocator locator = createLocator(port, PortmapCall.PORTMAP_PROGRAM, 1);
		if (locator == null) {
			context.error("Unknown protocol: " + protocol);
			return;
		}
	
		context.setTitle("Discovering RPC service at " + address + "/" + port + "/" + protocol);
		
		if (maxProgramTested > programList.length) {
			maxProgramTested = programList.length;
		}
		context.setTotalWork(maxProgramTested);
		
		discover(locator);
		
		context.done();
	}
	
	@Override
	protected void setupRPCToolOptions() throws RequiredOptionMissingException {
		port = ((Integer)context.getConfiguration().get(TARGET_PORT_KEY)).intValue();
		
		timeout = 30;
	}
	
	private class PingContext {
		protected int program;
		protected int versionMin = 0;
		protected int versionMax = maxVersion;
		protected boolean found;
	}
	
	private void discover(final RpcSocketLocator locator) {
		IRPCChannel channel;
		try {
			channel = Activator.getInstance().getRPCSocketEngine().openRPC();
			if (channel.connect(locator, timeout, TimeUnit.SECONDS)) {
				PingContext ctx = new PingContext();
				int programTriedCount = 0;
				int programFoundCount = 0;
				for (int program : programList) {
					if (programTriedCount >= maxProgramTested) {
						context.warning("limit reached, tried " + maxProgramTested + " programs");
						break;
					}
					ctx.program = program;
					ctx.found = false;
					ping(channel, ctx, 0);
					if (ctx.found) {
						String progName = RpcServiceEntryTable.getName(program);
						context.setStatus("Scanning versions for " + progName);
						for (int version = ctx.versionMin; version <= ctx.versionMax; version++) {
							if (!RpcServiceEntryTable.isValidVersion(program, version)) {
								// TODO avoid weird entries in /var/log/syslog
								continue;
							}
							ctx.found = false;
							ping(channel, ctx, version);
							if (ctx.found) {
								System.out.println("FOUND " + progName + " v" + version);
								Activator.getInstance().getRpcNetworkEntityFactory().createRpcService(realm, context.getSpaceId(), locator, ctx.program, version, null);
							}
						}
						if (maxProgramFound != 0 && ++programFoundCount >= maxProgramFound) {
							context.warning("limit reached, found " + maxProgramFound + " program" + (maxProgramFound > 1 ? 's' : ""));
							break;
						}
					}
					context.worked(1);
				}
				
				channel.close();
			} else {
				context.error("can not connect");
			}
		} catch (Exception e) {
			context.exception(e.getMessage(), e);
		}
	}
	
	private void ping(final IRPCChannel channel, final PingContext ctx, final int version) throws RPCException, InterruptedException, ExecutionException {
		channel.asynchronousCall(PortmapCall.Ping(ctx.program, version), timeout, TimeUnit.SECONDS, ctx, new CompletionHandler<RpcReply, PingContext>() {

			public void cancelled(PingContext attachment) {
				context.error("rpc error (cancelled)");
			}

			public void completed(RpcReply reply, PingContext attachment) {
				String progName = RpcServiceEntryTable.getName(attachment.program);
				if (reply.getReplyState() != 0) {
					context.error("service " + progName + " rejected the request");
					// TODO get reject_stat + auth_stat
				} else {
					switch (reply.getAcceptState()) {
					case PROG_UNAVAIL:
						//flooding... context.warning("service is not " + progName);
						break;
					case PROG_MISMATCH:
						attachment.versionMin = reply.xdrBuffer().xdr_int();
						attachment.versionMax = reply.xdrBuffer().xdr_int();
						attachment.found = true;
						if (version == 0) {
							// we ping'ed
							context.info("service " + progName + " responded");
						} else {
							context.warning("service " + progName + " does not support version " + version);
						}
						break;
					case SUCCESS:
						attachment.found = true;
						context.info("found service " + progName + " v" + version);
						break;
					case PROC_UNAVAIL:
						context.error("ping procedure unsupported for service " + progName);
						break;
					default:
						context.warning("unknown state [" + reply.getAcceptState() + "] trying service " + progName);
						// TODO
					}
				}
			}

			public void failed(Throwable exc, PingContext attachment) {
				context.error("rpc error " + exc.getMessage());
				// will retry
				//ping(channel, ctx, version);
			}
		}).get();
	}
}
