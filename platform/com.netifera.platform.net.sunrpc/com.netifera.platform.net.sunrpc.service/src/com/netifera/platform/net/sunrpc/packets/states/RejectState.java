package com.netifera.platform.net.sunrpc.packets.states;

public enum RejectState {
	RPC_MISMATCH(0),
	AUTH_ERROR(1);
	
	private final int value;
	
	private RejectState(final int value) {
		this.value = value;
	}
	
	public int value() {
		return value;
	}
	
	public static RejectState byValue(final int value) {
		for (RejectState state: values()) {
			if (state.value == value) {
				return state;
			}
		}
		throw new IllegalArgumentException("No MessageType with value "
				+ value);
	}
}
