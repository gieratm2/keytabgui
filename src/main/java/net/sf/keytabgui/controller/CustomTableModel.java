package net.sf.keytabgui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.table.AbstractTableModel;

import net.sf.keytabgui.model.Keytab;
import net.sf.keytabgui.model.row.KeytabRow;
import net.sf.keytabgui.util.KeytabFileReader;

/**
 * 
 * 	Wymaganie 11: "czy skorzystano z polimorfizmu"
 *  Tak. Metody przesłaniane w tej klasie są wyróżnione przy użyciu adnotacji @Override.
 *  
 *  Na przykładzie tej klasy:
 *  Przesłanianie to kolokwialnie: dostarczanie konkretnej, lepszej implementacji, 
 *  np. lepsza nazwa kolumny.
 *  
 *  domyślnie: A, B, C, ... Z, AA, AB (jak w Excelu) - metoda getColumnName w klasie AbstractTableModel
 *  my zwracamy konkretną nazwę kolumny, przy pomocy metody getColumnName
 *  Przesłaniamy getColumnName z klasy AbstractTableModel naszą getColumnName
 *  
 *  Adnotacją @Override możemy oznaczać 
 *  zarówno metody przesłaniające metody, które mają implementację w klasie nadrzędnej np. getColumnName,
 *  jak i metody przesłaniające metody abstrakcyjne, czyli bez implementacji w klasie nadrzędnej, np. getRowCount, getColumnCount, getValueAt. 
 *
 */
public class CustomTableModel extends AbstractTableModel implements ActionListener {
	
	private List<Column> columns = new ArrayList<Column>();
	private Keytab keytab = new Keytab();
	
	public CustomTableModel(Column... cols){
		this.columns.addAll(Arrays.asList(cols));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (JFileChooser.APPROVE_SELECTION.equals(e.getActionCommand())){
			if (e.getSource() instanceof JFileChooser){
				JFileChooser chooser = (JFileChooser)e.getSource();
				String filename = chooser.getSelectedFile().getAbsolutePath();
				try {
					keytab = new KeytabFileReader().read(filename);
					this.fireTableDataChanged();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	
	@Override
	public int getRowCount() {
		return keytab.getRowCount();
	}

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Column col = columns.get(columnIndex);
		KeytabRow row = keytab.getRow(rowIndex);
		return col.getValue(row);
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return columns.get(columnIndex).getTitle();
	}

}
