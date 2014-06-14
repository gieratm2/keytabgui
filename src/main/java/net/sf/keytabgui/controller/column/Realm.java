package net.sf.keytabgui.controller.column;

import net.sf.keytabgui.controller.Column;
import net.sf.keytabgui.model.row.KeytabRow;

public class Realm implements Column {

	public String getTitle() {
		return "realm";
	}
	
	public Object getValue(KeytabRow row) {
		return row.getRealm();
	}

}
