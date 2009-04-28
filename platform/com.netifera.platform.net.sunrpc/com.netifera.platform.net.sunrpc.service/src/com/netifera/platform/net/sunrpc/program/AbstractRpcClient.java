package com.netifera.platform.net.sunrpc.program;

import java.io.IOException;

import com.netifera.platform.api.log.ILogger;
import com.netifera.platform.net.sunrpc.RpcServiceEntryTable;
import com.netifera.platform.net.sunrpc.internal.service.Activator;
import com.netifera.platform.net.sunrpc.sockets.IRPCChannel;
import com.netifera.platform.net.sunrpc.sockets.RpcSocketLocator;

public class AbstractRpcClient {

	protected final RpcSocketLocator locator;
	protected final ILogger logger;
	
	public AbstractRpcClient(RpcSocketLocator locator, int expectedProgram, ILogger logger) {
		if (locator.getRpcProgram() != expectedProgram) {
			throw new IllegalArgumentException(locator.toString() + " is not "
					+ RpcServiceEntryTable.getName(expectedProgram));
		}
		this.locator = locator;
		this.logger = logger;
	}
	
	IRPCChannel openChannel() throws IOException {
		return Activator.getInstance().getRPCSocketEngine().openRPC();
	}
}
