package com.netifera.platform.net.sunrpc.internal.tools;

import com.netifera.platform.api.tools.ITool;
import com.netifera.platform.api.tools.IToolProvider;
import com.netifera.platform.net.sunrpc.tools.Mountd;
import com.netifera.platform.net.sunrpc.tools.PortMapper;
import com.netifera.platform.net.sunrpc.tools.RpcDiscovery;
import com.netifera.platform.net.sunrpc.tools.RusersClient;
import com.netifera.platform.net.sunrpc.tools.UgidClient;

public class ToolProvider implements IToolProvider {

	private final static String[] toolClassNames = { 
		PortMapper.class.getName(),
		RpcDiscovery.class.getName(),
		RusersClient.class.getName(),
		UgidClient.class.getName(),
		Mountd.class.getName(),
	};
	
	public ITool createToolInstance(String className) {
		if (className.equals(PortMapper.class.getName())) {
			return new PortMapper();
		} else if (className.equals(RpcDiscovery.class.getName())) {
			return new RpcDiscovery();
		} else if (className.equals(RusersClient.class.getName())) {
			return new RusersClient();
		} else if (className.equals(UgidClient.class.getName())) {
			return new UgidClient();
		} else if (className.equals(Mountd.class.getName())) {
			return new Mountd();
		}
		return null;
	}

	public String[] getProvidedToolClassNames() {
		return toolClassNames;
	}
}
