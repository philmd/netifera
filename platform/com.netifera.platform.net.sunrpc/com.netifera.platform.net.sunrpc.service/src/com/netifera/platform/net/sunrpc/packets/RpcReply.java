package com.netifera.platform.net.sunrpc.packets;

import java.nio.ByteBuffer;
import java.util.Set;

import com.netifera.platform.net.sunrpc.packets.credentials.CredNone;
import com.netifera.platform.net.sunrpc.packets.credentials.XdrCred;
import com.netifera.platform.net.sunrpc.packets.states.AcceptState;

public class RpcReply extends AbstractRpcPacket {
	private static final long serialVersionUID = 5471639775003606662L;

	public final static int REPLY_STATE_ACCEPTED	= 0;
	public final static int REPLY_STATE_DENIED		= 1;
	
	public final static int ACCEPT_STATE_SUCCESS		= 0;
	public final static int ACCEPT_STATE_PROG_UNAVAIL	= 1;
	public final static int ACCEPT_STATE_PROG_MISMATCH	= 2;
	public final static int ACCEPT_STATE_PROC_UNAVAIL	= 3;
	public final static int ACCEPT_STATE_GARBAGE_ARGS	= 4;
	public final static int ACCEPT_STATE_SYSTEM_ERR		= 5;
	
	public final static int REJECT_STATE_RPC_MISMATCH	= 0;
	public final static int REJECT_STATE_AUTH_ERROR		= 1;
	
	private int replyState;
	private AcceptState acceptState;

	public RpcReply() {
		super(IRpcPacket.MESSAGE_TYPE_REPLY);
	}
	
	public RpcReply(RpcReply reply) {
		super(IRpcPacket.MESSAGE_TYPE_REPLY, reply.buffer);
		replyState = reply.replyState;
		acceptState = reply.acceptState;
	}
	
	//public RPCReply(RPCReply reply) {
	//	this(reply.xdrBuffer());
	//}

	public void setReplyState(int state) {
		replyState = state;
	}
	
	public long getReplyState() {
		return replyState;
	}
	
	public void setAcceptState(AcceptState state) {
		acceptState = state;
	}
	
	public AcceptState getAcceptState() {
		return acceptState;
	}
	
	public void unpack(ByteBuffer pktBuffer) {
		setXID(pktBuffer.getInt());
		if (pktBuffer.getInt() != messageType) {
			// TODO error
			throw new AssertionError("messageType " + messageType);
		}
		replyState = pktBuffer.getInt();
		
		int verifierFlavor = pktBuffer.getInt();
		int verifierLength = pktBuffer.getInt();
		if (verifierLength > 0) {
			byte[] verifierData = new byte[verifierLength];
			pktBuffer.get(verifierData);
		}
		switch (verifierFlavor) {
		case XdrCred.AUTH_NONE:
			setCredential(new CredNone());
			break;
		default:
			// TODO
			throw new UnsupportedOperationException();
		}
		
		acceptState = AcceptState.byValue(pktBuffer.getInt());
		switch (acceptState) {
			// TODO
		}
		
		buffer.clear();
		buffer.rawBuffer().put(pktBuffer);
		buffer.flip();
	}

	public Set<Integer> getValidCredentialFlavors() {
		// TODO Auto-generated method stub
		return null;
	}
}
