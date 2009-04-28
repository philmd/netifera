package com.netifera.platform.net.sunrpc.packets.portmap;

import java.util.LinkedList;
import java.util.List;

import com.netifera.platform.net.sunrpc.packets.RpcReply;

public class PortmapReply extends RpcReply {
	private static final long serialVersionUID = -6028328100727707250L;

	public PortmapReply(RpcReply reply) {
		super(reply);
	}
	
	public List<PortmapEntry> getServices() {
		List<PortmapEntry> entries = new LinkedList<PortmapEntry>();
		buffer.position(0);
		while (buffer.xdr_bool()) {
			entries.add(new PortmapEntry(buffer.xdr_int(), buffer.xdr_int(), buffer.xdr_int(), buffer.xdr_int()));
		}
		
		return entries;
	}
}
