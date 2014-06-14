package net.sf.keytabgui.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

public final class KeytabFileChooser extends JFileChooser {
	
	static {
	    String[] countries = { "zh", "zh", "en", "fr", "de", "it", "ja", "ko", "es", "sv" };
	    ArrayList<String> countriesList = new ArrayList<String>(Arrays.asList(countries));

	    String[] suppCountries = { "pl" };
	    ArrayList<String> addCountriesList = new ArrayList<String>(Arrays.asList(suppCountries));

	    String country = Locale.getDefault().getCountry().toLowerCase();
	    ResourceBundle rb;
	    if ((!(countriesList.contains(country))) && (addCountriesList.contains(country))) {
	      rb = ResourceBundle.getBundle("net/sf/keytabgui/properties/FileChooser");
	      if (rb == null)
	       System.out.println("Can't load JFileChooser properties");
	      else {
	        for (String key : rb.keySet()) {
	          UIManager.put(key, rb.getString(key));
	        }
	      }
	    }
	}
	
	public KeytabFileChooser() {
		super(System.getenv("USERPROFILE"));
		addChoosableFileFilter(new ExtensionFileFilter("keytab", "Pęk kluczy Kerberosa (*.keytab)"));
	}
	
	
	
	
	private class ExtensionFileFilter extends FileFilter {
		private String extension;
		private String description;
		
		public ExtensionFileFilter(String extension, String description) {
			this.extension = extension;
			this.description = description;
		}
		
		public boolean accept(File file) {
			return ((file.isDirectory()) || (file.getAbsolutePath().endsWith(this.extension)));
		}
		
		public String getDescription() {
			return this.description;
		}
	}
}

