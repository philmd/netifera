package com.netifera.platform.net.sunrpc.packets.states;

public enum AcceptState {
	SUCCESS(0),
	PROG_UNAVAIL(1),
	PROG_MISMATCH(2),
	PROC_UNAVAIL(3),
	GARBAGE_ARGS(4),
	SYSTEM_ERROR(5);
	
	private final int value;
	
	private AcceptState(final int value) {
		this.value = value;
	}
	
	public int value() {
		return value;
	}
	
	public static AcceptState byValue(final int value) {
		for (AcceptState state: values()) {
			if (state.value == value) {
				return state;
			}
		}
		throw new IllegalArgumentException("No MessageType with value "
				+ value);
	}
}
