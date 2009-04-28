package com.netifera.platform.net.sockets;

import java.io.IOException;
import java.nio.channels.SelectableChannel;

import com.netifera.platform.net.sockets.internal.SocketEngineService;

public abstract class AsynchronousSelectableChannel implements AsynchronousChannel {
	protected final SocketEngineService engine;
	
	/** The socket or datagram channel which is wrapped */
	private SelectableChannel channel;
	
	protected AsynchronousSelectableChannel(SocketEngineService engine) {
		this.engine = engine;
	}
	
	protected final void setChannel(SelectableChannel channel) {
		this.channel = channel;
	}
	
	public SelectableChannel getWrappedChannel() {
		return channel;
	}
	
	public void close() throws IOException {
		try {
			channel.close();
		} finally {
			engine.unregisterChannel(this);
		}
	}

	public boolean isOpen() {
		return channel.isOpen();
	}
	
	@Override
	protected void finalize() {
		try {
			close();
		} catch (IOException e) {
		}
	}
}
