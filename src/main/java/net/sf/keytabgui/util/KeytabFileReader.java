package net.sf.keytabgui.util;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.sf.keytabgui.model.Keytab;
import net.sf.keytabgui.model.row.KeytabRow;
import net.sf.keytabgui.model.row.NewKeytabRow;
import net.sf.keytabgui.model.row.OldKeytabRow;

public class KeytabFileReader {
	
	public synchronized Keytab read(String filename) throws IOException {
		
		KeytabInputStream in = new KeytabInputStream(new FileInputStream(filename));
		
		Keytab keytab = new Keytab();
		keytab.setMajorVersion(in.read());
		keytab.setMinorVersion(in.read());
		
		
		List<KeytabRow> rows = keytab.getRows();
		KeytabRow row = null;
		while (in.available()>0){
			switch(keytab.getMinorVersion()){
			case 1:
				row = getOldRow(in);
				break;
			case 2:
				row = getNewRow(in);
				break;
			default:
				StringBuilder msg = new StringBuilder("Uknown keytab file version: ");
				msg.append(keytab.getMajorVersion());
				msg.append('.');
				msg.append(keytab.getMinorVersion());
				throw new IOException(msg.toString());
			}
			//System.out.println(row);
			rows.add(row);
		}
		
		return keytab;
	}

	private KeytabRow getNewRow(KeytabInputStream in) throws IOException {
		NewKeytabRow row = new NewKeytabRow();
		row.setSize(in.readInt());
		row.setNumComponents(in.readShort());
		row.setRealm(in.readCountedOctetString());
		
		//ip;.
		
		String[] components = new String[row.getNumComponents()];
		for (int i=0; i<row.getNumComponents(); i++){
			components[i] = in.readCountedOctetString();
		}
		row.setComponents(components);
		
		row.setNameType(in.readInt());
		row.setTimestamp(in.readInt());
		row.setVno8(in.read());
		row.setKeyblockType(in.readShort());
		row.setKeyblock(in.readCountedOctet());
		
		if (row.getSize() >= row.countSize()+4)
			row.setVno((int)in.readShort());
		
		return row;
	}

	private KeytabRow getOldRow(KeytabInputStream in) throws IOException {
		OldKeytabRow row = new OldKeytabRow();
		row.setSize(in.readInt());
		row.setNumComponents(in.readShort());
		row.setRealm(in.readCountedOctetString());
		
		String[] components = new String[row.getNumComponents()];
		for (int i=0; i<row.getNumComponents(); i++){
			components[i] = in.readCountedOctetString();
		}
		row.setComponents(components);
		
		//row.setNameType(in.readInt()); // not present if version 0x501
		row.setTimestamp(in.readInt());
		row.setVno8(in.read());
		row.setKeyblockType(in.readShort());
		row.setKeyblock(in.readCountedOctet());
		
		if (row.getSize() >= row.countSize()+4)
			row.setVno((int)in.readShort());
		
		return row;
	}
	
	
	

}

class KeytabInputStream extends DataInputStream {

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