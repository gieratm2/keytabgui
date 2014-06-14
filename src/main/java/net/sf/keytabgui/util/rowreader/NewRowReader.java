package net.sf.keytabgui.util.rowreader;

import java.io.IOException;

import net.sf.keytabgui.model.row.KeytabRow;
import net.sf.keytabgui.model.row.NewKeytabRow;
import net.sf.keytabgui.util.KeytabInputStream;

public class NewRowReader implements RowReader {

	public KeytabRow readRow(KeytabInputStream in) throws IOException {
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
}
