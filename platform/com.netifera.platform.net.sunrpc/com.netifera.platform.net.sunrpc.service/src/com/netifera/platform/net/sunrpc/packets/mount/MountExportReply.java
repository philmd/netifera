package com.netifera.platform.net.sunrpc.packets.mount;

import java.util.LinkedList;
import java.util.List;

import com.netifera.platform.net.sunrpc.packets.RpcReply;

public class MountExportReply extends RpcReply {
	private static final long serialVersionUID = 7729630525759508238L;

	public MountExportReply(RpcReply reply) {
		super(reply);
	}
	
	public List<MountExportEntry> getExportList() {
		List<MountExportEntry> entries = new LinkedList<MountExportEntry>();
		while (buffer.xdr_bool()) {
			String dir = buffer.xdr_string();
			List<String> groups = new LinkedList<String>();
			while (buffer.xdr_bool()) {
				groups.add(buffer.xdr_string());
			}
			entries.add(new MountExportEntry(dir, groups));
		}
		
		return entries;
	}
}
