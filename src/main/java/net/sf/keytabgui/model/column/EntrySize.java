package net.sf.keytabgui.model.column;

import net.sf.keytabgui.model.Column;
import net.sf.keytabgui.model.row.KeytabRow;

public class EntrySize implements Column {

	public String getTitle() {
		return "entry size";
	}
	
	public Object getValue(KeytabRow row) {
		return row.getSize();
	}

}
