package net.sf.keytabgui.controller.column;

import net.sf.keytabgui.controller.Column;
import net.sf.keytabgui.model.row.KeytabRow;

public class EncTypeComment implements Column {

	public String getTitle() {
		return "EncType comment";
	}

	/**
	 * @see RFC 3961 page 30
	 */
	public Object getValue(KeytabRow row) {
		switch (row.getKeyblockType()){
		case 1:  return "6.2.3";
		case 2:  return "6.2.2";
		case 3:  return "6.2.1";
		case 5:  return "";
		case 7:  return "";
		case 9:  return "(pkinit)";
		case 10: return "(pkinit)";
		case 11: return "(pkinit)";
		case 12: return "(pkinit)";
		case 13: return "(pkinit from PKCS#1 v.1.5)";
		case 14: return "(pkinit from PKCS#1 v.2.0)";
		case 15: return "(pkinit)";
		case 16: return "6.3";
		case 17: return "KRB5-AES";
		case 18: return "KRB5-AES";
		case 23: return "(Microsoft)";
		case 24: return "(Microsoft)";
		case 65: return "(opaque; PacketCable)";
		}
		return "unknown";
	}

}
