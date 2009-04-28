package com.netifera.platform.net.sunrpc.model;

import java.util.ArrayList;
import java.util.List;

import com.netifera.platform.api.model.AbstractEntity;
import com.netifera.platform.api.model.IEntity;
import com.netifera.platform.api.model.IEntityReference;
import com.netifera.platform.api.model.IWorkspace;
import com.netifera.platform.net.model.InternetAddressEntity;
import com.netifera.platform.net.model.ServiceEntity;
import com.netifera.platform.net.sunrpc.RpcServiceEntryTable;
import com.netifera.platform.util.HexaEncoding;
import com.netifera.platform.util.PortSet;
import com.netifera.platform.util.addresses.inet.InternetAddress;

public class RpcServiceEntity extends AbstractEntity {
	
	private static final long serialVersionUID = 1369976797041399335L;

	public final static String ENTITY_NAME = "rpc.service";
	
	private final int programNumber;
	private String programVersions;
	private final IEntityReference address;
	private final transient PortSet programVersion = new PortSet();
	private List<IEntityReference> services = new ArrayList<IEntityReference>(2);

	public RpcServiceEntity(IWorkspace workspace, InternetAddressEntity address, int programNumber) {
		super(ENTITY_NAME, workspace, address.getRealmId());
		this.programNumber = programNumber;
		this.address = address.createReference();
	}

	public void addService(ServiceEntity service) {
		for (IEntityReference ref: services) {
			ServiceEntity entity = (ServiceEntity) referenceToEntity(ref);
			if (service.getPort() == entity.getPort() && service.getProtocol().equals(entity.getProtocol())) {
				return;
			}
		}
		services.add(service.createReference());
	}
	
	public void addVersion(int version) {
		programVersion.addPort(version);
		programVersions = programVersion.getLabel();
	}
	
	public PortSet getVersions() {
		return programVersion;
	}
	
	public boolean hasVersion(int version) {
		return programVersion.contains(version);
	}
	
	public List<ServiceEntity> getServices() {
		List<ServiceEntity> answer = new ArrayList<ServiceEntity>(services.size());
		for (IEntityReference ref: services)
			answer.add((ServiceEntity) referenceToEntity(ref));
		return answer;
	}
	
	public int getProgram() {
		return programNumber;
	}
	
	public String getProgramName() {
		return RpcServiceEntryTable.getName(programNumber);
	}
	
	public InternetAddressEntity getAddress() {
		return (InternetAddressEntity) referenceToEntity(address);
	}
	
	private RpcServiceEntity(IWorkspace workspace, long realm, IEntityReference addressReference, int programNumber, String programVersions) {
		super(ENTITY_NAME, workspace, realm);
		this.programNumber = programNumber;
		this.address = addressReference.createClone();
		this.programVersions = programVersions;
		programVersion.addPortSet(programVersions);
	}
	
	public static String createQueryKey(long realmId, InternetAddress address, int programNumber) {
		return ENTITY_NAME + ":" + realmId + ":" + HexaEncoding.bytes2hex(address.toBytes()) + ":" + programNumber;
	}
	
	@Override
	protected String generateQueryKey() {
		return createQueryKey(getRealmId(), getAddress().getAddress(), programNumber);
	}
	
	protected IEntity cloneEntity() {
		RpcServiceEntity clone = new RpcServiceEntity(getWorkspace(),
				getRealmId(), address, programNumber, programVersions); 
		for (IEntityReference ref: services)
			clone.services.add(ref.createClone());
		return clone;
	}
}
