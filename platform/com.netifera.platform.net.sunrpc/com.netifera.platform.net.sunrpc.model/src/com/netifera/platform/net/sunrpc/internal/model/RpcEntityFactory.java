package com.netifera.platform.net.sunrpc.internal.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.ComponentContext;

import com.netifera.platform.api.model.IModelService;
import com.netifera.platform.api.model.IWorkspace;
import com.netifera.platform.net.model.InternetAddressEntity;
import com.netifera.platform.net.model.ServiceEntity;
import com.netifera.platform.net.sunrpc.RpcServiceEntryTable;
import com.netifera.platform.net.sunrpc.model.IRpcEntityFactory;
import com.netifera.platform.net.sunrpc.model.MountdDumpEntity;
import com.netifera.platform.net.sunrpc.model.MountdExportEntity;
import com.netifera.platform.net.sunrpc.model.RpcServiceEntity;
import com.netifera.platform.util.addresses.inet.InternetAddress;
import com.netifera.platform.util.locators.ISocketLocator;
import com.netifera.platform.net.model.INetworkEntityFactory;

public class RpcEntityFactory implements IRpcEntityFactory {

	private static final boolean COLLECT_STATISTICS = false; // TODO user preference
	
	private IModelService model;
	private INetworkEntityFactory networkEntityFactory;
	
	private RpcStatisticsContext statsContext;
	
	protected void setModelService(final IModelService model) {
		this.model = model;
	}
	
	protected void unsetModelService(final IModelService model) {
		this.model = null;
	}
	
	protected void setNetworkEntityFactory(final INetworkEntityFactory factory) {
		networkEntityFactory = factory;
	}
		
	protected void unsetNetworkEntityFactory(final INetworkEntityFactory factory) {
		networkEntityFactory = null;
	}
	
	protected void activate(ComponentContext context) {
		if (COLLECT_STATISTICS) {
			statsContext = RpcStatisticsContext.open(model);
			statsContext.dump();
		}
	}
	
	protected void deactivate(ComponentContext context) {
		if (COLLECT_STATISTICS) {
			statsContext.save();
			statsContext.dump();
			statsContext = null;
		}
	}

	private IWorkspace getWorkspace() {
		if(model.getCurrentWorkspace() == null) {
			throw new IllegalStateException("Cannot create RPC entities because no workspace is currently open");
		}
		return model.getCurrentWorkspace();
	}
	
	public synchronized RpcServiceEntity createRpcService(long realm, long spaceId,
			ISocketLocator locator, int programNumber, int programVersion,
			Map<String, String> info) {
		
		// - ServiceEntity
		
		String serviceType;
		boolean isRPC = true;
		
		/* some services are registered to the portmapper but does not strictly
		 * communicate with the RPC protocol (so, over a RPC channel) */
		switch (programNumber) {
		case 391002:
			serviceType = "FAM";
			isRPC = false;
			break;
		default:
			serviceType = "RPC";
		}
		
		ServiceEntity service = networkEntityFactory.createService(realm, spaceId, locator, serviceType, info);
		
		if ("RPC".equals(serviceType)) {
		String product = RpcServiceEntryTable.getName(programNumber);
			// TODO multiple rpc on same service
			service.setProduct(product.toString());
		}
		service.update();
		service.addToSpace(spaceId);
		
		if (!isRPC) {
			return null; // FIXME
		}
		
		// - RpcServiceEntity
		
		RpcServiceEntity rpc = (RpcServiceEntity) getWorkspace().findByKey(RpcServiceEntity.createQueryKey(realm, locator.getAddress(), programNumber));
		if(rpc != null) {
			rpc.addVersion(programVersion);
			rpc.addService(service);
			rpc.save();
			rpc.addToSpace(spaceId);
			return rpc;
		}
		
		rpc = new RpcServiceEntity(getWorkspace(), service.getAddress(), programNumber);
		rpc.addVersion(programVersion);
		rpc.addService(service);
		
		rpc.save();
		rpc.addToSpace(spaceId);
		
		if (COLLECT_STATISTICS) {
			statsContext.add(programNumber, programVersion);
		}

		return rpc;
	}
	
	public synchronized MountdExportEntity createMountdExport(long realm, long spaceId, RpcServiceEntity serviceEntity, String path, List<String> groups) {
		long serviceId = serviceEntity.getId();
		
		MountdExportEntity export = (MountdExportEntity) getWorkspace().findByKey(MountdExportEntity.createQueryKey(realm, serviceId, path));
		if(export != null) {
			export.addToSpace(spaceId);
			return export;
		}
		
		export = new MountdExportEntity(getWorkspace(), serviceEntity, path, groups);
		export.save();
		export.addToSpace(spaceId);
		
		return export;
	}

	public synchronized MountdDumpEntity createMountdDump(long realm, long spaceId, MountdExportEntity exportEntity, InternetAddress address) {
		
		// first add the client (in this space)
		ServiceEntity mountd = exportEntity.getRpcService().getServices().get(0); // XXX get(0)
		Map<String, String> info = new HashMap<String, String>();
		info.put("mountd.port", Integer.toString(mountd.getPort()));
		info.put("mountd.protocol", mountd.getProtocol());
		networkEntityFactory.createClient(realm, spaceId, address, "NFS", info, mountd.getLocator()); // XXX mountd can be non related network (localhost, another interface...)
		
		// then add the dump entry
		MountdDumpEntity dump = (MountdDumpEntity) getWorkspace().findByKey(MountdDumpEntity.createQueryKey(realm, address.toString(), exportEntity.getPath()));
		if(dump != null) {
			dump.addToSpace(spaceId);
			return dump;
		}
		
		InternetAddressEntity addressEntity = networkEntityFactory.createAddress(realm, spaceId, address);
		addressEntity.save();
		
		dump = new MountdDumpEntity(getWorkspace(), exportEntity, addressEntity);
		dump.save();
		dump.addToSpace(spaceId);
		
		return dump;
	}
}
