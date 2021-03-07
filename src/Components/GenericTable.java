package Components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class GenericTable<T> extends JTable {
	private TableModel model = new TableModel();

	public GenericTable(String[] columns) {
		setHeader(columns);
		setModel(model);
		setAutoCreateRowSorter(true);
//			setDefaultRenderer(String.class, new TableCellRenderer());
//			model.items = HibernateSessionFactoryUtil.loadAllData(clazz);
	}
	
	public void setHeader(String[] columns)
	{
		model.columns = columns;
	}
	
	public void setItems(List vec) {
		model.items = vec;
		this.updateUI();
//		model.filter("");
	}

	private class TableCellRenderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			c.setBackground(Color.white);
			return c;
		}
	}

	private class TableModel extends AbstractTableModel {
		public List<T> items = new ArrayList<>();
		private String[] columns = new String[0];

		public int getColumnCount() {
			return columns.length;
		}

		public int getRowsCount() {
			return items.size();
		}

		public Object getValueAt(int row, int col) {
			T book = items.get(row);

			Class myClass = book.getClass();
			Field[] fields = myClass.getDeclaredFields();
			Object value;
			try {
				value = fields[col].get(book);
			} catch (IllegalAccessException e) {
				value = null;
			}
			return value;
		}

		public String getColumnName(int col) {
			return columns[col];
		}

		@Override
		public int getRowCount() {
			return items.size();
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (items.isEmpty()) {
				return Object.class;
			}
			return getValueAt(0, columnIndex).getClass();
		}

		public boolean isCellEditable(int row, int col) {
			return true;
		}

		public void setValueAt(Object value, int row, int col) {
			T book = items.get(row);
			Class myClass = book.getClass();
			Field[] fields = myClass.getDeclaredFields();
			Object value1;
			try {
				fields[col].set(book, value);
			} catch (IllegalAccessException e) {
			}
//			fireTableCellUpdated(row, col);
		}

	}

}
