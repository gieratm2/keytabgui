package net.sf.keytabgui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.sf.keytabgui.model.Column;
import net.sf.keytabgui.model.column.EncType;
import net.sf.keytabgui.model.column.Kvno;
import net.sf.keytabgui.util.ConfigSingleton;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Upewnijmy sie czy lista kolumn zapisuje sie i odczytuje poprawnie
 *
 */
public class ConfigTest {
	private Column[] userConfig;
	
	@Before
	public void backupUserConfig(){
		userConfig = ConfigSingleton.INSTANCE.getColumns();
	}
	
	@Test
	public void testPersistingColumns(){
		// given
		Column[] cols = {new EncType(), new Kvno()};
		
		// when
		ConfigSingleton.INSTANCE.setColumns(cols);
		
		// then
		Column[] readCols = ConfigSingleton.INSTANCE.getColumns();
		assertEquals("Powinnismy otrzymac tyle samo kolumn ile zapisywalismy", cols.length, readCols.length);
		
		for (int i=0; i<cols.length; i++){
			assertNotNull("ConfigSingleton powinien utworzyc klase. Element " + i + " jest null'em", readCols[i]);
			assertEquals("Kolumny " + i + " zawieraja obiekty roznych klas.", cols[i].getClass().getCanonicalName(), readCols[i].getClass().getCanonicalName());
		}
	}

	@After
	public void restoreUserConfig(){
		ConfigSingleton.INSTANCE.setColumns(userConfig);
	}
}
