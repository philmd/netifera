package com.netifera.platform.net.sunrpc.program;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.netifera.platform.api.log.ILogger;
import com.netifera.platform.net.sockets.CompletionHandler;
import com.netifera.platform.net.sunrpc.packets.RpcReply;
import com.netifera.platform.net.sunrpc.packets.mount.AbstractMountCall;
import com.netifera.platform.net.sunrpc.packets.mount.MountCall1;
import com.netifera.platform.net.sunrpc.packets.mount.MountExportEntry;
import com.netifera.platform.net.sunrpc.packets.mount.MountExportReply;
import com.netifera.platform.net.sunrpc.sockets.IRPCChannel;
import com.netifera.platform.net.sunrpc.sockets.RpcSocketLocator;

public class MountClient extends AbstractRpcClient {

	public MountClient(RpcSocketLocator locator, ILogger logger) {
		super(locator, AbstractMountCall.MOUNT_PROG, logger);
	}
	
	// TODO decrement timeout
	public List<MountExportEntry> getExportList(long timeout, TimeUnit unit) {
		if (locator.getRpcVersion() != MountCall1.MOUNTVERS) {
			// only v1 supported for now
			return Collections.emptyList();
		}
		List<MountExportEntry> list = new LinkedList<MountExportEntry>();
		try {
			IRPCChannel channel = openChannel();
			if (channel.connect(locator, timeout, TimeUnit.SECONDS)) {
				channel.asynchronousCall(MountCall1.Export(), timeout, TimeUnit.SECONDS, list, new CompletionHandler<RpcReply, List<MountExportEntry>>() {

					public void cancelled(List<MountExportEntry> attachment) {
						logger.error("MOUNTDv1 EXPORT");
					}

					public void completed(RpcReply result, List<MountExportEntry> attachment) {
						attachment.addAll(new MountExportReply(result).getExportList());
						logger.info(attachment.toString()); // XXX
					}

					public void failed(Throwable exc, List<MountExportEntry> attachment) {
						logger.error("MOUNTDv1 EXPORT", exc);
					}
				}).get(timeout, unit);
				
				//channel.close();
			}
		} catch (IOException e) {
			logger.error("I/O Error", e);
		} catch (InterruptedException e) {
			logger.error("Interrupted", e);
		} catch (ExecutionException e) {
			logger.error("Execution Error", e);
		} catch (TimeoutException e) {
			logger.error("Timeout", e);
		}
		return list;
	}
}
