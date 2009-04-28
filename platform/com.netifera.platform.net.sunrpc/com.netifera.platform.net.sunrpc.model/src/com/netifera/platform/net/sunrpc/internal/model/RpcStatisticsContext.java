package com.netifera.platform.net.sunrpc.internal.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netifera.platform.api.model.AbstractEntity;
import com.netifera.platform.api.model.IEntity;
import com.netifera.platform.api.model.IModelService;
import com.netifera.platform.api.model.IWorkspace;

class RpcStatisticsContext extends AbstractEntity {
	private static final long serialVersionUID = -6726984041535827070L;
	
	public static final String ENTITY_NAME = "rpc.statistics";
	
	private final Map<Integer, Integer> programStats = new HashMap<Integer, Integer>();
	private final Map<Integer, Integer> versionStats = new HashMap<Integer, Integer>();

	private RpcStatisticsContext(IWorkspace workspace, long realmId) {
		super(ENTITY_NAME, workspace, realmId);
	}
	
	void dump() {
		if (!programStats.isEmpty()) {
			System.out.println("[STATS] rpc programs: " + programStats);
		}
		if (!versionStats.isEmpty()) {
			System.out.println("[STATS] rpc versions: " + versionStats);
		}
	}

	void add(int programNumber, int programVersion) {
		if (!programStats.containsKey(programNumber)) {
			programStats.put(programNumber, 1);
		} else {
			programStats.put(programNumber, programStats.get(programNumber) + 1); // FIXME
		}
		if (!versionStats.containsKey(programVersion)) {
			versionStats.put(programVersion, 1);
		} else {
			versionStats.put(programVersion, versionStats.get(programVersion) + 1); // FIXME
		}
	}

	@Override
	protected IEntity cloneEntity() {
		RpcStatisticsContext clone = new RpcStatisticsContext(getWorkspace(), getRealmId());
		
		for (Integer program : programStats.keySet()) {
			clone.programStats.put(program, programStats.get(program));
		}
		for (Integer program : versionStats.keySet()) {
			clone.versionStats.put(program, versionStats.get(program));
		}
		
		return clone;
	}
	
	static RpcStatisticsContext open(IModelService model) {
		List<RpcStatisticsContext> list = model.getCurrentWorkspace().findAll(RpcStatisticsContext.class);
		//if (list.isEmpty()) {
			return new RpcStatisticsContext(model.getCurrentWorkspace(), 0); // FIXME '0'
		//}
		// XXX
		//return list.get(0);
	}
}
