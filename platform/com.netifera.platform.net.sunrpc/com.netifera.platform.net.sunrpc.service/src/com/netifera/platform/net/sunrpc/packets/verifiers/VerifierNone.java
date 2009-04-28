package com.netifera.platform.net.sunrpc.packets.verifiers;

import com.netifera.platform.net.sunrpc.packets.XdrBuffer;

public class VerifierNone extends XdrBuffer implements XdrVerifier {

	public XdrBuffer buffer() {
		return this;
	}

	public int getFlavor() {
		return AUTH_NONE;
	}
	
	@Override
	public String toString() {
		return "VerifierNone";
	}
}
