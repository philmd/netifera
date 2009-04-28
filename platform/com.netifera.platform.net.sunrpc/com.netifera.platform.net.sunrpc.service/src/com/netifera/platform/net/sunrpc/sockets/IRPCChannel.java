package com.netifera.platform.net.sunrpc.sockets;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.netifera.platform.net.sockets.AsynchronousChannel;
import com.netifera.platform.net.sockets.CompletionHandler;
import com.netifera.platform.net.sunrpc.packets.RpcCall;
import com.netifera.platform.net.sunrpc.packets.RpcReply;

public interface IRPCChannel extends AsynchronousChannel {
	
	int FLAG_PRIVILEGED	= 0x01;
	int FLAG_CACHED		= 0x02;
	int FLAG_INDIRECT	= 0x04;

	int getFlags();
	
	boolean connect(RpcSocketLocator remote, long timeout, TimeUnit unit) throws IOException, InterruptedException;
	
	// FIXME indirectCall?
	<A> Future<RpcReply> asynchronousCall(RpcCall pkt, long timeout, TimeUnit unit, A attachment, CompletionHandler<RpcReply, ? super A> handler) throws RPCException, InterruptedException;
	
}
