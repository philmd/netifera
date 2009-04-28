package com.netifera.platform.net.sunrpc.packets.portmap;

import com.netifera.platform.util.addresses.inet.InternetAddress;
import com.netifera.platform.util.locators.ISocketLocator;
import com.netifera.platform.util.locators.TCPSocketLocator;
import com.netifera.platform.util.locators.UDPSocketLocator;

public class PortmapEntry {
	
	private final int program;
	private final int version;
	private final int protocol;
	private final int port;
	
	public PortmapEntry(int program, int version, int protocol, int port) {
		this.program = program;
		this.version = version;
		this.protocol = protocol;
		this.port = port;
	}
	
	public int getProgram() {
		return program;
	}
	
	public int getVersion() {
		return version;
	}

	public int getProtocol() {
		return protocol;
	}
	
	public int getPort() {
		return port;
	}

	public ISocketLocator getLocator(InternetAddress address) {
		switch (protocol) {
		case 6:
			return new TCPSocketLocator(address, port);
		case 17:
			return new UDPSocketLocator(address, port);
		}
		throw new UnsupportedOperationException("protocol=" + protocol);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("program:");
		sb.append(program);
		sb.append(" vers:");
		sb.append(version);
		sb.append(" proto:");
		sb.append(protocol == 6 ? "tcp" : (protocol == 17 ? "udp" : "unknown"));
		sb.append(" port:");
		sb.append(port);
		// name
		return sb.toString();
	}
}
