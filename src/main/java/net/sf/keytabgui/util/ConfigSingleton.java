package net.sf.keytabgui.util;

import java.util.Arrays;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import net.sf.keytabgui.model.Column;
import net.sf.keytabgui.model.column.EncType;
import net.sf.keytabgui.model.column.EncTypeComment;
import net.sf.keytabgui.model.column.EntrySize;
import net.sf.keytabgui.model.column.Key;
import net.sf.keytabgui.model.column.Kvno;
import net.sf.keytabgui.model.column.NameType;
import net.sf.keytabgui.model.column.NameTypeComment;
import net.sf.keytabgui.model.column.Principal;
import net.sf.keytabgui.model.column.Realm;
import net.sf.keytabgui.model.column.Timestamp;

/**
 * Wzorzec Singleton mozemy pokazac na przykladzie obiektu przechowujacego
 * konfiguracje. - W czasie dzialania programu powinien istniec tylko jeden taki
 * obiekt. - Z kazdego miejsca aplikacji mozna otrzymac ten obiekt.
 * 
 * Definicja Gang of Four:
 * "Ensure a class has only one instance, and provide a global point of access to it."
 * 
 * Ten wzorzec jest zwodniczo prosty. Glowne problemy: - problemy wynikajace z
 * dostepu z wielu watkow - naduzywanie.
 * 
 * Charakterystyczne cechy budowy: prywatny kontstruktor (by nie mozna go bylo
 * wywolac z zewnatrz klasy) publiczna, statyczna metoda getInstance jedyna
 * instancje obiektu przechowujemy w zmiennej statycznej
 * 
 * 
 * Standardowo implementuje sie to tak (wariant eager synchronization, poniewaz
 * obiektu konfiguracji bedziemy uzywac zawsze, przy kazdym uruchomieniu
 * aplikacji):
 * 
 * private static final ConfigSingleton INSTANCE = new ConfigSingleton(); 
 * public static ConfigSingleton getInstance() { return INSTANCE; } 
 * private ConfigSingleton(){ ... }
 * 
 * Joshua Bloch (Architekt w Sunie, tworca kolekcji w java.util) poleca
 * implementowac Singletona na bazie enuma (od Java 1.5)
 * 
 * @see http
 *      ://www.drdobbs.com/jvm/creating-and-destroying-java-objects-par/208403883
 *      ?pgno=3
 * 
 *      As of release 1.5, there is a third approach to implementing singletons.
 *      Simply make an enum type with one element This approach is functionally
 *      equivalent to the public field approach, except that it is more concise,
 *      provides the serialization machinery for free, and provides an ironclad
 *      guarantee against multiple instantiation, even in the face of
 *      sophisticated serialization or reflection attacks. While this approach
 *      has yet to be widely adopted, a single-element enum type is the best way
 *      to implement a singleton.
 * 
 */
public enum ConfigSingleton {
	INSTANCE;
	
	public static final Column[] COLUMNS = {
		new EncType(),
		new EncTypeComment(),
		new EntrySize(),
		new Key(),
		new Kvno(),
		new Principal(),
		new NameType(),
		new NameTypeComment(),
		new Realm(),
		new Timestamp()
	};
	public static final String COLUMN_CLASSNAMES = "columnClassnames";

	private Preferences prefs;

	private ConfigSingleton() {
		prefs = Preferences.userRoot().node(this.getClass().getName());
	}

	public Column[] getColumns() {
		// odczytajmy liste nazw klas
		String sCC = prefs.get(COLUMN_CLASSNAMES, getDefaultClassnames());
		return ColumnUtils.classnamesToColumns(sCC);
	}
	
	public void setColumns(Column... cols){
		String[] classnames = new String[cols.length];
		for (int i=0; i<cols.length; i++){
			classnames[i] = cols[i].getClass().getCanonicalName();
		}
		prefs.put(COLUMN_CLASSNAMES, Arrays.toString(classnames));
	}
	
	public void addPreferenceChangeListener(PreferenceChangeListener pcl){
		prefs.addPreferenceChangeListener(pcl);
	}

	/**
	 * Lista domyslnych nazw klas jest tworzona dynamicznie, a nie podana w
	 * Stringu, by uniknac klopotow jesli by sie zmienilo nazwe klasy (w ramach
	 * refactoringu).
	 * 
	 * @return lista domyslnych nazw klas
	 */
	String getDefaultClassnames() {
		return "[" + Principal.class.getCanonicalName() + ","
				+ Kvno.class.getCanonicalName() + ","
				+ EncType.class.getCanonicalName() + "," + "]";
	}

}
