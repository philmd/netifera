package com.netifera.platform.net.sunrpc.internal.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.netifera.platform.util.IntegerSet;

// TODO rename
public class RpcProgramList {
	private final Map<Integer, IntegerSet> map;
	
	public RpcProgramList(final String line) {
		map = new HashMap<Integer, IntegerSet>();
		
		if (line != null) {
			for (String rpcProgram : line.split(";")) {
				String[] programValues = rpcProgram.split(":");
				map.put(Integer.parseInt(programValues[0]), new IntegerSet(programValues[1]));
			}
		}
	}
	
	public Set<Integer> getPrograms() {
		return map.keySet();
	}
	
	public boolean containsProgram(Integer program) {
		return map.containsKey(program);
	}
	
	public IntegerSet getVersions(Integer program) {
		return map.containsKey(program) ? map.get(program) : null;
	}
	
	public void add(Integer rpcProgramNumber, String rpcVersion) {
		if (map.containsKey(rpcProgramNumber)) {
			map.get(rpcProgramNumber).addIntegerSet(rpcVersion);
		} else {
			map.put(rpcProgramNumber, new IntegerSet(rpcVersion));
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Map.Entry<Integer, IntegerSet>> i = map.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry<Integer, IntegerSet> entry = i.next();
			sb.append(entry.getKey());
			sb.append(":");
			sb.append(entry.getValue().getLabel());
			if (i.hasNext()) {
				sb.append(";");
			}
		}
		return sb.toString();
	}
}
