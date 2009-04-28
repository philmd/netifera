package com.netifera.platform.net.sunrpc.packets.credentials;

import com.netifera.platform.net.sunrpc.packets.XdrBuffer;

public class CredNone extends XdrBuffer implements XdrCred {
	private static final long serialVersionUID = -628145047531951157L;

	public XdrBuffer buffer() {
		return this;
	}
	
	public int getFlavor() {
		return AUTH_NONE;
	}
	
	@Override
	public String toString() {
		return "CredNone";
	}
}
