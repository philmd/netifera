package com.netifera.platform.net.sunrpc.packets;

public abstract class RpcCall extends AbstractRpcPacket {
	private static final long serialVersionUID = -7703844314508330712L;
	
	public final static int PROC_NULL		= 0;
	
	private int rpcVersion;
	private int program;
	private int programVersion;
	private int procedure;

	public RpcCall() {
		super(IRpcPacket.MESSAGE_TYPE_CALL);
		this.rpcVersion = 2;
	}
	
	public void setVersion(final int rpcVersion) {
		this.rpcVersion = rpcVersion;
	}
	
	public int getVersion() {
		return rpcVersion;
	}
	
	public void setProgram(final int program) {
		this.program = program;
	}
	
	public int getProgram() {
		return program;
	}
	
	public void setProgramVersion(final int programVersion) {
		this.programVersion = programVersion;
	}
	
	public int getProgramVersion() {
		return programVersion;
	}
	
	public void setProcedure(final int procedure) {
		this.procedure = procedure;
	}
	
	public int getProcedure() {
		return procedure;
	}
	
	public abstract boolean isValidCredentialFlavor(int flavor, int procedure);
	
	protected boolean isValidCredential() {
		return isValidCredentialFlavor(credential.getFlavor(), procedure);
	}
	
	// pack = [hdr][data]
	public XdrBuffer pack() {
		XdrBuffer packedBuffer = packHeader();

		packedBuffer.xdr_raw(buffer);
		
		packedBuffer.flip();
		
		return packedBuffer;
	}
	
	public XdrBuffer packHeader() {
		if (!isValidCredential()) {
			throw new IllegalArgumentException("cred: " + credential.toString() + " for proc:" + procedure);
		}
		
		XdrBuffer buffer = new XdrBuffer();
		
		buffer.xdr_int(getXID());
		buffer.xdr_int(messageType);
		buffer.xdr_int(rpcVersion);
		buffer.xdr_int(program);
		buffer.xdr_int(programVersion);
		buffer.xdr_int(procedure);
		
		buffer.xdr_int(credential.getFlavor());
		buffer.xdr_xdr(credential.buffer());
		
		buffer.xdr_int(verifier.getFlavor());
		buffer.xdr_xdr(verifier.buffer());
		
		return buffer;
	}
	
	@Override
	public String toString() {
		return String.format("id:%08x type:%d program:%d v%d proc:%d cred:[%s]",
				getXID(), messageType, program, programVersion,
				procedure, "cred");
	}
	
}
