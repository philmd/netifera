package com.netifera.platform.net.sunrpc.model;

import java.util.ArrayList;
import java.util.List;

import com.netifera.platform.api.model.AbstractEntity;
import com.netifera.platform.api.model.IEntity;
import com.netifera.platform.api.model.IEntityReference;
import com.netifera.platform.api.model.IWorkspace;

public class MountdExportEntity extends AbstractEntity {
	private static final long serialVersionUID = -970915997075885550L;

	public static final String ENTITY_NAME = "rpc.mountd.export";
	
	private String path;
	private String groupList = null; // XXX
	private final IEntityReference mountdService;

	public MountdExportEntity(IWorkspace workspace, RpcServiceEntity mountdService, String path, List<String> groups) {
		super(ENTITY_NAME, workspace, mountdService.getRealmId());
		this.mountdService = mountdService.createReference();
		assert path != null;
		this.path = path;
		// TODO groups
	}

	private MountdExportEntity(IWorkspace workspace, long realmId, IEntityReference mountdServiceReference, String path, String groupList) {
		super(ENTITY_NAME, workspace, realmId);
		this.mountdService = mountdServiceReference.createClone();		
		this.path = path;
		// TODO groups
	}
	
	public RpcServiceEntity getRpcService() {
		return (RpcServiceEntity) referenceToEntity(mountdService);
	}
	
	public String getPath() {
		return path;
	}
	
	public List<String> getGroups() {
		return new ArrayList<String>(0); // TODO
	}
	
	public void addGroups(List<String> groups) {
		// TODO
	}
	
	@Override
	protected void synchronizeEntity(AbstractEntity masterEntity) {
		path = ((MountdExportEntity)masterEntity).path;
		groupList = ((MountdExportEntity)masterEntity).groupList;
	}
	
	@Override
	protected IEntity cloneEntity() {
		return new MountdExportEntity(getWorkspace(), getRealmId(), mountdService, path, groupList);
	}
	
	public static String createQueryKey(long realmId, long serviceId, String path) {
		return ENTITY_NAME + ":" + realmId + ":" + serviceId + ":" + path;
	}
	
	@Override
	protected String generateQueryKey() {
		return createQueryKey(getRealmId(), getRpcService().getId(), path);
	}
}
