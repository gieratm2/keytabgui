package net.sf.keytabgui.util.rowreader;

import java.io.IOException;

import net.sf.keytabgui.model.row.KeytabRow;
import net.sf.keytabgui.util.KeytabInputStream;

public interface RowReader {
	
	KeytabRow readRow(KeytabInputStream in) throws IOException;

}
