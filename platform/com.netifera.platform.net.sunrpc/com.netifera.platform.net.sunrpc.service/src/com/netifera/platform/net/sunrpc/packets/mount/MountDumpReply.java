package com.netifera.platform.net.sunrpc.packets.mount;

import java.util.LinkedList;
import java.util.List;

import com.netifera.platform.net.sunrpc.packets.RpcReply;

public class MountDumpReply extends RpcReply {
	private static final long serialVersionUID = 7729630525759508238L;

	public MountDumpReply(RpcReply reply) {
		super(reply);
	}
	
	public List<MountDumpEntry> getDumpList() {
		List<MountDumpEntry> entries = new LinkedList<MountDumpEntry>();
		while (buffer.xdr_bool()) {
			entries.add(new MountDumpEntry(buffer.xdr_string(), buffer.xdr_string()));
		}
		
		return entries;
	}
}
