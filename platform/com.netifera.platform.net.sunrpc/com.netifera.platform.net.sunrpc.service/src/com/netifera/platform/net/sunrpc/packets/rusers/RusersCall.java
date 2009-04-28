package com.netifera.platform.net.sunrpc.packets.rusers;

import com.netifera.platform.net.sunrpc.packets.RpcCall;
import com.netifera.platform.net.sunrpc.packets.credentials.XdrCred;

public class RusersCall extends RpcCall {
	private static final long serialVersionUID = 5338353922673979779L;

	public final static int RUSERS_PROG = 100002;
	public final static int RUSERSVERS_3 = 3;
	
	public final static int PROC_NUM		= 1;
	public final static int PROC_NAMES		= 2;
	public final static int PROC_ALLNAMES	= 3;

	
	public RusersCall(int programVersion, int procedure) {
		super();
		setVersion(2);
		setProgramVersion(programVersion);
		setProgram(RUSERS_PROG);
		setProcedure(procedure);
	}
	
	@Deprecated // verify
	public boolean isValidCredentialFlavor(int flavor, int procedure) {
		return flavor == XdrCred.AUTH_NONE;
	}
	
	public RusersCall(int procedure) {
		this(RUSERSVERS_3, procedure);
	}
	
	public static RusersCall Num() {
		return new RusersCall(PROC_NUM);
	}
	
	public static RusersCall Names() {
		return new RusersCall(PROC_NAMES);
	}
	
	public static RusersCall AllNames() {
		return new RusersCall(PROC_ALLNAMES);
	}
	
	public static RusersCall AllNames2() {
		return new RusersCall(2, PROC_ALLNAMES);
	}
}
