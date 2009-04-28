package com.netifera.platform.net.sunrpc.packets;

import java.nio.ByteBuffer;

import com.netifera.platform.net.sunrpc.packets.credentials.XdrCred;
import com.netifera.platform.net.sunrpc.packets.verifiers.XdrVerifier;

public interface IRpcPacket {
	
	int MESSAGE_TYPE_CALL = 0;
	int MESSAGE_TYPE_REPLY = 1;
	
	void setMessageType(int messageType);
	int getMessageType();
	
	XdrBuffer xdrBuffer();
	ByteBuffer rawBuffer();
	
	int length();

	void setXID(int xid);
	int getXID();
	
	void setCredential(XdrCred credentials);
	XdrCred getCredential();
	
	void setVerifier(XdrVerifier verifier);
	XdrVerifier getVerifier();

	//void packHeader(IRPCChannel channel);
	//void unpackHeader(IRPCChannel channel);
}
