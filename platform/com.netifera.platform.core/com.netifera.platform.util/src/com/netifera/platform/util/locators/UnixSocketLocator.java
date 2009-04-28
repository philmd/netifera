package com.netifera.platform.util.locators;

public class UnixSocketLocator implements SocketLocator {
	private static final long serialVersionUID = -1105335082340898775L;

	public static final String FAMILY_NAME = "unix";
	
	private final String path;
	
	public UnixSocketLocator(String path) {
		this.path = path;
	}
	
	public String getFamily() {
		return FAMILY_NAME;
	}
	
	public String getPath() {
		return path;
	}
	
	public static UnixSocketLocator create(String path) {
		return new UnixSocketLocator(path);
	}
}
