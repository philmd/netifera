package com.netifera.platform.net.sunrpc.tools;

import java.util.concurrent.TimeUnit;

import com.netifera.platform.api.probe.IProbe;
import com.netifera.platform.api.tools.IToolContext;
import com.netifera.platform.api.tools.ToolException;
import com.netifera.platform.net.sockets.CompletionHandler;
import com.netifera.platform.net.sunrpc.internal.tools.Activator;
import com.netifera.platform.net.sunrpc.model.MountdExportEntity;
import com.netifera.platform.net.sunrpc.model.RpcServiceEntity;
import com.netifera.platform.net.sunrpc.packets.RpcCall;
import com.netifera.platform.net.sunrpc.packets.RpcReply;
import com.netifera.platform.net.sunrpc.packets.credentials.CredUnix;
import com.netifera.platform.net.sunrpc.packets.mount.MountCall1;
import com.netifera.platform.net.sunrpc.packets.mount.MountDumpEntry;
import com.netifera.platform.net.sunrpc.packets.mount.MountDumpReply;
import com.netifera.platform.net.sunrpc.packets.mount.MountExportEntry;
import com.netifera.platform.net.sunrpc.program.MountClient;
import com.netifera.platform.net.sunrpc.sockets.IRPCChannel;
import com.netifera.platform.net.sunrpc.sockets.RpcSocketLocator;
import com.netifera.platform.tools.RequiredOptionMissingException;
import com.netifera.platform.util.addresses.inet.InternetAddress;
import com.netifera.platform.util.patternmatching.HostnameMatcher;
import com.netifera.platform.util.patternmatching.InternetAddressMatcher;

public class Mountd extends AbstractRpcTool {
	public final static int MOUNT_PROG = MountCall1.MOUNT_PROG;

	int timeout;
	
	public void toolRun(IToolContext context) throws ToolException {
		this.context = context;

		// XXX hardcode local probe as realm
		IProbe probe = Activator.getInstance().getProbeManager().getLocalProbe();
		realm = probe.getEntity().getId();
		
		setupToolOptions();

		context.setTotalWork(1);
		
		RpcSocketLocator locator = createLocator(port, MountCall1.MOUNT_PROG, MountCall1.MOUNTVERS);
		if (locator == null) {
			context.error("Unknown protocol: " + protocol);
		} else {
			context.setTitle("Listing exported directories at " + address + "/" + port + "/" + protocol);
			export(locator);
			
			context.setTitle("Listing mounted directories at " + address + "/" + port + "/" + protocol);
			dump(locator);
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
	
	private void export(final RpcSocketLocator locator) {
		
		for (MountExportEntry exportEntry : new MountClient(locator, logger).getExportList(timeout, TimeUnit.SECONDS)) {
			context.info("found " + exportEntry);
			RpcServiceEntity service = Activator.getInstance().getRpcNetworkEntityFactory().createRpcService(realm, context.getSpaceId(), locator, MountCall1.MOUNT_PROG, MountCall1.MOUNTVERS, null); // XXX mount vers...
			Activator.getInstance().getRpcNetworkEntityFactory().createMountdExport(realm, context.getSpaceId(), service, exportEntry.getDirectory(), exportEntry.getGroups());
			for (String host : exportEntry.getGroups()) {
				if (InternetAddressMatcher.matches(host)) {
					// TODO add relation host -> server
					Activator.getInstance().getNetworkEntityFactory().createAddress(realm, context.getSpaceId(), InternetAddress.fromString(host));
				} else if (HostnameMatcher.matches(host)) {
					// TODO resolve/add hostname/ip in workspace?
				}
			}
		}
		
		context.worked(1);
	}

	private void dump(final RpcSocketLocator locator) {
		IRPCChannel channel;
		try {
			channel = Activator.getInstance().getRPCSocketEngine().openRPC();
			if (channel.connect(locator, timeout, TimeUnit.SECONDS)) {
				RpcCall call = MountCall1.Dump();
				call.setCredential(new CredUnix());
				channel.asynchronousCall(call, timeout, TimeUnit.SECONDS, null, new CompletionHandler<RpcReply, Void>() {

					public void cancelled(Void attachment) {
						context.error("rpc error (cancelled)");
					}

					public void completed(RpcReply result, Void attachment) {
						MountDumpReply reply = new MountDumpReply(pkt(result));
						for (MountDumpEntry dumpEntry : reply.getDumpList()) {
							context.info("found " + dumpEntry);
							RpcServiceEntity service = Activator.getInstance().getRpcNetworkEntityFactory().createRpcService(realm, context.getSpaceId(), locator, MountCall1.MOUNT_PROG, MountCall1.MOUNTVERS, null);
							MountdExportEntity export = Activator.getInstance().getRpcNetworkEntityFactory().createMountdExport(realm, context.getSpaceId(), service, dumpEntry.getDirectory(), null);
							String host = dumpEntry.getHostname();
							if (InternetAddressMatcher.matches(host)) {
								// TODO add relation host -> server
								Activator.getInstance().getRpcNetworkEntityFactory().createMountdDump(realm, context.getSpaceId(), export, InternetAddress.fromString(host));
							} else if (HostnameMatcher.matches(host)) {
								// TODO resolve/add hostname/ip in workspace?
							}
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
