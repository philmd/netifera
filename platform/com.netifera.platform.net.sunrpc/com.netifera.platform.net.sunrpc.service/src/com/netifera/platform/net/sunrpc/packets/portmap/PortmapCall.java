package com.netifera.platform.net.sunrpc.packets.portmap;

import com.netifera.platform.net.sunrpc.packets.RpcCall;
import com.netifera.platform.net.sunrpc.packets.XdrBuffer;
import com.netifera.platform.net.sunrpc.packets.credentials.XdrCred;

public class PortmapCall extends RpcCall {
	private static final long serialVersionUID = -8934258224521562065L;

	public final static int PORTMAP_PROGRAM = 100000;
	
	public final static int PROC_SET		= 1;
	public final static int PROC_UNSET		= 2;
	public final static int PROC_GETPORT	= 3;
	public final static int PROC_DUMP		= 4;
	public final static int PROC_CALLIT		= 5; // TODO indirect call
	
	public PortmapCall(int program, int programVersion, int procedure) {
		super();
		setVersion(2);
		setProgramVersion(programVersion);
		setProgram(program);
		setProcedure(procedure);
	}
	
	@Deprecated // verify
	public boolean isValidCredentialFlavor(int flavor, int procedure) {
		return flavor == XdrCred.AUTH_NONE;
	}
	
	public static PortmapCall Dump() {
		return new PortmapCall(PORTMAP_PROGRAM, 2, PROC_DUMP);
	}
	
	public static PortmapCall Ping(int program, int programVersion) {
		return new PortmapCall(program, programVersion, PROC_NULL);
	}
	
	public static PortmapCall Callit(RpcCall call) {
		PortmapCall callit = new PortmapCall(PORTMAP_PROGRAM, 2, PROC_CALLIT);
		
		XdrBuffer dst = callit.xdrBuffer();
		
		// call args
		dst.xdr_int(call.getProgram());
		dst.xdr_int(call.getProgramVersion());
		dst.xdr_int(call.getProcedure());
		
		// encap parms
		XdrBuffer src = call.xdrBuffer();
		src.flip();
		dst.xdr_int(src.length());
		dst.xdr_raw(src);
		
		return callit;
	}
}
