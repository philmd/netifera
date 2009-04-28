package com.netifera.platform.net.sunrpc.internal.sockets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.netifera.platform.net.sockets.AsynchronousSelectableChannel;
import com.netifera.platform.net.sockets.AsynchronousSocketChannel;
import com.netifera.platform.net.sockets.CompletionHandler;
import com.netifera.platform.net.sunrpc.packets.RpcCall;
import com.netifera.platform.net.sunrpc.packets.RpcReply;
import com.netifera.platform.net.sunrpc.sockets.IRPCChannel;
import com.netifera.platform.net.sunrpc.sockets.RPCException;
import com.netifera.platform.net.sunrpc.sockets.RpcSocketLocator;

public class RPCChannel extends AsynchronousSelectableChannel implements IRPCChannel {

	final RPCSocketEngineService engine;
	final int flags;
	AsynchronousSocketChannel socketChannel;
	RpcSocketLocator locator;
	final ByteBuffer readBuffer = ByteBuffer.allocate(8192);
	
	RPCChannel(RPCSocketEngineService engine, int flags) {
		super(engine.socketEngine);
		this.engine = engine;
		this.flags = flags;
	}

	public int getFlags() {
		return flags;
	}
	
	boolean isCached() {
		return (flags & FLAG_CACHED) != 0;
	}
		
	boolean isPrivileged() {
		return (flags & FLAG_PRIVILEGED) != 0;
	}
		
	public AsynchronousSocketChannel getWrappedSocketChannel() {
		return socketChannel;
	}
	
	//public <A> Future<Void> connect(TCPSocketLocator remote, long timeout, TimeUnit unit, A attachment, CompletionHandler<Void, ? super A> handler) throws IOException, InterruptedException {

	public boolean connect(RpcSocketLocator remote, long timeout, TimeUnit unit) throws IOException, InterruptedException {
		locator = remote;
		return engine.connect(this, remote, timeout, unit);
	}
	
	/*
	public RPCReply call(RPCCall pkt, long timeout, TimeUnit unit) throws IOException {
		return engine.call(this, pkt, timeout, unit);
	}
	*/
	
	public <A> Future<RpcReply> asynchronousCall(RpcCall pkt, long timeout, TimeUnit unit, A attachment, CompletionHandler<RpcReply, ? super A> handler) throws RPCException {
		return engine.asynchronousCall(this, pkt, timeout, unit, attachment, handler);
	}
	
	public void close() throws IOException {
		engine.close(this);
	}

	@Override
	public String toString() {
		return socketChannel == null ? "not connected" : socketChannel.toString();
	}
}
