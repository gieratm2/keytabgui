package net.sf.keytabgui.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.prefs.PreferenceChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import net.sf.keytabgui.controller.Column;
import net.sf.keytabgui.controller.CustomTableModel;
import net.sf.keytabgui.util.ColumnUtils;
import net.sf.keytabgui.util.ConfigSingleton;
import net.sf.keytabgui.util.KeytabFileReader;

/**
 * 
 * Swing implementuje wzorzec architektoniczny MVC, Model View Controller. 
 * View i controller często są łączone ze sobą.
 * 
 * Na przykładzie tabeli:
 *  view/controller: JTable
 *  model: TableModel
 *  
 * Podział odpowiedzialności:
 *  JTable prezentuje jest odpowiedzialna za prezentowanie danych (view) 
 *  	oraz obsługę zdarzeń (kliknięcia, edytowanie danych, itd).
 *  TableModel - interfejs, dzięki któremu JTable uzyskuje dostęp do danych.
 *  
 *  Taki podział odpowiedzialności utrudnia dostęp do warstwy prezentacji.
 *  Około 50% funkcjonalności (typowej) implementuje za nas AbstractTableModel.
 *  W CustomTableModelu implementujemy tylko to, czego brakuje, czyli jakie konkretnie dane chcemy pokazywać (listę obiektów klasy KeytabRow)
 *  
 *  
 *  
 *  
 *  Wzorzec projektowy Observer
 *  
 *  To jeden z wzorców czynnościowych. Problem: obiekt potrzebuje informacji o zmianie stanu innego obiektu.
 *  Przykład: Otwieranie pliku.
 *  Biorą w tym udział 4 klasy:
 *  - JFileChooser (on otrzymuje informację, który plik użytkownik chce otworzyć).
 *  - CustomTableModel (który musi uaktualnić dane tabeli)
 *  - JTable (uaktualnia widok tabeli)
 *  - Gui (Chcemy pokazać nazwę wybranego pliku w pasku tytułu okienka)
 *  
 *  JFileChooser --> CustomTableModel --> JTable
 *              \-----------------------> Gui
 *              
 *  Obiekt obserwowany w momencie zmiany stanu musi rozgłosić informację o tym. 
 *  Przykład: w klasie CustomTableModel, gdy zmieniają się dane, powiadamiamy o tym słuchaczy (akurat tylko jednego: JTable), tzn. 
 *  	wywołujemy metodę fireTableDataChanged(); JTable otrzyma dane w metodzie tableChanged, ponieważ implementuje interfejs TableModelListener.
 *  
 *  Drugi przykład: w poprzednim przykładzie wysyłaliśmy dane. Mamy też przykład odbierania danych.
 *      CustomTableModel implementuje interfejs ActionListener. Obiekt tej klasy został powiązany z obiektem klasy JFileChooser, więc
 *      gdy użytkownik wybrał plik, customTableModel otrzyma informację o tym w metodzie actionPerformed().
 *      
 *  Obiekt obserwowany musi utrzymywać listę referencji do obserwatorów.
 *  Trzeba rejestrować i wyrejestrowywać listnerów. 
 *  Zapominanie o wyrejestrowaniu może grozić wyciekami pamięci. Jeśli zapomniało się wyrejestrować, to mimo że zwolniło się referencję do obiektu, obiekt nie zostanie zniszczony przez Garbage Collectora, ponieważ jest jeszcze referencja do niego na liście listenerów obiektu obserwowanego. Radą na to jest rejestrowanie WeakReference do listenera.
 *  
 *  W Javie mamy też interfejs Observer i klasę Observable. Wysyłają one bardziej ogólne powiadomienie.
 *  Observable posiada metodę notifyObservers() oraz notifyObservers(Object)
 *  Listenery (stosowanych w Swingu) przesyłają wiadomość konkretnego typu, nie Object.
 *  
 *  Porównanie z wzorcem Publish-Subscribe:
 *  We wzorcu Observer wiążemy dwa obiekty ze sobą bezpośrednio. Wobec tego każdy obserwowany musi utrzymywać własną listę obserwatorów.
 *  W publish-subscribe te listy obserwatorów łączymy w jedną, prowadzoną przez klasę EventBus (w Java Message Service: Broker).
 *  Zmniejsza to liczbę powiązań między klasami (weeker coupling). 
 *
 */
public class Gui extends JFrame implements ActionListener {
	
	// pola są protected, abyśmy mogli uzyskać dostęp do nich w teście UseCaseTest
	protected JTable table;	
	protected JFileChooser chooser; 
	protected JButton buttonOpen;
	protected JDialog errorDialog;
	protected JButton buttonChooseColumns;
	protected JDialog chooseColumnsDialog;
	
	public Gui(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setRollover(true);
		
		chooser = new KeytabFileChooser();
		chooser.addActionListener(this); // gdy użytkownik wybierze nowy plik, pokażemy jego nazwę w pasku tytułu aplikacji
		
		buttonOpen = new JButton("Otwórz plik");
		buttonOpen.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Gui.this.chooser.showOpenDialog(Gui.this);
			}
		});
		toolbar.add(buttonOpen);
		
		buttonChooseColumns = new JButton("Wybierz kolumny");
		buttonChooseColumns.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String[] colNames = new String[ConfigSingleton.COLUMNS.length];
				Set<String> selectedColumnNames = ColumnUtils.getSelectedColumnNames();
				List<Integer> selectedColumnIdices = new ArrayList<Integer>();
				for (int i=0; i<ConfigSingleton.COLUMNS.length; i++){
					colNames[i] = ConfigSingleton.COLUMNS[i].getTitle();
					if (selectedColumnNames.contains(colNames[i])){
						selectedColumnIdices.add(i);
					}
				}
				JList list = new JList(colNames);
				
				// show on list which columns are currently visible
				list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				int[] selectedIndices = new int[selectedColumnIdices.size()];
				for (int i=0; i<selectedIndices.length; i++){
					selectedIndices[i] = selectedColumnIdices.get(i);
				}
				list.setSelectedIndices(selectedIndices);
				
				JOptionPane.showMessageDialog(Gui.this, list, "Zaznacz, które kolumny pokazywać", JOptionPane.PLAIN_MESSAGE);
				
				Column[] columns = new Column[list.getSelectedValuesList().size()];
				// przejrzyjmy liste wszystkich kolumn. 
				// Jesli tytul kolumny bedzie wybrany, wstawiamy kolumne.				
				ArrayList<Column> alCols = new ArrayList<Column>();
				for (Column column: ConfigSingleton.COLUMNS){
					if (list.getSelectedValuesList().contains(column.getTitle())){
						alCols.add(column);
					}
				}
				columns = alCols.toArray(columns);
				ConfigSingleton.INSTANCE.setColumns(columns);
			}
			
		});
		toolbar.add(buttonChooseColumns);
		
		this.add(toolbar, BorderLayout.NORTH);
		
		Column[] columns = ConfigSingleton.INSTANCE.getColumns();
		// Wymaganie 10: "czy skorzystano z interfejsów i/lub klas abstrakcyjnych do reprezentowania abstrakcji jako jednego z elementów programowania obiektowego" 
		// abstrakcja kolumny. Podajemy że kolumna to obiekt implementujący interfejs Column.
		// Dzięki temu spełniona jest zasada Open/Closed. Gdy chcemy dodać nową kolumnę, (open:) dodajemy klasę implementującą interfejs Column, nastomiast nie dotykamy klasy CustomTableModel (pozostaje Closed).
		
		// To jest wzorzec projektowy Bridge (strukturalny): oddzielamy abstrakcję obiektu od jego implementacji. To wymusza zmniejszenie powiązań (week coupling) między obiektem posługującym się abstrakcją, a konkretnymi implementacjami. (week coupling & strong cohesion).
		// Strong cohesion poznajemy po tym, że klasa jest poświęcona tylko 1 rzeczy, mała, wszystkie zmienne, które zawiera związane są z tą jedną rzeczą (mówimy o niej: odpowiedzialność).
		// Przeciwieństwem 'strong cohesion' jest tzw. God-object, czyli klasa, która ma wie wszystko (mnóstwo pól) i robi wszystko (wiele metod). Jest bardzo długa.
		// 
		TableModel tableModel = new CustomTableModel(columns);
		chooser.addActionListener((ActionListener)tableModel);
		ConfigSingleton.INSTANCE.addPreferenceChangeListener((PreferenceChangeListener)tableModel);
		table = new JTable(tableModel);
		table.setFillsViewportHeight(true); // When this property is true the table uses the entire height of the container, even if the table doesn't have enough rows to use the whole vertical space. This makes it easier to use the table as a drag-and-drop target.
		table.setAutoCreateRowSorter(true); // Dzięki temu użytkownik może sortować wiersze tabeli po wybranej kolumnie
		JScrollPane jsp = new JScrollPane(table);
		add(jsp, BorderLayout.CENTER);
		
		this.pack();
		setSize(1000, 400); // To wywołujemy po pack(), by powiększyć aplikację. Rozmiar ustawiony przez pack() jest zbyt mały.
		
		// pokazemy GUI dopiero jak wszystko sie do konca wyrenderuje (zgodnie z zaleceniem opisanym w ksiazce "Thinking in Java") 
		java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
            }
        });
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (JFileChooser.APPROVE_SELECTION.equals(e.getActionCommand())){
			if (e.getSource() instanceof JFileChooser){
				JFileChooser chooser = (JFileChooser)e.getSource();
				String path = chooser.getSelectedFile().getAbsolutePath();
				try {
					new KeytabFileReader().read(path);
					this.setTitle(path);
				} catch (IOException e1) {
					//JOptionPane.showMessageDialog(this, "Nie można otworzyć pliku: " + path, "Błąd", JOptionPane.ERROR_MESSAGE);
					
					JOptionPane pane = new JOptionPane("Nie można otworzyć pliku: " + path, JOptionPane.ERROR_MESSAGE);
					errorDialog = pane.createDialog(this, "Błąd");
					errorDialog.setModal(false); // aby mozna bylo zamknac to okienko w tescie, by nie blokowalo testu, zwlaszcza testy uruchamia Maven.
					errorDialog.setVisible(true);
				}
			}
		}
	}

}
