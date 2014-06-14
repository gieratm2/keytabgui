package net.sf.keytabgui.controller.column;

import net.sf.keytabgui.controller.Column;
import net.sf.keytabgui.model.row.KeytabRow;
import net.sf.keytabgui.model.row.NewKeytabRow;

public class NameTypeComment implements Column {

	public String getTitle() {
		return "name type comment";
	}
	
	/**
	 * Principal types: KRB5_NT_*
	 * 
	 * @see http://web.mit.edu/kerberos/krb5-1.11/doc/appdev/refs/macros/
	 */
	public Object getValue(KeytabRow row) {
		if (row instanceof NewKeytabRow){
			int nameType = ((NewKeytabRow) row).getNameType();
			switch (nameType){
			case 0: return "Name type not known.";
			case 1: return "Just the name of the principal as in DCE, or for users.";
			case 2: return "Service and other unique instance (krbtgt)";
			case 3: return "Service with host name as instance (telnet, rcommands)";
			case 4: return "Service with host as remaining components.";
			case 5: return "Unique ID.";
			case 6: return "PKINIT.";
			case 7: return "Name in form of SMTP email name.";
			case 10: return "Windows 2000 UPN";
			case 11: return "Well-known (special) principal.";
			
			case -128: return "Windows 2000 UPN and SID.";
			case -130: return "NT 4 style name and SID.";
			case -129: return "NT 4 style name";
			}
			System.out.println("Unrecognized NameType: " + nameType);
			return nameType + " ?";
		} else {
			System.out.println("Old keytab doesn't have NameType attribute.");
			return "ERR";
		}
	}

}