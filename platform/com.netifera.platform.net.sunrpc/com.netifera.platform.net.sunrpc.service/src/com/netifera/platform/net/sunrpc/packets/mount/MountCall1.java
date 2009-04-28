package com.netifera.platform.net.sunrpc.packets.mount;

import com.netifera.platform.net.sunrpc.packets.credentials.XdrCred;

// RFC 1094
// Version 1 of the mount protocol used with version 2 of the NFS protocol.
public class MountCall1 extends AbstractMountCall {
	private static final long serialVersionUID = -56588396047339161L;
	
	public final static int MOUNTVERS = 1;
	
	public final static int PROC_MNT		= 1;
	public final static int PROC_DUMP		= 2;
	public final static int PROC_UMNT		= 3;
	public final static int PROC_UMNTALL	= 4;
	public final static int PROC_EXPORT		= 5;
	
	public MountCall1(int procedure) {
		super(MOUNTVERS, procedure);
	}

	public boolean isValidCredentialFlavor(int flavor, int procedure) {
		return flavor == XdrCred.AUTH_NONE || flavor == XdrCred.AUTH_UNIX;
	}
	
	public static MountCall1 Export() {
		return new MountCall1(PROC_EXPORT);
	}
	
	public static MountCall1 Dump() {
		return new MountCall1(PROC_DUMP);
	}
	
	public static MountCall1 Mnt(String path) {
		MountCall1 mnt = new MountCall1(PROC_MNT);
		mnt.xdrBuffer().xdr_string(path);
		return mnt;
	}
	
	public static MountCall1 Umnt(String path) {
		MountCall1 umnt = new MountCall1(PROC_UMNT);
		umnt.xdrBuffer().xdr_string(path);
		return umnt;
	}
}
