package com.netifera.platform.net.sunrpc.sockets;

import java.io.IOException;

public interface IRPCSocketEngineService {

	/* cached, unprivileged channel */
	IRPCChannel openRPC() throws IOException;
	
	IRPCChannel openRPC(int flags) throws IOException;
	
	// TODO RpcServicesEntryTable functions
}
