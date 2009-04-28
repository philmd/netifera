package com.netifera.platform.net.sunrpc.packets.states;

public enum AuthState {
	AUTH_BADCRED(1),
	AUTH_REJECTEDCRED(2),
	AUTH_BADVERF(3),
	AUTH_REJECTEDVERF(4),
	AUTH_TOOWEAK(5),
	RPCSEC_GSSCREDPROB(13),
	RPCSEC_GSSCTXPROB(14);
	
	private final int value;
	
	private AuthState(final int value) {
		this.value = value;
	}
	
	public int value() {
		return value;
	}
	
	public static AuthState byValue(final int value) {
		for (AuthState state: values()) {
			if (state.value == value) {
				return state;
			}
		}
		throw new IllegalArgumentException("No MessageType with value "
				+ value);
	}
}
