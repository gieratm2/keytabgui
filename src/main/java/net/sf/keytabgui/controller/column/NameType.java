package net.sf.keytabgui.controller.column;

import net.sf.keytabgui.controller.Column;
import net.sf.keytabgui.model.row.KeytabRow;
import net.sf.keytabgui.model.row.NewKeytabRow;

public class NameType implements Column {

	public String getTitle() {
		return "name type";
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
			case 0: return "KRB5_NT_UNKNOWN"; // Name type not known.
			case 1: return "KRB5_NT_PRINCIPAL"; // Just the name of the principal as in DCE, or for users.
			case 2: return "KRB5_NT_SRV_INST"; // Service and other unique instance (krbtgt)
			case 3: return "KRB5_NT_SRV_HST"; // Service with host name as instance (telnet, rcommands)
			case 4: return "KRB5_NT_SRV_XHST"; // Service with host as remaining components.
			case 5: return "KRB5_NT_UID"; // Unique ID.
			case 6: return "KRB5_NT_X500_PRINCIPAL"; // PKINIT.
			case 7: return "KRB5_NT_SMTP_NAME"; // Name in form of SMTP email name.
			case 10: return "KRB5_NT_ENTERPRISE_PRINCIPAL"; // Windows 2000 UPN
			case 11: return "KRB5_NT_WELLKNOWN"; // Well-known (special) principal.
			
			case -128: return "KRB5_NT_MS_PRINCIPAL"; // Windows 2000 UPN and SID.
			case -130: return "KRB5_NT_ENT_PRINCIPAL_AND_ID"; // NT 4 style name and SID.
			case -129: return "KRB5_NT_MS_PRINCIPAL_AND_ID"; // NT 4 style name
			}
			System.out.println("Unrecognized NameType: " + nameType);
			return nameType + " ?";
		} else {
			System.out.println("Old keytab doesn't have NameType attribute.");
			return "ERR";
		}
	}

}