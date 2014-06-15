package net.sf.keytabgui.model.column;

import net.sf.keytabgui.model.Column;
import net.sf.keytabgui.model.row.KeytabRow;

public class EncType implements Column {

	public String getTitle() {
		return "EncType";
	}

	/**
	 * @see RFC 3961 page 30
	 */
	public Object getValue(KeytabRow row) {
		switch (row.getKeyblockType()){
		case 1:  return "des-cbc-crc";
		case 2:  return "des-cbc-md4";
		case 3:  return "des-cbc-md5";
		case 5:  return "des3-cbc-md5";
		case 7:  return "des3-cbc-sha1";
		case 9:  return "dsaWithSHA1-CmsOID";
		case 10: return "md5WithRSAEncryption-CmsOID";
		case 11: return "sha1WithRSAEncryption-CmsOID";
		case 12: return "rc2CBC-EnvOID";
		case 13: return "rsaEncryption-EnvOID";
		case 14: return "rsaES-OAEP-ENV-OID";
		case 15: return "des-ede3-cbc-Env-OID";
		case 16: return "des3-cbc-sha1-kd";
		case 17: return "aes128-cts-hmac-sha1-96";
		case 18: return "aes256-cts-hmac-sha1-96";
		case 23: return "rc4-hmac";
		case 24: return "rc4-hmac-exp";
		case 65: return "subkey-keymaterial";
		}
		return "unknown";
	}

}
