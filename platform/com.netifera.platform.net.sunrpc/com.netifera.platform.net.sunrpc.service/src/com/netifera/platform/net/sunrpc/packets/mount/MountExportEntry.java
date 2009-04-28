package com.netifera.platform.net.sunrpc.packets.mount;

import java.util.List;

public class MountExportEntry {
	
	private final String directory;
	private final List<String> groups;
	
	public MountExportEntry(String directory, List<String> groups) {
		this.directory = directory;
		this.groups = groups;
	}
	
	public String getDirectory() {
		return directory;
	}
	
	public List<String> getGroups() {
		return groups;
	}
	
	@Override
	public String toString() {
		return '[' + directory + "] exported to " + groups;
	}
}
