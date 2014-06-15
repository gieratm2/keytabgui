package net.sf.keytabgui.model.column;

import net.sf.keytabgui.model.Column;
import net.sf.keytabgui.model.row.KeytabRow;

public class Kvno implements Column {

	public String getTitle() {
		return "kvno";
	}
	
	public Object getValue(KeytabRow row) {
		return row.getVno()==null?row.getVno8():row.getVno();
	}

}
