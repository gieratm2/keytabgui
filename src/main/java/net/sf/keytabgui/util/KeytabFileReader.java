package net.sf.keytabgui.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import net.sf.keytabgui.model.Keytab;
import net.sf.keytabgui.model.row.KeytabRow;
import net.sf.keytabgui.util.rowreader.NewRowReader;
import net.sf.keytabgui.util.rowreader.OldRowReader;
import net.sf.keytabgui.util.rowreader.RowReader;

/**
 * Wzorzec Fabryka abstrakcyjna (kreacyjny): 
 * 
 * Provide an interface for creating families of related or dependent objects without specifying their concrete classes. 
 *
 */
public class KeytabFileReader {
	
	public synchronized Keytab read(String filename) throws IOException {
		
		KeytabInputStream in = new KeytabInputStream(new FileInputStream(filename));
		
		Keytab keytab = new Keytab();
		keytab.setMajorVersion(in.read());
		keytab.setMinorVersion(in.read());
		
		RowReader reader = getRowReader(keytab.getMajorVersion(), keytab.getMinorVersion());
		List<KeytabRow> rows = keytab.getRows();
		KeytabRow row = null;
		while (in.available()>0){
			rows.add(reader.readRow(in)); // za odczyt wiersza odpowiedzialna jest konkretna klasa
		}
		
		return keytab;
	}
	
	private RowReader getRowReader(int majorVersion, int minorVersion) throws IOException {
		switch (minorVersion){
		case 1: return new OldRowReader();
		case 2: return new NewRowReader();
		default:
			StringBuilder msg = new StringBuilder("Uknown keytab file version: ");
			msg.append(majorVersion);
			msg.append('.');
			msg.append(minorVersion);
			throw new IOException(msg.toString());
		}
	}

}