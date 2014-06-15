package net.sf.keytabgui.model.column;

import net.sf.keytabgui.model.Column;
import net.sf.keytabgui.model.row.KeytabRow;

public class Principal implements Column {

	public String getTitle() {
		return "principal";
	}
	
	public Object getValue(KeytabRow row) {
		StringBuilder sb = new StringBuilder();
		
		String[] components = row.getComponents();
		for (int i=0; i<components.length; i++){
			sb.append(components[i]);
			if (i+1<components.length)
				sb.append('/');
		}
		sb.append('@');
		sb.append(row.getRealm());
		return sb.toString();
	}

}
