package com.netifera.platform.net.sunrpc.sockets;

import java.io.IOException;

public class RPCException extends IOException {

	private static final long serialVersionUID = 1L;

	public RPCException(String message) {
    	super(message);
    }

    public RPCException() {}

	public RPCException(String message, Throwable cause) {
    	super(message);
	}
}

