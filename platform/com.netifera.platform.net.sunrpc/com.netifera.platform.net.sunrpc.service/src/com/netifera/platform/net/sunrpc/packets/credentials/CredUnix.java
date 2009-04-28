package com.netifera.platform.net.sunrpc.packets.credentials;

import java.util.Calendar;

import com.netifera.platform.net.sunrpc.packets.XdrBuffer;

public class CredUnix extends XdrBuffer implements XdrCred {
	private static final long serialVersionUID = -628145047531951157L;

	public XdrBuffer buffer() {
		return this;
	}

	public CredUnix() {
		this(0, 0, "localhost", new int[] {0, 1, 2, 3, 4, 5});
	}
	
	public CredUnix(int uid, int gid, String machineName, int[] gids) {
		super();
		
		xdr_int((int) Calendar.getInstance().getTimeInMillis());
		
		xdr_string(machineName);
		
		xdr_int(uid);
		xdr_int(gid);
		
		xdr_int(gids.length);
		for (int g : gids) {
			xdr_int(g);
		}
	}
	
	public int getFlavor() {
		return AUTH_UNIX;
	}
	
	@Override
	public String toString() {
		return "CredUnix";
	}
}
