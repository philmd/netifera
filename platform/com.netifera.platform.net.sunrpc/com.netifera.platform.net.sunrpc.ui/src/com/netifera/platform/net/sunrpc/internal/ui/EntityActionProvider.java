package com.netifera.platform.net.sunrpc.internal.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;

import com.netifera.platform.api.model.IShadowEntity;
import com.netifera.platform.net.model.ServiceEntity;
import com.netifera.platform.net.sunrpc.model.RpcServiceEntity;
import com.netifera.platform.net.sunrpc.tools.Mountd;
import com.netifera.platform.net.sunrpc.tools.PortMapper;
import com.netifera.platform.net.sunrpc.tools.RpcDiscovery;
import com.netifera.platform.net.sunrpc.tools.RusersClient;
import com.netifera.platform.net.sunrpc.tools.UgidClient;
import com.netifera.platform.tools.options.EnableOption;
import com.netifera.platform.tools.options.IntegerOption;
import com.netifera.platform.tools.options.StringOption;
import com.netifera.platform.ui.actions.ToolAction;
import com.netifera.platform.ui.api.actions.IEntityActionProvider;

// TODO rpc action via portmap
public class EntityActionProvider implements IEntityActionProvider {

	public List<IAction> getActions(IShadowEntity entity) {
		List<IAction> answer = new ArrayList<IAction>();
		
		if (entity instanceof ServiceEntity) {
			ServiceEntity service = (ServiceEntity)entity;
			
			ToolAction discovery = new ToolAction("Discover RPC services", RpcDiscovery.class.getName());
			discovery.addFixedOption(new StringOption(RpcDiscovery.TARGET_ADDRESS_KEY, "Target", "Target addresses", service.getAddress().getAddress().toString()));
			discovery.addFixedOption(new IntegerOption(PortMapper.TARGET_PORT_KEY, "Port", "Port to scan", Integer.valueOf(service.getPort())));
			discovery.addFixedOption(new StringOption(RpcDiscovery.TARGET_PROTOCOL_KEY, "Protocol", "Protocol to use", service.getProtocol()));
			answer.add(discovery);
			
			if (service.getPort() == 111) {
				ToolAction portmap = new ToolAction("List RPC services", PortMapper.class.getName());
				portmap.addFixedOption(new StringOption(PortMapper.TARGET_ADDRESS_KEY, "Target", "Target addresses", service.getAddress().getAddress().toString()));
				portmap.addFixedOption(new IntegerOption(PortMapper.TARGET_PORT_KEY, "Port", "Port to scan", Integer.valueOf(service.getPort())));
				portmap.addFixedOption(new StringOption(PortMapper.TARGET_PROTOCOL_KEY, "Protocol", "Protocol to use", service.getProtocol()));
				answer.add(portmap);
			}
			
		} else if (entity instanceof RpcServiceEntity) {
			RpcServiceEntity rpc = (RpcServiceEntity)entity;
			int program = rpc.getProgram();
			List<ServiceEntity> services = ((RpcServiceEntity) entity).getServices();
			for (ServiceEntity service : services) {
				String titleTail = "";
				if (services.size() > 1) {
					titleTail = " (via " + service.getProtocol() + ')';
				}
				if (program == RusersClient.RUSERS_PROG && rpc.hasVersion(2)) {
					ToolAction rusers = new ToolAction("List users" + titleTail, RusersClient.class.getName());
					rusers.addFixedOption(new StringOption(RusersClient.TARGET_ADDRESS_KEY, "Target", "Target addresses", service.getAddress().getAddress().toString()));
					rusers.addFixedOption(new IntegerOption(RusersClient.TARGET_PORT_KEY, "Port", "Port to scan", Integer.valueOf(service.getPort())));
					rusers.addFixedOption(new StringOption(RusersClient.TARGET_PROTOCOL_KEY, "Protocol", "Protocol to use", service.getProtocol()));
					rusers.addFixedOption(new EnableOption(RusersClient.USE_PORTMAPPER_KEY, "ViaPortmapper", "Connect via portmapper", false)); // true
					answer.add(rusers);
				} else if (program == UgidClient.UGID_PROG && rpc.hasVersion(1)) {
					ToolAction listUids = new ToolAction("List usernames" + titleTail, UgidClient.class.getName());
					listUids.addFixedOption(new StringOption(UgidClient.TARGET_ADDRESS_KEY, "Target", "Target addresses", service.getAddress().getAddress().toString()));
					listUids.addFixedOption(new IntegerOption(UgidClient.TARGET_PORT_KEY, "Port", "Port to scan", Integer.valueOf(service.getPort())));
					listUids.addFixedOption(new StringOption(UgidClient.TARGET_PROTOCOL_KEY, "Protocol", "Protocol to use", service.getProtocol()));
					listUids.addFixedOption(new EnableOption(UgidClient.USE_PORTMAPPER_KEY, "ViaPortmapper", "Connect via portmapper", false));
					answer.add(listUids);
				} else if (program == Mountd.MOUNT_PROG && rpc.hasVersion(1)) {
					// showmount -ea
					ToolAction mountDump = new ToolAction("List exported/mounted directories" + titleTail, Mountd.class.getName());
					mountDump.addFixedOption(new StringOption(Mountd.TARGET_ADDRESS_KEY, "Target", "Target addresses", service.getAddress().getAddress().toString()));
					mountDump.addFixedOption(new IntegerOption(Mountd.TARGET_PORT_KEY, "Port", "Port to scan", Integer.valueOf(service.getPort())));
					mountDump.addFixedOption(new StringOption(Mountd.TARGET_PROTOCOL_KEY, "Protocol", "Protocol to use", service.getProtocol()));
					mountDump.addFixedOption(new EnableOption(Mountd.USE_PORTMAPPER_KEY, "ViaPortmapper", "Connect via portmapper", false));
					answer.add(mountDump);
				}
			}
		}
		
		return answer;
	}

	public List<IAction> getQuickActions(IShadowEntity shadow) {
		// TODO Auto-generated method stub
		return null;
	}

}
