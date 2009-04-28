package com.netifera.platform.net.sunrpc.packets.rusers;

import java.util.LinkedList;
import java.util.List;

import com.netifera.platform.net.sunrpc.packets.RpcReply;

/*
 * The utmp structure for BSD systems.
 */
public class RusersAllNames2Reply extends RpcReply {
	private static final long serialVersionUID = -6028328100727707250L;

	public RusersAllNames2Reply(RpcReply reply) {
		super(reply);
	}
	
	public List<RusersUtmpEntry> getUtmpEntries() {
		List<RusersUtmpEntry> entries = new LinkedList<RusersUtmpEntry>();
		//buffer.reset();
		int length = buffer.xdr_int();
		for (int i = 0; i < length; i++) {
			entries.add(new RusersUtmpEntry(buffer.xdr_string(), buffer.xdr_string(), buffer.xdr_string(), buffer.xdr_int()));
			buffer.xdr_int(); // FIXME what is that value?
		}
		
		return entries;
	}
}
