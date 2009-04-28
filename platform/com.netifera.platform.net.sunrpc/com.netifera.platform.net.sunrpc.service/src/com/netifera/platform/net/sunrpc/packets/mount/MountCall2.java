package com.netifera.platform.net.sunrpc.packets.mount;

public class MountCall2 extends AbstractMountCall {
	private static final long serialVersionUID = 864553346492469939L;

	public final static int MOUNTVERS = 2;
	
	public final static int PROC_MNT		= 1;
	public final static int PROC_DUMP		= 2;
	public final static int PROC_UMNT		= 3;
	public final static int PROC_UMNTALL	= 4;
	public final static int PROC_EXPORT		= 5;
	public final static int PROC_EXPORTALL	= 6;
	
	public MountCall2(int procedure) {
		super(MOUNTVERS, procedure);
	}

	public boolean isValidCredentialFlavor(int flavor, int procedure) {
		return false;
	}
}
