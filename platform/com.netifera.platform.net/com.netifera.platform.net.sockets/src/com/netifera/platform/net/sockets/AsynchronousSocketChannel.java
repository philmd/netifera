package com.netifera.platform.net.sockets;

import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.netifera.platform.net.sockets.internal.SocketEngineService;

public abstract class AsynchronousSocketChannel extends AsynchronousSelectableChannel implements AsynchronousByteChannel {
	
	protected AsynchronousSocketChannel(SocketEngineService engine, SelectableChannel channel) {
		super(engine);
		setChannel(channel);
	}
	
	public Future<Integer> read(ByteBuffer dst) { // TODO throws IOException
		return read(dst, 30, TimeUnit.SECONDS, null, null);
	}

	public <A> Future<Integer> read(ByteBuffer dst,
			long timeout, TimeUnit unit,
			A attachment, CompletionHandler<Integer, ? super A> handler) { // TODO throws IOException
		return engine.asynchronousRead(this, dst, timeout, unit, attachment, handler);
	}

	public Future<Integer> write(ByteBuffer src) { // TODO throws IOException
		return write(src, 30, TimeUnit.SECONDS, null, null);
	}

	public <A> Future<Integer> write(ByteBuffer src,
			long timeout, TimeUnit unit,
			A attachment, CompletionHandler<Integer, ? super A> handler) { // TODO throws IOException
		return engine.asynchronousWrite(this, src, timeout, unit, attachment, handler);
	}
	
	public LineChannel toLineChannel() {
		return new LineChannel(engine, getWrappedChannel(), this);
	}
}
