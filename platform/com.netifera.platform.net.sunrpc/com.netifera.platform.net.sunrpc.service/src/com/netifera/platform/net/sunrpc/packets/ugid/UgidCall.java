package com.netifera.platform.net.sunrpc.packets.ugid;

import com.netifera.platform.net.sunrpc.packets.RpcCall;
import com.netifera.platform.net.sunrpc.packets.credentials.XdrCred;

public class UgidCall extends RpcCall {
	private static final long serialVersionUID = 6329717200838419270L;
	
	public final static int UGID_PROG = 0x2084e581;
	public final static int UGID_VERS = 1;
	
	public final static int PROC_AUTHENTICATE	= 1;
	public final static int PROC_UID			= 2;
	public final static int PROC_GID			= 3;
	public final static int PROC_NAME			= 4;
	public final static int PROC_GROUP			= 5;
	
	public UgidCall(int procedure) {
		super();
		setVersion(2);
		setProgramVersion(UGID_VERS);
		setProgram(UGID_PROG);
		setProcedure(procedure);
	}
	
	@Deprecated // verify
	public boolean isValidCredentialFlavor(int flavor, int procedure) {
		return flavor == XdrCred.AUTH_NONE;
	}
	
	public static UgidCall UidByName() {
		return new UgidCall(PROC_UID);
	}
	
	public static UgidCall GidByName() {
		return new UgidCall(PROC_GID);
	}
	
	public static UgidCall NameByUid() {
		return new UgidCall(PROC_NAME);
	}
	
	public static UgidCall NameByGid() {
		return new UgidCall(PROC_GROUP);
	}
}
