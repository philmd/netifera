package com.netifera.platform.net.sunrpc.packets.states;

public enum ReplyState {
	MSG_ACCEPTED(0),
	MSG_DENIED(1);
	
	private final int value;
	
	private ReplyState(final int value) {
		this.value = value;
	}
	
	public int value() {
		return value;
	}
	
	public static ReplyState byValue(final int value) {
		for (ReplyState state: values()) {
			if (state.value == value) {
				return state;
			}
		}
		throw new IllegalArgumentException("No MessageType with value "
				+ value);
	}
}
