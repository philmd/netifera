package com.netifera.platform.net.sunrpc.model;

import com.netifera.platform.api.model.AbstractEntity;
import com.netifera.platform.api.model.IEntity;
import com.netifera.platform.api.model.IEntityReference;
import com.netifera.platform.api.model.IWorkspace;
import com.netifera.platform.net.model.InternetAddressEntity;

public class MountdDumpEntity extends AbstractEntity {
	private static final long serialVersionUID = -4031302771727435932L;

	public static final String ENTITY_NAME = "rpc.mountd.dump";
	
	private final IEntityReference address;
	/* Store the address as a string for faster queries */
	final private String addressString;
	private final IEntityReference export;
	
	public MountdDumpEntity(IWorkspace workspace, MountdExportEntity mountedExport, InternetAddressEntity address) {
		super(ENTITY_NAME, workspace, mountedExport.getRealmId());
		this.address = address.createReference();
		this.addressString = getAddressEntity().getAddressString();
		this.export = mountedExport.createReference();
	}
	
	private MountdDumpEntity(IWorkspace workspace, long realmId, IEntityReference mountedExportReference, IEntityReference addressReference) {
		super(ENTITY_NAME, workspace, realmId);
		this.address = addressReference.createClone();
		this.addressString = getAddressEntity().getAddressString();
		this.export = mountedExportReference.createClone();
	}
	
	public MountdExportEntity getExportEntity() {
		return (MountdExportEntity) referenceToEntity(export);
	}
	
	public InternetAddressEntity getAddressEntity() {
		return (InternetAddressEntity) referenceToEntity(address);
	}
	
	public String getAddressString() {
		return addressString;
	}
	
	@Override
	protected IEntity cloneEntity() {
		return new MountdDumpEntity(getWorkspace(), getRealmId(), export, address);
	}
	
	public static String createQueryKey(long realmId, String address, String path) {
		return ENTITY_NAME + ":" + realmId + ":" + address + ":" + path;
	}
	
	@Override
	protected String generateQueryKey() {
		return createQueryKey(getRealmId(), addressString, getExportEntity().getPath());
	}
}
