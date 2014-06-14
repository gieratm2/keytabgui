package net.sf.keytabgui.controller;

import net.sf.keytabgui.model.row.KeytabRow;

public interface Column {

	String getTitle();
	Object getValue(KeytabRow row);
}
