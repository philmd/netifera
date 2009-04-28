package com.netifera.platform.net.sunrpc.tools;

import com.netifera.platform.api.log.ILogManager;
import com.netifera.platform.api.log.ILogger;
import com.netifera.platform.api.tools.ITool;
import com.netifera.platform.api.tools.IToolConfiguration;
import com.netifera.platform.api.tools.IToolContext;
import com.netifera.platform.net.sunrpc.packets.RpcCall;
import com.netifera.platform.net.sunrpc.packets.RpcReply;
import com.netifera.platform.net.sunrpc.packets.XdrBuffer;
import com.netifera.platform.net.sunrpc.packets.portmap.PortmapCall;
import com.netifera.platform.net.sunrpc.sockets.RpcSocketLocator;
import com.netifera.platform.tools.RequiredOptionMissingException;
import com.netifera.platform.util.addresses.inet.InternetAddress;

public abstract class AbstractRpcTool implements ITool {
	public static final String TARGET_ADDRESS_KEY = "target";
	public static final String TARGET_PORT_KEY = "port";
	public static final String TARGET_PROTOCOL_KEY = "protocol";
	public static final String PROCEDURE_KEY = "procedure";
	public static final String USE_PORTMAPPER_KEY = "via.portmapper";
	
	protected InternetAddress address;
	protected int port;
	protected String protocol;
	protected IToolContext context;
	protected long realm;
	protected String procedure;
	@Deprecated // use RpcSocketLocator
	protected boolean viaPortmapper;
	
	@Deprecated // sorry ugly and not designed to use that way
	protected ILogger logger = new ILogger() {

		public void debug(String message) {
			context.debug(message);
		}

		public void debug(String message, Throwable exception) {
			context.debug(message);
			context.exception(message, exception);
		}

		public void disableDebug() {
			// TODO Auto-generated method stub
			
		}

		public void enableDebug() {
			// TODO Auto-generated method stub
			
		}

		public void error(String message) {
			context.error(message);
		}

		public void error(String message, Throwable exception) {
			context.error(message);
			context.exception(message, exception);
		}

		public ILogManager getManager() {
			// TODO Auto-generated method stub
			return null;
		}

		public void info(String message) {
			context.info(message);
		}

		public void info(String message, Throwable exception) {
			context.info(message);
			context.exception(message, exception);
		}

		public void warning(String message) {
			context.warning(message);
		}

		public void warning(String message, Throwable exception) {
			context.warning(message);
			context.exception(message, exception);
		}
		
	};
	
	protected RpcSocketLocator createLocator(int port, int program, int version) {
		if (viaPortmapper) {
			port = 111;
		}
		// FIXME
		if ("TCP".equalsIgnoreCase(protocol)) {
			return RpcSocketLocator.createRPC(RpcSocketLocator.createTcp(address, port), program, version);
		} else if ("UDP".equalsIgnoreCase(protocol)) {
			return RpcSocketLocator.createRPC(RpcSocketLocator.createUdp(address, port), program, version);
		}
		return null;
	}
	
	protected void setupRPCToolOptions() throws RequiredOptionMissingException {
		// overwrite
	}
	
	protected final void setupToolOptions() throws RequiredOptionMissingException {
		IToolConfiguration config = context.getConfiguration();
		
		String addr = (String)(config.get(TARGET_ADDRESS_KEY));
		if(addr == null) {
			throw new RequiredOptionMissingException(TARGET_ADDRESS_KEY);
		}
		address = InternetAddress.fromString(addr);
		
		protocol = (String)(config.get(TARGET_PROTOCOL_KEY));
		
		procedure = (String)(config.get(PROCEDURE_KEY));
		
		if (config.get(USE_PORTMAPPER_KEY) != null) {
			viaPortmapper = (Boolean)(config.get(USE_PORTMAPPER_KEY));
		}
		
		setupRPCToolOptions();
	}
	
	protected RpcCall pkt(RpcCall call) {
		if (!viaPortmapper) {
			return call;
		}
		return PortmapCall.Callit(call);
	}
	
	protected RpcReply pkt(RpcReply reply) {
		if (!viaPortmapper) {
			return reply;
		}

		XdrBuffer buf = reply.xdrBuffer();
		/*int port =*/ buf.xdr_int();
		// encap args
		byte[] args = buf.xdr_bytes();
		buf.clear();
		buf.xdr_raw(args);
		buf.flip();

		return reply;
	}
}
