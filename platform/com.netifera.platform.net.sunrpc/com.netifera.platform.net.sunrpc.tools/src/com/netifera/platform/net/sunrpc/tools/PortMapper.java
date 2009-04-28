package com.netifera.platform.net.sunrpc.tools;

import java.util.concurrent.TimeUnit;

import com.netifera.platform.api.probe.IProbe;
import com.netifera.platform.api.tools.IToolContext;
import com.netifera.platform.api.tools.ToolException;
import com.netifera.platform.net.sockets.CompletionHandler;
import com.netifera.platform.net.sunrpc.internal.tools.Activator;
import com.netifera.platform.net.sunrpc.packets.RpcReply;
import com.netifera.platform.net.sunrpc.packets.portmap.PortmapCall;
import com.netifera.platform.net.sunrpc.packets.portmap.PortmapEntry;
import com.netifera.platform.net.sunrpc.packets.portmap.PortmapReply;
import com.netifera.platform.net.sunrpc.sockets.IRPCChannel;
import com.netifera.platform.net.sunrpc.sockets.RpcSocketLocator;
import com.netifera.platform.tools.RequiredOptionMissingException;

public class PortMapper extends AbstractRpcTool {
	public final static int PORTMAP_PROG = PortmapCall.PORTMAP_PROGRAM;
	
	public final static int PORTMAP_PORT = 111;
	
	int timeout;
	
	public void toolRun(IToolContext context) throws ToolException {
		this.context = context;

		// XXX hardcode local probe as realm
		IProbe probe = Activator.getInstance().getProbeManager().getLocalProbe();
		realm = probe.getEntity().getId();
		
		setupToolOptions();

		if (port != PORTMAP_PORT) {
			context.setTitle("Portmapping " + address + "/" + port + "/" + protocol);
		} else {
			context.setTitle("Portmapping " + address + "/" + protocol);
		}
		context.setTotalWork(1);
		
		RpcSocketLocator locator = createLocator(port, PortmapCall.PORTMAP_PROGRAM, 1);
		if (locator == null) {
			context.error("Unknown protocol: " + protocol);
		} else {
			//if (procedure.equals(PROCEDURE_DUMP)) {
				portmapDump(locator);
			//}
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
	
	private void portmapDump(final RpcSocketLocator locator) {
		IRPCChannel channel;
		try {
			channel = Activator.getInstance().getRPCSocketEngine().openRPC();
			if (channel.connect(locator, timeout, TimeUnit.SECONDS)) {
				channel.asynchronousCall(PortmapCall.Dump(), timeout, TimeUnit.SECONDS, null, new CompletionHandler<RpcReply, Void>() {

					public void cancelled(Void attachment) {
						context.error("rpc error (cancelled)");
					}

					public void completed(RpcReply result, Void attachment) {
						PortmapReply reply = new PortmapReply(result);
						for (PortmapEntry service : reply.getServices()) {
							context.info("found " + service);
							Activator.getInstance().getRpcNetworkEntityFactory().createRpcService(realm, context.getSpaceId(), service.getLocator(locator.getAddress()), service.getProgram(), service.getVersion(), null);
						}						
					}

					public void failed(Throwable exc, Void attachment) {
						context.error("rpc error " + exc.getMessage());
					}
				});
				
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
