package com.netifera.platform.net.sunrpc.internal.tools;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.netifera.platform.api.model.IModelService;
import com.netifera.platform.api.probe.IProbeManagerService;
import com.netifera.platform.net.model.INetworkEntityFactory;
import com.netifera.platform.net.sunrpc.sockets.IRPCSocketEngineService;
import com.netifera.platform.net.sunrpc.model.IRpcEntityFactory;

public class Activator implements BundleActivator {

	private ServiceTracker modelTracker;
	private ServiceTracker probeManagerTracker;
	private ServiceTracker networkEntityFactoryTracker;
	private ServiceTracker rpcSocketEngineTracker;
	private ServiceTracker rpcNetworkEntityFactoryTracker;
	
	private static Activator instance;
	
	public static Activator getInstance() {
		return instance;
	}
	
	public void start(BundleContext context) throws Exception {
		instance = this;
		modelTracker = new ServiceTracker(context, IModelService.class.getName(), null);
		modelTracker.open();
		
		probeManagerTracker = new ServiceTracker(context, IProbeManagerService.class.getName(), null);
		probeManagerTracker.open();
		
		networkEntityFactoryTracker = new ServiceTracker(context, INetworkEntityFactory.class.getName(), null);
		networkEntityFactoryTracker.open();

		rpcSocketEngineTracker = new ServiceTracker(context, IRPCSocketEngineService.class.getName(), null);
		rpcSocketEngineTracker.open();
		
		rpcNetworkEntityFactoryTracker = new ServiceTracker(context, IRpcEntityFactory.class.getName(), null);
		rpcNetworkEntityFactoryTracker.open();
	}

	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
	}

	public IModelService getModelService() {
		return (IModelService) modelTracker.getService();
	}
	
	public IProbeManagerService getProbeManager() {
		return (IProbeManagerService) probeManagerTracker.getService();
	}
	
	public IRPCSocketEngineService getRPCSocketEngine() {
		return (IRPCSocketEngineService) rpcSocketEngineTracker.getService();
	}
	
	public INetworkEntityFactory getNetworkEntityFactory() {
		return (INetworkEntityFactory) networkEntityFactoryTracker.getService();
	}
	
	public IRpcEntityFactory getRpcNetworkEntityFactory() {
		return (IRpcEntityFactory) rpcNetworkEntityFactoryTracker.getService();
	}
}
