package com.netifera.platform.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.netifera.platform.api.iterables.IndexedIterable;
import com.netifera.platform.api.iterables.SequentialIterator;

// FIXME copy of PortSet, PortSet should expand this class (use not restricted to tcp/udp ports).
public class IntegerSet implements IndexedIterable<Integer> {
	
	private static final long serialVersionUID = -4951196327354642059L;

	private final List<PortRange> ports;
	private final static int max = 0x7fffffff;

	public IntegerSet() throws IllegalArgumentException {
		ports = new ArrayList<PortRange>();
	}

	public IntegerSet(final String ports) throws IllegalArgumentException {
		this.ports = new ArrayList<PortRange>();
		fromString(ports);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null || !(obj instanceof IntegerSet)){
			return false;
		}
		IntegerSet portset = (IntegerSet)obj;
		if (itemCount() != portset.itemCount()) {
			return false;
		}
		if (ports.size() != portset.ports.size()) {
			return false;
		}

		//XXX
		return ports.equals(((IntegerSet)obj).ports);
	}
	
    @Override
	public int hashCode() {
        //FIXME
    	return ports.hashCode();
    }

	public void clear() {
		ports.clear();
	}
	
	public IntegerSet addPort(final int port) throws IllegalArgumentException {
		verifyPort(port);

		for(PortRange r : ports) {
			if(r.contains(port)) {
				return this;
			}
		}
		
		ports.add(new PortRange(port));
		simplify();
		return this;
	}
	
	public IntegerSet addIntegerSet(final IntegerSet portSet) throws IllegalArgumentException {
		return addIntegerSet(portSet.toString());
	}
	
	public IntegerSet addIntegerSet(final String portSet) throws IllegalArgumentException {
		fromString(portSet);
		simplify();
		return this;
	}

	public boolean contains(final int port) {
		verifyPort(port);
		for (PortRange range: ports) {
			if (range.contains(port)) {
				return true;
			}
		}
		return false;
	}

	private void fromString(final String string) throws IllegalArgumentException {
		if (string.length() == 0) {
			return;
		}
		final String[] parts = string.split(",");		
		for(String s : parts) {
			if (s.contains("-")) {
				String[] rangeParts = s.split("-");
				ports.add(new PortRange(Integer.parseInt(rangeParts[0]),Integer.parseInt(rangeParts[1])));
			} else {
				ports.add(new PortRange(Integer.parseInt(s)));
			}
		}
		
		simplify();
	}
	
	@Override
	public String toString() {
		return getLabel();
	}
	
	public String getLabel() {
		final StringBuffer buffer = new StringBuffer();
		for(PortRange r : ports) {
			if(buffer.length() > 0) {
				buffer.append(',');
			}
			buffer.append(r.toString());
		}
		
		return buffer.toString();
	}
	
	/**
	 * Sort the ranges of ports by start port and merge overlapping
	 * ranges.  This method should be called after every change which
	 * modifies the set of ports.
	 */
	private void simplify() {
		Collections.sort(ports);
		
		final Iterator<PortRange> iter = ports.iterator();
		PortRange last = null;
		while(iter.hasNext()) {
			final PortRange current = iter.next();
			
			if (last != null) {
				if (last.end + 1 >= current.start) {
					last.end = current.end;
					iter.remove();
					continue;
				}
			}
			last = current;
		}
	}

	public static int verifyPort(final int port) throws IllegalArgumentException {
		if (port < 0 || port > max) {
			throw new IllegalArgumentException("Invalid port: " + port);
		}
		return port;
	}

	public Integer itemAt(final int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}
		int idx = index;
		for (PortRange range: ports) {
			if (idx < range.itemCount()) {
				return range.itemAt(idx);
			}
			idx -= range.itemCount();
		}
		throw new IndexOutOfBoundsException();
	}

	public int itemCount() { // Cardinal
		int answer = 0;
		for (PortRange range: ports) {
			answer += range.itemCount();
		}
		return answer;
	}
	
	public Iterator<Integer> iterator() {
		return new SequentialIterator<Integer>(this);
	}
}