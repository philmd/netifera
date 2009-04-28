package com.netifera.platform.net.sunrpc.packets.mount;

import com.netifera.platform.net.sunrpc.packets.RpcCall;

public abstract class AbstractMountCall extends RpcCall {
	private static final long serialVersionUID = 7063315963892546617L;
	
	public final static int MOUNT_PROG	= 100005;
	
	protected AbstractMountCall(int version, int procedure) {
		super();
		setVersion(2);
		setProgramVersion(version);
		setProgram(MOUNT_PROG);
		setProcedure(procedure);
	}
}
