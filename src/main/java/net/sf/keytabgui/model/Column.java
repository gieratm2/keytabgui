package net.sf.keytabgui.model;

import net.sf.keytabgui.model.row.KeytabRow;

public interface Column {

	String getTitle();
	Object getValue(KeytabRow row);
}
