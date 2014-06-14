package net.sf.keytabgui.view;

import static org.junit.Assert.*;

import java.awt.Component;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeoutException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import org.junit.Before;
import org.junit.Test;

/**
 * Proba testu przypadku uzycia przy pomocy JUnita
 * 
 * Test bedzie manipulowac tylko klasami dostarczonymi przez Jave (przetestowanymi). 
 * Sprawdzamy czy nasze klasy poprawnie sie zachowuja.
 *
 */
public class UseCaseTest {
	
	class TestedGui extends Gui {
		JFileChooser getFileChooser(){return chooser;}
		JButton getButtonOpen(){return buttonOpen;}
		TableModel getTableModel(){return table.getModel();}
		JDialog getErrorDialog(){return errorDialog;}
	}
	private TestedGui gui;
	
	/**
	 * Mamy problem: Inicjalizacja testu bedzie taka sama dla kilku testow.
	 * Powtorzenie jest jednym z tzw. "zapaszkow kodu", powoduje problemy, poniewaz 
	 * jesli w kopiowanym kodzie jest blad, w przyszlosci znajdziemy go i poprawimy, 
	 * to musimy pamietac, by poprawic go we wszystkich miejscach. :-(
	 * 
	 * Rozwiazaniem jest ekstrakcja wspolnego kodu do jednej metody.
	 * 
	 * JUnit daje 2 mozliwosci tutaj: 
	 * @throws InterruptedException  
	 * @throws TimeoutException 
	 * @BeforeClass - metoda uruchamiana przy tworzeniu klasy. Bedzie miala wplyw na wszystkie testy.
	 * @Before - metoda uruchamiana przed uruchamieniem kazdej metody (testu).
	 * 
	 * Ktora kiedy uzywac?
	 * Zasada: Poszczegolne testy musza byc niezalezne od siebie.
	 * 		np. bledem byloby (testy bylyby zalezne), gdyby jeden test modyfikowal obiekt, 
	 * 		a potem ta zmiana bylaby widoczna w innym tescie.
	 * Dlatego w BeforeClass tworzymy obiekty, ktore w tescie beda tylko odczytywane (np. ladujemy resource'y),
	 * natomiast w Before tworzymy obiekty, ktore test bedzie zmienial.
	 * 
	 * @throws TimeoutException jesli uplynal limit czasu oczekiwania na zbudowanie Gui
	 * @throws InterruptedException jesli jeszcze przed uplywem limitu czasu budowanie Gui zostalo przerwane
	 */
	@Before
	public void prepareGui() throws TimeoutException, InterruptedException {
		gui = new TestedGui();
		waitTillShowing(gui);
		
		// Uzytkownik klika przycisk "Otworz plik"
		new Thread(){
			public void run(){
				gui.getButtonOpen().doClick();				
			}
		}.start();
		// Uzytkownik czeka na wyswietlenie sie okienka, w ktorym bedzie mogl wybrac plik 
		waitTillShowing(gui.getFileChooser());
	}

	/**
	 * Uzytkownik otwiera dobry plik
	 * 
	 * @throws TimeoutException jesli przekroczono limit czasu oczekiwania na wyswietlenie okienka, w ktorym uzytkownik moze wybrac plik
	 * @throws InterruptedException jesli przed przekroczeniem limitu czasu aplikacja zostala przerwana
	 * @throws InvocationTargetException 
	 * 
	 */
	@Test
	public void openGoodFile() throws InterruptedException, TimeoutException, InvocationTargetException {
		
		// given - przeniesiony do sekcji @Before
		
		// when - Uzytkownik wpisuje plik (dobry)
		String goodFile = getAbsoluteFilename("test.keytab");
		openFile(goodFile);
		
		// then - w tabeli powinien byc wyswietlony plik, nazwa pliku (pelna sciezka) powinna byc w pasku tytulu
		TableModel model = gui.getTableModel();
		assertEquals("W pliku jest 5 wierszy.", 5, model.getRowCount());
		
		// spr. jedna z wartosci. 
		int colNo = getColumnNoByName(model, "principal");
		if (colNo>-1)
			assertEquals("principal w pliku: gieratm@KRB.TEST", "gieratm@KRB.TEST", model.getValueAt(0, colNo));
		
		// zamiast porownywac Stringi (w nich ma znaczenie separator: / czy \), porownujemy rownosc sciezek
		assertEquals("Nazwa pliku powinna byc wyswietlona w pasku tytulu", new File(goodFile), new File(gui.getTitle()) );
	}
	
	/**
	 * Sprawdzamy czy program poprawnie obsluguje otwieranie zlego pliku (alternatywna sciezka na diagramie przypadku uzycia) 
	 * 
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	@Test
	public void openInvalidFile() throws InterruptedException, InvocationTargetException {
		// given - przeniesiony do sekcji @Before
		
		// when - Uzytkownik wpisuje plik (zly)
		String invalidFile = getAbsoluteFilename("invalid.keytab");
		openFile(invalidFile);
		
		// then - Tabela powinna byc pusta. Pasek tytulu aplikacji tez.
		TableModel model = gui.getTableModel();
		assertEquals("Tabela powinna byc pusta.", 0, model.getRowCount());
		assertEquals("Pasek tytulu aplikacji powinien byc pusty", "", gui.getTitle());
		assertNotNull("Aplikacja powinna utworzyc okienko z komunikatem bledu", gui.getErrorDialog());
		
		closeErrorDialog(); // Okienko z komunikatem bledu trzeba zamknac, by zakonczyc test.
	}
	
	// --------------- metody pomocnicze ----------------------------
	
	private int getColumnNoByName(TableModel model, String columnName){
		int colNo = -1;
		for (int i=0; i<model.getColumnCount(); i++){
			if (columnName.equals(model.getColumnName(i))){
				return i;
			}
		}
		return colNo;
	}
	
	private void openFile(final String filename) throws InterruptedException, InvocationTargetException{
		SwingUtilities.invokeAndWait(new Runnable(){ // Swing is not thread safe! Komponenty Swinga mozna modyfikowac tylko w watku Swinga. http://mindprod.com/jgloss/swingthreads.html
			@Override
			public void run() {
				gui.getFileChooser().setSelectedFile( new File(filename) );
				gui.getFileChooser().approveSelection();
			}
		});
	}
	
	private void closeErrorDialog() throws InterruptedException, InvocationTargetException{
		SwingUtilities.invokeAndWait(new Runnable(){ // Swing is not thread safe! Komponenty Swinga mozna modyfikowac tylko w watku Swinga. http://mindprod.com/jgloss/swingthreads.html
			@Override
			public void run() {
				gui.getErrorDialog().dispose();
			}
		});
	}
	
	private void waitTillShowing(Component c) throws TimeoutException, InterruptedException{
		int timeoutCounter = 100;
		while (!c.isShowing() && timeoutCounter-- > 0) {
			Thread.currentThread().sleep(100);
		}
		if (!c.isShowing()) {
			throw new TimeoutException();
		}
	}
	
	private String getAbsoluteFilename(String filename){
		return this.getClass().getResource(filename).getFile();
	}

}
