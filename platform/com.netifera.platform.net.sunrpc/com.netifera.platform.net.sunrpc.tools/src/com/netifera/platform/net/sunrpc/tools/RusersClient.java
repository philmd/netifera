package com.netifera.platform.net.sunrpc.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.netifera.platform.api.probe.IProbe;
import com.netifera.platform.api.tools.IToolContext;
import com.netifera.platform.api.tools.ToolException;
import com.netifera.platform.net.sockets.CompletionHandler;
import com.netifera.platform.net.sunrpc.internal.tools.Activator;
import com.netifera.platform.net.sunrpc.packets.RpcReply;
import com.netifera.platform.net.sunrpc.packets.rusers.RusersAllNames2Reply;
import com.netifera.platform.net.sunrpc.packets.rusers.RusersCall;
import com.netifera.platform.net.sunrpc.packets.rusers.RusersUtmpEntry;
import com.netifera.platform.net.sunrpc.sockets.IRPCChannel;
import com.netifera.platform.net.sunrpc.sockets.RpcSocketLocator;
import com.netifera.platform.tools.RequiredOptionMissingException;
import com.netifera.platform.util.addresses.inet.InternetAddress;
import com.netifera.platform.util.patternmatching.HostnameMatcher;
import com.netifera.platform.util.patternmatching.InternetAddressMatcher;

public class RusersClient extends AbstractRpcTool {
	public final static int RUSERS_PROG = RusersCall.RUSERS_PROG;

	int timeout;
	
	public void toolRun(IToolContext context) throws ToolException {
		this.context = context;

		// XXX hardcode local probe as realm
		IProbe probe = Activator.getInstance().getProbeManager().getLocalProbe();
		realm = probe.getEntity().getId();
		
		setupToolOptions();

		context.setTitle("Listing users at " + address + "/" + port + "/" + protocol);
		context.setTotalWork(1);
		
		RpcSocketLocator locator = createLocator(port, RusersCall.RUSERS_PROG, 2);
		if (locator == null) {
			context.error("Unknown protocol: " + protocol);
		} else {
			ruserList(locator);
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
	
	private void ruserList(final RpcSocketLocator locator) {
		IRPCChannel channel;
		try {
			channel = Activator.getInstance().getRPCSocketEngine().openRPC();
			if (channel.connect(locator, timeout, TimeUnit.SECONDS)) {
				channel.asynchronousCall(pkt(RusersCall.AllNames2()), timeout, TimeUnit.SECONDS, null, new CompletionHandler<RpcReply, Void>() {

					public void cancelled(Void attachment) {
						context.error("rpc error (cancelled)");
					}

					public void completed(RpcReply result, Void attachment) {
						RusersAllNames2Reply reply = new RusersAllNames2Reply(pkt(result));
						SimpleDateFormat dateFormat = new SimpleDateFormat();
						for (RusersUtmpEntry utmpEntry : reply.getUtmpEntries()) {
							String host = utmpEntry.getHost();
							StringBuilder sb = new StringBuilder(64);
							sb.append("found user ");
							sb.append(utmpEntry.getUser());
							sb.append(" logged on ");
							sb.append(utmpEntry.getLine());
							if (host.length() > 0) {
								sb.append(" from ");
								sb.append(host);
							}
							sb.append(" since ");
							sb.append(dateFormat.format(new Date(utmpEntry.getTime()))); // FIXME date?
							context.info(sb.toString());
							Activator.getInstance().getNetworkEntityFactory().createUser(realm, context.getSpaceId(), locator.getAddress(), utmpEntry.getUser());
							if (InternetAddressMatcher.matches(host)) {
								// TODO add relation host -> server
								Activator.getInstance().getNetworkEntityFactory().createAddress(realm, context.getSpaceId(), InternetAddress.fromString(host));
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
