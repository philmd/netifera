package com.netifera.platform.net.sunrpc.packets.nfs;

import com.netifera.platform.net.sunrpc.packets.RpcCall;
import com.netifera.platform.net.sunrpc.packets.credentials.XdrCred;

/* TODO
 *
 * For efficient operation over a local network, 8192 bytes of data are normally
 * used. This may result in lower-level fragmentation (such as at the IP level).
 * Since some network interfaces may not allow such packets, for operation over
 * slower-speed networks or hosts, or through gateways, transfer sizes of 512 or
 * 1024 bytes often provide better results.
 */
public class Nfs2Call extends RpcCall {
	private static final long serialVersionUID = 5639391337560989103L;

	public final static int NFSVERS	= 2;
	
	public final static int PROC_GETATTR	= 1;
	public final static int PROC_SETATTR	= 2;
	//public final static int PROC_ROOT		= 3;
	public final static int PROC_LOOKUP		= 4;
	public final static int PROC_READLINK	= 5;
	public final static int PROC_READ		= 6;
	//public final static int PROC_WRITECACHE	= 7;
	public final static int PROC_WRITE		= 8;
	public final static int PROC_CREATE		= 9;
	public final static int PROC_REMOVE		= 10;
	public final static int PROC_RENAME		= 11;
	public final static int PROC_LINK		= 12;
	public final static int PROC_SYMLINK	= 13;
	public final static int PROC_MKDIR		= 14;
	public final static int PROC_RMDIR		= 15;
	public final static int PROC_READDIR	= 16;
	public final static int PROC_STATFS		= 17;

	/*
	 * The maximum number of bytes of data in a READ or WRITE
	 * request.
	 */
	int MAXDATA = 8192;

	public boolean isValidCredentialFlavor(int flavor, int procedure) {
		if (procedure == PROC_NULL && flavor == XdrCred.AUTH_NONE) {
			return true;
		}
		switch (flavor) {
		case XdrCred.AUTH_UNIX:
		case XdrCred.AUTH_DES:
		case XdrCred.AUTH_SHORT:
			return true;
		default:
			return false;
		}
	}
}
