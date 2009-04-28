package com.netifera.platform.net.sunrpc.model;

import java.util.List;
import java.util.Map;

import com.netifera.platform.util.addresses.inet.InternetAddress;
import com.netifera.platform.util.locators.ISocketLocator;

public interface IRpcEntityFactory {
	String RPCPROGRAMLIST_KEY = "rpc.programs";
	
	RpcServiceEntity createRpcService(long realm, long spaceId, ISocketLocator locator, int programNumber, int programVersion, Map<String, String> info);
	
	MountdExportEntity createMountdExport(long realm, long spaceId, RpcServiceEntity mountd, String path, List<String> groups);
	
	MountdDumpEntity createMountdDump(long realm, long spaceId, MountdExportEntity export, InternetAddress address);
}
