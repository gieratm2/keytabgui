package net.sf.keytabgui.controller.column;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.keytabgui.controller.Column;
import net.sf.keytabgui.model.row.KeytabRow;

public class Timestamp implements Column {
	
	private DateFormat formatter = SimpleDateFormat.getInstance();

	public String getTitle() {
		return "timestamp";
	}
	
	public Object getValue(KeytabRow row) {
		int timestamp = row.getTimestamp(); // ms from Unix start era 1.1.1970
		Date date = new Date(timestamp);
		return formatter.format(date);
	}

}
