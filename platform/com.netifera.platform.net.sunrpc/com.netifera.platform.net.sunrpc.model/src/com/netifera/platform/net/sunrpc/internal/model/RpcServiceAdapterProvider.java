package com.netifera.platform.net.sunrpc.internal.model;

import java.util.HashMap;
import java.util.Map;

import com.netifera.platform.api.iterables.IndexedIterable;
import com.netifera.platform.api.model.IEntity;
import com.netifera.platform.api.model.IEntityAdapterProvider;
import com.netifera.platform.net.model.ClientServiceConnectionEntity;
import com.netifera.platform.net.model.ServiceEntity;
import com.netifera.platform.net.services.INetworkServiceProvider;
import com.netifera.platform.net.sunrpc.model.RpcServiceEntity;
import com.netifera.platform.net.sunrpc.sockets.RpcSocketLocator;
import com.netifera.platform.util.addresses.inet.InternetAddress;
import com.netifera.platform.util.locators.ISocketLocator;
import com.netifera.platform.util.locators.SSLSocketLocator;
import com.netifera.platform.util.locators.TCPSocketLocator;
import com.netifera.platform.util.locators.UDPSocketLocator;

public class RpcServiceAdapterProvider implements IEntityAdapterProvider {
	private Map<String,INetworkServiceProvider> providers = new HashMap<String,INetworkServiceProvider>();
	
	public Object getAdapter(IEntity entity, Class<?> adapterType) {
		/*
		if (adapterType.isAssignableFrom(RpcSocketLocator.class)) {
			if (entity instanceof RpcServiceEntity) {
				RpcServiceEntity rpcEntity = (RpcServiceEntity)entity;
				return RpcSocketLocator.createRPC(null, rpcEntity.getProgram(), rpcEntity.getVersion());
			}
		}
		
		ServiceEntity serviceEntity = null;
		if (entity instanceof ServiceEntity)
			serviceEntity = (ServiceEntity)entity;
		if (entity instanceof RpcServiceEntity)
			serviceEntity = ((RpcServiceEntity)entity).getServices().get(0); // XXX
		
		if (serviceEntity == null)
			return null;
		
		ISocketLocator locator = getSocketLocator(serviceEntity);
		if (locator == null) {
			return null;
		}
		if (adapterType.isAssignableFrom(locator.getClass()))
			return locator;

		String serviceType = serviceEntity.getServiceType();
		INetworkServiceProvider provider = providers.get(serviceType);
		if (provider != null && adapterType.isAssignableFrom(provider.getServiceClass())) {
			return provider.create(locator);
		}
		*/
		return null;
	}
	
	private ISocketLocator getSocketLocator(ServiceEntity serviceEntity) {
		InternetAddress address = (InternetAddress)serviceEntity.getAddress().getAdapter(InternetAddress.class);
		int port = serviceEntity.getPort();
		String protocol = serviceEntity.getProtocol();
		if (protocol.equals("tcp"))
			return new TCPSocketLocator(address,port);
		if (protocol.equals("udp"))
			return new UDPSocketLocator(address,port);
		if (protocol.equals("ssl"))
			return new SSLSocketLocator(address,port);
		return null; // or exception?
	}
	
	protected void registerProvider(INetworkServiceProvider provider) {
		providers.put(provider.getServiceName(), provider);
	}
	
	protected void unregisterProvider(INetworkServiceProvider provider) {
		providers.remove(provider.getServiceName());
	}

	// TODO RpcServiceEntity -> [ServiceEntity,...] ?
	public IndexedIterable<?> getIterableAdapter(IEntity entity, Class<?> iterableType) {
		// TODO Auto-generated method stub
		return null;
	}
}
