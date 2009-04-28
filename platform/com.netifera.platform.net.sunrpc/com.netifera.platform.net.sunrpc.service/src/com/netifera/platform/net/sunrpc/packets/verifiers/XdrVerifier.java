package com.netifera.platform.net.sunrpc.packets.verifiers;

import com.netifera.platform.net.sunrpc.packets.XdrBuffer;

public interface XdrVerifier {
	int AUTH_NONE = 0;
	
	XdrVerifier VERIF_NULL = new VerifierNone();
	
	XdrBuffer buffer();
	
	int getFlavor();
}
