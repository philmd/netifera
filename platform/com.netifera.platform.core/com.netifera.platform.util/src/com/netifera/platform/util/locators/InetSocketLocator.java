package com.netifera.platform.util.locators;

import java.net.InetSocketAddress;

import com.netifera.platform.util.addresses.inet.InternetAddress;

public class InetSocketLocator implements SocketLocator, ISocketLocator {
	private static final long serialVersionUID = 2723774071711129034L;
	
	public static final String FAMILY_NAME = "inet";
	protected static final String PROTO_TCP = "tcp";
	protected static final String PROTO_UDP = "udp";
	
	protected final InternetAddress address;
	protected final int port;
	protected final String protocol;
	
	protected InetSocketLocator(InternetAddress address, int port, String protocol) {
		this.address = address;
		this.port = port;
		this.protocol = protocol;
	}
	
	protected InetSocketLocator newInstance() {
		return new InetSocketLocator(address, port, protocol);
	}
	
	public String getFamily() {
		return FAMILY_NAME;
	}
	
	public InternetAddress getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getProtocol() {
		return protocol;
	}
	
	public InetSocketAddress toInetSocketAddress() {
		return new InetSocketAddress(address.toInetAddress(), port);
	}
	
	public boolean isTCP() {
		return protocol.equals(PROTO_TCP);
	}
	
	public boolean isUDP() {
		return protocol.equals(PROTO_UDP);
	}
	
	@Override
	public String toString() {
		return getAddress().toStringLiteral() + ':'
			+ Integer.toString(getPort()) + '/' + getProtocol();
	}
	
	@Override
	public int hashCode() {
		return (address.hashCode() ^ port) + protocol.hashCode() ; // FIXME
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (!(obj instanceof InetSocketLocator)){
			return false;
		}
		InetSocketLocator other = (InetSocketLocator)obj;
		return port == other.port && protocol.equals(other.protocol) && address.equals(other.address);
	}
	
	public static InetSocketLocator createTcp(InternetAddress address, int port) {
		return new InetSocketLocator(address, port, PROTO_TCP);
	}
	
	public static InetSocketLocator createUdp(InternetAddress address, int port) {
		return new InetSocketLocator(address, port, PROTO_UDP);
	}
	
	@Deprecated // remove this function once UDPSocketLocator got removed
	public UDPSocketLocator getUdp() {
		assert isUDP();
		return new UDPSocketLocator(address, port);
	}
	
	@Deprecated // remove this function once TCPSocketLocator got removed
	public TCPSocketLocator getTcp() {
		assert isTCP();
		return new TCPSocketLocator(address, port);
	}
}
