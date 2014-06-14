package net.sf.keytabgui;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.sf.keytabgui.model.Keytab;
import net.sf.keytabgui.model.row.KeytabRow;
import net.sf.keytabgui.util.KeytabFileReader;
import net.sf.keytabgui.util.ToString;

import org.junit.Test;

/**
 * Przyklad klasycznych testow jednostkowych: sprawdzanie poprawnosci metody.
 * Podajemy dane wejsciowe, sprawdzamy dane wyjsciowe.
 * 
 * Czysto napisany test sklada sie z czesci given, when i then.
 * Piszac test, warto zaczac od napisania takich komentarzy.
 *
 */
public class FileReadingTest {
	
	/**
	 * Wygenerowalismy plik test.keytab, znamy jego zawartosc.
	 * Sprawdzamy, czy new KeytabFileReader().read(filename) poprawnie odczyta ten plik.
	 * 
	 * W assertEquals porownujemy dwie wartosci. 
	 * Pierwsza wartosc (oczekiwana) to jest wartosc zwrocona przy tworzeniu pliku test.keytab przez polecenie Microsoft ktpass
	 * Druga jest wartosc odczytana z pliku. Musza byc rowne.
	 * 
	 * @throws IOException W tym tescie nie powinien wystapic zaden wyjatek.
	 */
	@Test
	public void testSimpleKeytab() throws IOException {
		// given
		String filename = this.getClass().getResource("test.keytab").getFile();
		
		// when
		Keytab keytab = new KeytabFileReader().read(filename);
		
		// then
		assertEquals("File contains 5 rows.", 5, keytab.getRowCount());
		
		KeytabRow row0 = keytab.getRow(0);
		assertEquals("KVNO: 3", 3, row0.getVno8());
		assertEquals("Key type: 1", 1, row0.getKeyblockType());
		assertEquals("Key: 0x7a5bc12cfb341097", "0x7a5bc12cfb341097", "0x" +ToString.bytesToHex(row0.getKeyblock()).toLowerCase());
		assertEquals("Timestamp: 0", 0, row0.getTimestamp());
		
		KeytabRow row1 = keytab.getRow(1);
		assertEquals("KVNO: 3", 3, row1.getVno8());
		assertEquals("Key type: 3", 3, row1.getKeyblockType());
		assertEquals("Key: 0x7a5bc12cfb341097", "0x7a5bc12cfb341097", "0x" +ToString.bytesToHex(row1.getKeyblock()).toLowerCase());
		assertEquals("Timestamp: 0", 0, row1.getTimestamp());
		
		KeytabRow row2 = keytab.getRow(2);
		assertEquals("KVNO: 3", 3, row2.getVno8());
		assertEquals("Key type: 23", 23, row2.getKeyblockType());
		assertEquals("Key: 0x9bf79044fe86e5d86c3dda9af0838837", "0x9bf79044fe86e5d86c3dda9af0838837", "0x" +ToString.bytesToHex(row2.getKeyblock()).toLowerCase());
		assertEquals("Timestamp: 0", 0, row2.getTimestamp());
		
		KeytabRow row3 = keytab.getRow(3);
		assertEquals("KVNO: 3", 3, row3.getVno8());
		assertEquals("Key type: 18", 18, row3.getKeyblockType());
		// Wykomentowana wartosc (podana przez polecenie klist Javy 1.6.43 jest bledna).
		//assertEquals("Key: 0x557832a7eaf01d925741d7f338747cb875eb7e442bfea0d4912d58d41faee", "0x557832a7eaf01d925741d7f338747cb875eb7e442bfea0d4912d58d41faee", "0x" +ToString.bytesToHex(row3.getKeyblock()).toLowerCase());
		// Wartosc wyswietlona przez polecenie ktpass (po wygenerowaniu pliku test.keytab):
		assertEquals("Key: 0x557832a7eaf01d0925741d7f3308747cb875eb7e442bfea0d40912d58d41faee", "0x557832a7eaf01d0925741d7f3308747cb875eb7e442bfea0d40912d58d41faee", "0x" +ToString.bytesToHex(row3.getKeyblock()).toLowerCase());
		assertEquals("Timestamp: 0", 0, row3.getTimestamp());
		
		KeytabRow row4 = keytab.getRow(4);
		assertEquals("KVNO: 3", 3, row4.getVno8());
		assertEquals("Key type: 17", 17, row4.getKeyblockType());
		// klist NOK
		//assertEquals("Key: 0x5252cbc50ad44db1fea48e5b68c8f2", "0x5252cbc50ad44db1fea48e5b68c8f2", "0x" +ToString.bytesToHex(row4.getKeyblock()).toLowerCase());
		// ktpass OK
		assertEquals("Key: 0x5252cb0c50ad440db1fea48e5b68c8f2", "0x5252cb0c50ad440db1fea48e5b68c8f2", "0x" +ToString.bytesToHex(row4.getKeyblock()).toLowerCase());
		
		assertEquals("Timestamp: 0", 0, row4.getTimestamp());
	}
	
	/**
	 * Gdy chcemy sprawdzic, czy metoda poprawnie rzuca wyjatek,
	 * wpisujemy nazwe wyjatku w polu expected adnotacji Test. 
	 * 
	 * @throws IOException
	 */
	@Test(expected = FileNotFoundException.class)
	public void testWrongPath() throws IOException {
		// given
		String pathToWrongFile = "./missing-file.txt";
		
		// when
		new KeytabFileReader().read(pathToWrongFile);
		
		// then: FileNotFoundException is thrown
	}
	
	/**
	 * W JUnit'cie 4 nazwa metody nie musi sie juz zaczynac od test (jak w JUnit'cie 3).
	 * Moze byc dowolna, byle byla poprzedzona adnotacja @Test
	 * 
	 * @throws IOException
	 */
	@Test(expected = IOException.class)
	public void sprawdzZlyPlik() throws IOException {
		// given
		String filename = this.getClass().getResource("test.txt").getFile();
		
		// when
		new KeytabFileReader().read(filename);
		
		// then: IOException is thrown
	}
}
