package com.netifera.platform.net.sunrpc.packets.mount;

public abstract class FileHandle {

	private byte[] data;
	
	public byte[] getData() {
		return data;
	}
	
	// TODO nfs2:32 nfs3:64 nfs4:128
	public abstract int getSizeMax();
}
