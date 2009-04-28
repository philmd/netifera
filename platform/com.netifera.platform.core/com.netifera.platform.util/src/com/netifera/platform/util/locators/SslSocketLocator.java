package com.netifera.platform.util.locators;

import com.netifera.platform.util.addresses.inet.InternetAddress;

public class SslSocketLocator extends InetSocketLocator {
	private static final long serialVersionUID = -1105335082340898775L;

	public static final String PROTO_NAME = "ssl";
	
	private SslSocketLocator(InternetAddress address, int port) {
		super(address, port, PROTO_TCP);
	}
	
	@Override
	public String getProtocol() {
		return PROTO_NAME;
	}
	
	public SslSocketLocator createSSL(InternetAddress address, int port) {
		return new SslSocketLocator(address, port);
	}
}
