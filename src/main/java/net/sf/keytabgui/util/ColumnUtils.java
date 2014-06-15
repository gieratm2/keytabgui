package net.sf.keytabgui.util;

import java.util.TreeSet;

import net.sf.keytabgui.controller.Column;

public class ColumnUtils {

	public static TreeSet<String> getSelectedColumnNames() {
		Column[] columns = ConfigSingleton.INSTANCE.getColumns();
		TreeSet<String> selectedColumnNames = new TreeSet<String>();
		for (Column c: columns){
			selectedColumnNames.add(c.getTitle());
		}
		return selectedColumnNames;
	}

	public static Column[] classnamesToColumns(String sCC) {
		// usuwamy znaki '[' oraz ']'
		sCC = sCC.replaceAll("[\\[\\]]", ""); // patterns: http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
		String[] classnames = sCC.split(",");
		for (int i=0; i<classnames.length; i++){
			classnames[i] = classnames[i].trim();
		}

		// lets instantiate those classes
		Column[] cols = new Column[classnames.length];
		for (int i = 0; i < cols.length; i++) {
			try {
				Class clazz = Class.forName(classnames[i]);
				cols[i] = (Column) clazz.newInstance();
			} catch (Exception notExpected) {
				notExpected.printStackTrace();
			}
		}
		return cols;
	}
	
	

}
