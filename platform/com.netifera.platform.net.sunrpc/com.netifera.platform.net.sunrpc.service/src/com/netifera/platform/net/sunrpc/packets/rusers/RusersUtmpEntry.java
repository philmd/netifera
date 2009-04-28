package com.netifera.platform.net.sunrpc.packets.rusers;

public class RusersUtmpEntry {
	
	public final static int RUSERS_EMPTY			= 0;
	public final static int RUSERS_RUN_LVL			= 1;
	public final static int RUSERS_BOOT_TIME		= 2;
	public final static int RUSERS_OLD_TIME			= 3;
	public final static int RUSERS_NEW_TIME			= 4;
	public final static int RUSERS_INIT_PROCESS		= 5;
	public final static int RUSERS_LOGIN_PROCESS	= 6;
	public final static int RUSERS_USER_PROCESS		= 7;
	public final static int RUSERS_DEAD_PROCESS		= 8;
	public final static int RUSERS_ACCOUNTING		= 9;

	private final String user;
	private final String line;
	private final String host;
	private final int type;
	private final int time; // time of login
	private final int idle;
	
	// v3
	public RusersUtmpEntry(String user, String line, String host, int type, int time, /*unsigned*/ int idle) {
		this.user = user;
		this.line = line;
		this.host = host;
		this.type = type;
		this.time = time;
		this.idle = idle;
	}
	
	// v2
	public RusersUtmpEntry(String line, String user, String host, /*unsigned*/ int time) {
		this.user = user;
		this.line = line;
		this.host = host;
		this.type = RUSERS_USER_PROCESS;
		this.time = time;
		this.idle = 0;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getLine() {
		return line;
	}
	
	public String getHost() {
		return host;
	}
	
	public int getVersion() {
		return type;
	}

	public int getTime() {
		return time;
	}
	
	public int getIdle() {
		return idle;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("user:");
		sb.append(user);
		sb.append(" line:");
		sb.append(line);
		sb.append(" host:");
		sb.append(host);
		sb.append(" type:");
		sb.append(type);
		sb.append(String.format(" time:%08x", time));
		sb.append(" idle:");
		sb.append(idle);
		return sb.toString();
	}
}
