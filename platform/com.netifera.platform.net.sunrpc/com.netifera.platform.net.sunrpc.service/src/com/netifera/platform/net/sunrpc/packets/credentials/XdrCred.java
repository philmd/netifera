package com.netifera.platform.net.sunrpc.packets.credentials;

import com.netifera.platform.net.services.credentials.Credential;
import com.netifera.platform.net.sunrpc.packets.XdrBuffer;

// see rfc 2695
public interface XdrCred extends Credential {
	int AUTH_NONE = 0; // AUTH_NULL
	int AUTH_UNIX = 1;
	int AUTH_SHORT = 2; // AUTH_SYS
	int AUTH_DES = 3; // AUTH_DH
	int AUTH_KERB = 4;
	int AUTH_RPCSEC_GSS_KRB5 = 390003;
	int AUTH_RPCSEC_GSS_KRB5I = 390004;
	int AUTH_RPCSEC_GSS_KRB5P = 390005;
	
	XdrCred AUTH_NULL = new CredNone();
	
	XdrBuffer buffer();
	
	int getFlavor();
	
	//boolean validate(...);
}
