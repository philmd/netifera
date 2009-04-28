package com.netifera.platform.net.sunrpc.packets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

// TODO verify rfc 1014, 1832, 4506 Srinivasan-xdr

public class XdrBuffer {

	private ByteBuffer buffer;
	
	public XdrBuffer() {
		this(8192);
	}
	
	public XdrBuffer(int size) {
		buffer = ByteBuffer.allocate(size);
	}
	
	public XdrBuffer(ByteBuffer buffer) {
		this.buffer = buffer.duplicate();
	}
	
	public XdrBuffer duplicate() {
		return new XdrBuffer(buffer);
	}
	
	public ByteBuffer rawBuffer() {
		return buffer;
	}

    public int position() {
    	return buffer.position();
    }

    public XdrBuffer position(int newPosition) {
    	buffer.position(newPosition);
    	return this;
    }
    
	public boolean xdr_bool() {
		return buffer.getInt() != 0;
	}
	
    public void xdr_bool(final boolean value) {
    	xdr_int(value ? 1 : 0);
    }
    
	public int xdr_int() {
		return buffer.getInt();
	}
	
	public void xdr_int(int value) {
		buffer.putInt(value);
	}
	
	public long xdr_uint() {
		return buffer.getInt() & 0xFFFFFFFFL;
	}
	
	//public void xdr_uint(long value) {
	//	buffer.putInt(value & 0xffffffff);
	//}
	
	public long xdr_long() {
		return buffer.getLong();
	}
	
	public void xdr_long(long value) {
		buffer.putLong(value);
	}
	
	public String xdr_string() {
		byte[] data = xdr_opaque(xdr_int());
		if (data.length == 0 || data[0] == 0) {
			return "";
		}
		try {
			return new String(data, "US-ASCII").split("\0")[0];
		} catch (UnsupportedEncodingException e) {
			// XXX
			e.printStackTrace();
			return "";
		}
	}
	
	public void xdr_string(String value) {
		byte[] bytes;
		try {
			bytes = value.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e1) {
			// XXX
			e1.printStackTrace();
			bytes = new byte[0];
		}
		xdr_opaque(bytes);
	}
	
    public void xdr_bytes(byte[] src) {
    	xdr_bytes(src, 0, src.length);
    }
    
    public void xdr_bytes(byte[] src, int offset, int length) {
    	xdr_int(length);
    	buffer.put(src, offset, length);
    }
    
    public void xdr_opaque(byte[] src) {
    	xdr_bytes(src, 0, src.length);
    	int pad = src.length % 4;
    	if (pad > 0) {
    		buffer.position(buffer.position() + 4 - pad);
    	}
    }
    
    public byte[] xdr_opaque(int length) {
    	byte[] data = xdr_raw(length);
    	length %= 4;
    	if (length != 0) {
    		buffer.position(buffer.position() + 4 - length);
    	}
    	return data;
    }

    public byte[] xdr_bytes() {
    	return (xdr_raw(xdr_int()));
    }

    public byte[] xdr_raw(int length) {
    	if (length == 0)
    		return new byte[0];;

    	byte[] data = new byte[length];

    	buffer.get(data, 0, length);
    	
    	return data;
    }

    public void xdr_raw(byte[] src) {
    	buffer.put(src, 0, src.length);
    }
    
    public void xdr_raw(XdrBuffer xdr) {
    	xdr.buffer.flip(); // FIXME
    	buffer.put(xdr.buffer);
    }

    @Deprecated
	public XdrBuffer xdr_xdr() {
    	int length = xdr_int();
    	XdrBuffer buf = new XdrBuffer(length);
    	buf.buffer.put(xdr_raw(length), 0, length);
    	buf.flip();
    	return buf;
	}

    public void xdr_xdr(XdrBuffer xdr) {
    	xdr.buffer.flip();
    	xdr_int(xdr.buffer.limit());
    	buffer.put(xdr.buffer);
    }

    public int length() {
    	return buffer.limit(); // FIXME
    }
    
    public XdrBuffer clear() {
    	buffer.clear();
    	return this;
    }
    
    public XdrBuffer flip() {
    	buffer.flip();
    	return this;
    }
    
    public XdrBuffer mark() {
    	buffer.mark();
    	return this;
    }
    
    public XdrBuffer reset() {
    	buffer.reset();
    	return this;
    }
    
    public XdrBuffer rewind() {
    	buffer.rewind();
    	return this;
    }
    
    @Override
    public String toString() {
    	return "XDR " + buffer.toString();
    }
}
