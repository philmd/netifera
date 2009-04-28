package com.netifera.platform.net.services.ssh;

import com.netifera.platform.net.services.credentials.Credential;

public class SSHPrivKey extends SSHPubKey implements Credential {
	private static final long serialVersionUID = 7310471424966959626L;

	private final String username;
	private final char[] data;
	private String password;

	public SSHPrivKey(String username, char[] data) {
		this(username, data, null);
	}
	
	public SSHPrivKey(String username, char[] data, String password) {
		this.username = username;
		this.data = data;
		this.password = password;
	}

	public String getUsernameString() {
		return username;
	}
	
	public char[] getPrivKeyData() {
		return data;
	}
	
	public SSHPubKey getPubKey() {
		return null; // TODO
	}
	
	public String getPasswordString() {
		return password;
	}
}
