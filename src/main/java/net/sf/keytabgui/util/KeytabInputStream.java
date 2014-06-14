package net.sf.keytabgui.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class KeytabInputStream extends DataInputStream {

	public KeytabInputStream(InputStream in) {
		super(in);
	}
	
	public byte[] readCountedOctet() throws IOException {
		byte[] b = new byte[this.readShort()];
		this.read(b);
		return b;
	}
	
	public String readCountedOctetString() throws IOException {
		return new String(readCountedOctet());
	}
}
