package com.netifera.platform.net.sunrpc.internal.service;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.netifera.platform.net.sunrpc.internal.sockets.RPCSocketEngineService;
import com.netifera.platform.net.sunrpc.sockets.IRPCSocketEngineService;

public class Activator implements BundleActivator {
	private ServiceTracker rpcSocketEngineTracker;

	private static Activator instance;
	
	public static Activator getInstance() {
		return instance;
	}
	
	public void start(BundleContext context) throws Exception {
		instance = this;
		
		rpcSocketEngineTracker = new ServiceTracker(context, IRPCSocketEngineService.class.getName(), null);
		rpcSocketEngineTracker.open();
	}
	
	public void stop(BundleContext arg0) throws Exception {
		
	}
	
	public RPCSocketEngineService getRPCSocketEngine() {
		IRPCSocketEngineService service = (IRPCSocketEngineService)rpcSocketEngineTracker.getService();
		if (service instanceof RPCSocketEngineService) {
			return (RPCSocketEngineService)service;
		}
		return null; 
	}
	
}
