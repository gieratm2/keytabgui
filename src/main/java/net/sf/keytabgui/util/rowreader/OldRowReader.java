package net.sf.keytabgui.util.rowreader;

import java.io.IOException;

import net.sf.keytabgui.model.row.KeytabRow;
import net.sf.keytabgui.model.row.OldKeytabRow;
import net.sf.keytabgui.util.KeytabInputStream;

public class OldRowReader implements RowReader {

	public KeytabRow readRow(KeytabInputStream in) throws IOException {
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
