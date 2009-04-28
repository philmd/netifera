package com.netifera.platform.net.sunrpc.tools;

import java.util.concurrent.TimeUnit;

import com.netifera.platform.api.probe.IProbe;
import com.netifera.platform.api.tools.IToolContext;
import com.netifera.platform.api.tools.ToolException;
import com.netifera.platform.net.sockets.CompletionHandler;
import com.netifera.platform.net.sunrpc.internal.tools.Activator;
import com.netifera.platform.net.sunrpc.packets.RpcReply;
import com.netifera.platform.net.sunrpc.packets.ugid.UgidCall;
import com.netifera.platform.net.sunrpc.sockets.IRPCChannel;
import com.netifera.platform.net.sunrpc.sockets.RpcSocketLocator;
import com.netifera.platform.tools.RequiredOptionMissingException;

// ugidd[24120] 04/21/109 08:41 client 127.0.0.1 called from illegal port 32938

public class UgidClient extends AbstractRpcTool {
	public final static int UGID_PROG = UgidCall.UGID_PROG;

	int timeout;
	
	public void toolRun(IToolContext context) throws ToolException {
		this.context = context;

		// XXX hardcode local probe as realm
		IProbe probe = Activator.getInstance().getProbeManager().getLocalProbe();
		realm = probe.getEntity().getId();
		
		setupToolOptions();

		context.setTitle("Listing users at " + address + "/" + port + "/" + protocol);
		context.setTotalWork(1);
		
		RpcSocketLocator locator = createLocator(port, UgidCall.UGID_PROG, UgidCall.UGID_VERS);
		if (locator == null) {
			context.error("Unknown protocol: " + protocol);
		} else {
			usernamesList(locator);
		}
		context.done();
	}
	
	@Override
	protected void setupRPCToolOptions() throws RequiredOptionMissingException {
		if (context.getConfiguration().get(TARGET_PORT_KEY) != null) {
			port = ((Integer)context.getConfiguration().get(TARGET_PORT_KEY)).intValue();
		}
		
		timeout = 30;
	}
	
	private void usernamesList(final RpcSocketLocator locator) {
		IRPCChannel channel;
		try {
			channel = Activator.getInstance().getRPCSocketEngine().openRPC(IRPCChannel.FLAG_PRIVILEGED);
			if (channel.connect(locator, timeout, TimeUnit.SECONDS)) {
				
				for (int i = 1000; i < 1040; i++) {
					UgidCall call = UgidCall.NameByUid();
					final int uid = i;
					call.xdrBuffer().xdr_int(uid);
				
					channel.asynchronousCall(call, timeout, TimeUnit.SECONDS, null, new CompletionHandler<RpcReply, Void>() {

						public void cancelled(Void attachment) {
							context.error("rpc error (cancelled)");
						}

						public void completed(RpcReply result, Void attachment) {
							String name = result.xdrBuffer().xdr_string();
							if (name.length() > 0) {
								context.info("found username [" + name + "] with uid " + uid);
								Activator.getInstance().getNetworkEntityFactory().createUser(realm, context.getSpaceId(), locator.getAddress(), name);
							}
						}

						public void failed(Throwable exc, Void attachment) {
							context.error("rpc error " + exc);
							// if PortUnreach -> stop
						}
					});
				}
				
				channel.close();
			} else {
				context.error("can not connect");
			}
		} catch (Exception e) {
			context.exception(e.getMessage(), e);
		}
		context.worked(1);
	}

}
