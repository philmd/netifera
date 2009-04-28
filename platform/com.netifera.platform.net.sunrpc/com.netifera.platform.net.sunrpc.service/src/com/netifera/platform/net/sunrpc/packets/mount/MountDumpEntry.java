package com.netifera.platform.net.sunrpc.packets.mount;

public class MountDumpEntry {
	
	private final String hostname;
	private final String directory;
	
	public MountDumpEntry(String hostname, String directory) {
		this.hostname = hostname;
		this.directory = directory;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public String getDirectory() {
		return directory;
	}
	
	@Override
	public String toString() {
		return '[' + directory + "] mounted  by " + hostname;
	}
}
