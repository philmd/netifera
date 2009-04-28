package com.netifera.platform.net.sunrpc.internal.ui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.swt.graphics.Image;

import com.netifera.platform.api.model.IShadowEntity;
import com.netifera.platform.net.model.ServiceEntity;
import com.netifera.platform.net.sunrpc.model.MountdExportEntity;
import com.netifera.platform.net.sunrpc.model.RpcServiceEntity;
import com.netifera.platform.ui.api.model.IEntityInformationProvider;
import com.netifera.platform.ui.api.model.IEntityLabelProvider;
import com.netifera.platform.ui.images.ImageCache;
import com.netifera.platform.util.PortSet;

public class EntityLabelProvider implements IEntityLabelProvider, IEntityInformationProvider {
	private final static String PLUGIN_ID = "com.netifera.platform.net.sunrpc.ui";

	private ImageCache images = new ImageCache(PLUGIN_ID);

	private final static String RPC_SERVICE = "icons/mountd_export.png";
	private final static String MOUNTD_EXPORT = "icons/mountd_export.png";

	public String getText(IShadowEntity e) {
		if (e instanceof MountdExportEntity) {
			return ((MountdExportEntity) e).getPath();
		} else if (e instanceof RpcServiceEntity) {
			RpcServiceEntity rpc = (RpcServiceEntity)e;
			String name = rpc.getProgramName();
			if (!name.startsWith("rpc.")) {
				name = "rpc." + name;
			}
			return name;// + " v" + rpc.getVersions().getLabel();
		}
		return null;
	}
	
	public String getFullText(IShadowEntity e) {
		/*
		if (e instanceof RpcServiceEntity) {
			RpcServiceEntity rpc = (RpcServiceEntity)e;
			String programName = rpc.getProgramName();
			int programNumber = rpc.getProgram();
			String versions = rpc.getVersions().getLabel();
			if (Integer.toString(programNumber).equals(programName)) {
				return "rpc#" + programName + " v" + versions;
			} else {
				return programName + '/' + programNumber + " v" + versions;
			}
		}
		*/
		return getText(e);
	}

	public Image getImage(IShadowEntity e) {
		if (e instanceof MountdExportEntity) {
			return images.get(MOUNTD_EXPORT);
		} else if (e instanceof RpcServiceEntity) {
			return images.get(RPC_SERVICE);
		}
		return null;
	}

	public Image decorateImage(Image image, IShadowEntity e) {
		return null;
	}

	public String getInformation(IShadowEntity e) {
		if (e instanceof MountdExportEntity) {
			return getNFSExportInformation((MountdExportEntity)e);
		} else if (e instanceof RpcServiceEntity) {
			return getRpcServicetInformation((RpcServiceEntity)e);
		}
		return null;
	}

	private String getRpcServicetInformation(RpcServiceEntity rpc) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<p>rpc program: ");
		sb.append(rpc.getProgram());
		sb.append("</p><p>rpc version");
		PortSet versions = rpc.getVersions();
		if  (versions.itemCount() > 1) {
			sb.append('s');
		}
		sb.append(": ");
		sb.append(versions.getLabel());
		sb.append("</p><p>protocol");
		Set<String> transports = new HashSet<String>();
		for (ServiceEntity service : rpc.getServices()) {
			transports.add(service.getProtocol());
		}
		if (transports.size() > 1) {
			sb.append('s');
		}
		sb.append(": ");
		Iterator<String> transportIterator = transports.iterator();
		while (transportIterator.hasNext()) {
			sb.append(transportIterator.next());
			if (transportIterator.hasNext())
				sb.append(", ");
		}
		sb.append("</p>");
		
		return sb.toString();
	}
	
	private String getNFSExportInformation(MountdExportEntity e) {
		StringBuffer buffer = new StringBuffer();
		
		Iterator<String> groups = e.getGroups().iterator();
		if (groups.hasNext()) {
			buffer.append("<p>Groups: ");
			while (groups.hasNext()) {
				buffer.append(escape(groups.next()));
				if (groups.hasNext())
					buffer.append(", ");
			}
			buffer.append("</p>");
		}
		
		return buffer.toString();
	}
	
	private String escape(String data) {
		data = data.replaceAll("&", "&amp;");
		data = data.replaceAll("<", "&lt;");
		data = data.replaceAll(">", "&gt;");
		data = data.trim().replaceAll("[\\r\\n]+", "</p><p>");
		return data.replaceAll("[^\\p{Print}\\p{Blank}]", "."); // non-printable chars
	}

	public void dispose() {
		images.dispose();
	}

	public Integer getSortingCategory(IShadowEntity e) {
		return null;
	}

	public Integer compare(IShadowEntity e1, IShadowEntity e2) {
		return null;
	}
}
