package com.netifera.platform.net.sunrpc.internal.ui;

import java.util.List;

import com.netifera.platform.api.model.IEntity;
import com.netifera.platform.api.model.layers.ITreeLayerProvider;
import com.netifera.platform.net.model.ServiceEntity;
import com.netifera.platform.net.sunrpc.model.MountdExportEntity;
import com.netifera.platform.net.sunrpc.model.RpcServiceEntity;

public class TreeLayerProvider implements ITreeLayerProvider {
	
	public IEntity[] getParents(IEntity entity) {
		if(entity instanceof MountdExportEntity) {
			return new IEntity[] {((MountdExportEntity) entity).getRpcService()};
		} else if (entity instanceof RpcServiceEntity) {
			List<ServiceEntity> services = ((RpcServiceEntity) entity).getServices();
			IEntity[] parents = new IEntity[services.size() + 1];
			services.toArray(parents);
			parents[services.size()] = services.get(0).getAddress().getHost();
			return parents;
		}
		return new IEntity[0];
	}

	public boolean isRealmRoot(IEntity entity) {
		return false;
	}

	public String getLayerName() {
		return "RPC";
	}

	public boolean isDefaultEnabled() {
		return true;
	}
}
