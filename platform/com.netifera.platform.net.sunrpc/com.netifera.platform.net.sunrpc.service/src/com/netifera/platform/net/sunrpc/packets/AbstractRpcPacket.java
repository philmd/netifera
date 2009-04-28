package com.netifera.platform.net.sunrpc.packets;

import java.io.Serializable;
import java.nio.ByteBuffer;

import com.netifera.platform.net.sunrpc.internal.service.Activator;
import com.netifera.platform.net.sunrpc.packets.credentials.XdrCred;
import com.netifera.platform.net.sunrpc.packets.verifiers.XdrVerifier;

// RFC 1057 : Remote Procedure Call Protocol Specification Version 2

public abstract class AbstractRpcPacket implements IRpcPacket, Serializable {
	private static final long serialVersionUID = 1413141993376591609L;
	
	/* the arguments buffer (no header data) */
	protected final XdrBuffer buffer;
	
	protected int xid = Activator.getInstance().getRPCSocketEngine().nextXID();
	protected int messageType;
	protected XdrCred credential = XdrCred.AUTH_NULL;
	protected XdrVerifier verifier = XdrVerifier.VERIF_NULL;
	
	public AbstractRpcPacket(int messageType) {
		this.messageType = messageType;
		buffer = new XdrBuffer(8192); // FIXME
	}
	
	public AbstractRpcPacket(int messageType, XdrBuffer buffer) {
		this.messageType = messageType;
		this.buffer = buffer.duplicate();
	}
	
	public XdrBuffer xdrBuffer() {
		return buffer;
	}

	public ByteBuffer rawBuffer() {
		return buffer.rawBuffer();
	}

	public int length() {
		return buffer.rawBuffer().limit();
	}
	
	public void setXID(final int xid) {
		this.xid = xid;
	}
	
	public int getXID() { // XXX from engine?
			return xid;
	}

	public void setMessageType(final int messageType) {
		this.messageType = messageType;
	}
	
	public int getMessageType() {
		return messageType;
	}
	
	public void setCredential(XdrCred credential) {
		this.credential = credential;
	}
	
	public XdrCred getCredential() {
		return credential;
	}
	
	public void setVerifier(XdrVerifier verifier) {
		this.verifier = verifier;
	}
	
	public XdrVerifier getVerifier() {
		return verifier;
	}
	
	@Override
	public String toString() {
		return (messageType == MESSAGE_TYPE_CALL ? "CALL"
			: (messageType == MESSAGE_TYPE_REPLY ? "REPLY"
				: "TYPE_" + messageType))
			+ " XID:" + xid + " " + buffer.rawBuffer().toString();
	}
}
