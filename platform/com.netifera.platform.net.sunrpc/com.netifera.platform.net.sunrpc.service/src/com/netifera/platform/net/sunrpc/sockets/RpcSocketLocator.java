package com.netifera.platform.net.sunrpc.sockets;

import com.netifera.platform.net.sunrpc.RpcServiceEntryTable;
import com.netifera.platform.util.addresses.inet.InternetAddress;
import com.netifera.platform.util.locators.InetSocketLocator;

public class RpcSocketLocator extends InetSocketLocator {
	private static final long serialVersionUID = 1377687423850182859L;
	
	public static final String PROTO_NAME = "rpc";
	public static final int PORTMAPPER_PORT = 111;
	
	private final boolean indirectCall;
	private final int program;
	private final int version;

	private RpcSocketLocator(InternetAddress address, int port, String protocol, int program, int version, boolean indirectCall) {
		super(address, port, protocol);
		this.program = program;
		this.version = version;
		this.indirectCall = indirectCall;
	}
	
	@Override
	public int getPort() {
		if (indirectCall) {
			return PORTMAPPER_PORT;
		}
		return port;
	}
	
	public int getRpcPort() {
		return port;
	}
	
	public int getRpcProgram() {
		return program;
	}
	
	public int getRpcVersion() {
		return version;
	}
	
	public InetSocketLocator getInetSocketLocator() {
		return super.newInstance();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		
		sb.append(RpcServiceEntryTable.getName(program));
		sb.append(" v");
		sb.append(version);
		sb.append(" @");
		sb.append(getAddress().toStringLiteral());
		sb.append(':');
		sb.append(getPort());
		if (indirectCall) {
			sb.append("@111");
		}
		sb.append('/');
		sb.append(PROTO_NAME);
		sb.append('/');
		sb.append(getProtocol());
		
		return sb.toString();
	}
	
	public static RpcSocketLocator createRPC(InetSocketLocator locator, int program, int version) {
		return new RpcSocketLocator(locator.getAddress(), locator.getPort(), locator.getProtocol(), program, version, false);
	}
	
	public static RpcSocketLocator createIndirectRPC(InetSocketLocator locator, int program, int version) {
		return new RpcSocketLocator(locator.getAddress(), locator.getPort(), locator.getProtocol(), program, version, true);
	}
	/*
	public static RpcSocketLocator createRPC(InternetAddress address, int port, int program, int version) {
		return new RpcSocketLocator(address, port, program, version, false);
	}
	
	public static RpcSocketLocator createIndirectRPC(InternetAddress address, int port, int program, int version) {
		return new RpcSocketLocator(address, port, program, version, true);
	}
	*/
}
