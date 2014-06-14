package net.sf.keytabgui.model;

import java.util.ArrayList;
import java.util.List;

import net.sf.keytabgui.model.row.KeytabRow;

public class Keytab {
	
	private int majorVersion;
	private int minorVersion;
	private List<KeytabRow> rows = new ArrayList<KeytabRow>();
	
	

	public int getMajorVersion() {
		return majorVersion;
	}
	
	public void setMajorVersion(int majorVersion) {
		this.majorVersion = majorVersion;
	}
	
	public int getMinorVersion() {
		return minorVersion;
	}
	
	public void setMinorVersion(int minorVersion) {
		this.minorVersion = minorVersion;
	}
	
	

	public KeytabRow getRow(int rowIndex) {
		return rows.get(rowIndex);
	}
	
	public int getRowCount() {
		return rows.size();
	}
	
	public List<KeytabRow> getRows(){
		return this.rows;
	}
}
