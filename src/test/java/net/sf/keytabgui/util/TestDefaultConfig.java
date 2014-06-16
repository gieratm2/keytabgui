package net.sf.keytabgui.util;

import static org.junit.Assert.*;
import net.sf.keytabgui.model.Column;

import org.junit.Test;

public class TestDefaultConfig {
	
	@Test
	public void testDefaultClassnames() {
		// given
		
		// when
		String defaultClassNames = ConfigSingleton.INSTANCE.getDefaultClassnames();
		Column[] columns = ColumnUtils.classnamesToColumns(defaultClassNames);
		
		// then
		for (int i=0; i<columns.length; i++){
			assertNotNull("Column["+i+"] jest null'em. Nie znaleziono klasy", columns[i]);
		}
	}

}
