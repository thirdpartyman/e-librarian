package Components;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.hibernate.Session;
import org.hibernate.Transaction;

import ComboBox.GenericComboBox;
import Database.Book;
import Database.HibernateUtil;

public class GenericTable<T> extends JTable {
	private TableModel model = new TableModel();

	public GenericTable(Class<T> type, String[] columns) {
		setHeader(columns);
		setModel(model);
		setAutoCreateRowSorter(true);
		getColumnModel().getColumn(0).setResizable(false);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( DefaultTableCellRenderer.CENTER );
		getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
		centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( DefaultTableCellRenderer.LEFT );
		getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		setEventListeners();
		reloadHandler = () -> setItems(HibernateUtil.loadAllData(type));
	}
	
	
	Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction transaction = session.beginTransaction();

	private void setEventListeners()
	{	
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					removeItems(getSelectedItems());
		        }
			}
		});
		
		addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				super.focusLost(e);
				getSelectionModel().clearSelection();
			}
		});
	}
	
	public void removeItems(List<T> items)
	{
		model.items.removeAll(items);
		for (var item : items)
			session.delete(item);
		updateUI();
	}
	
	public void saveChanges() {
		var list = getItems();
		for (var item : list)
		{
//			item = session.merge(item);
			session.update(item);
		}
		transaction.commit();
		transaction.begin();
	};

	public void reload()
	{
		reloadHandler.run();
		session.clear();
	}
	
	private Runnable reloadHandler;
	
	public void setHeader(String[] columns)
	{
		model.columns.clear();
		model.columns.add("№");
		model.columns.addAll(Arrays.asList(columns));
		model.isEditable = new boolean[model.columns.size()];
	}
	
	public void setItems(List vec) {
		model.items = vec;
		this.updateUI();
		getColumnModel().getColumn(0).setMaxWidth(getColumnModel().getColumn(0).getMinWidth() + 4);
	}
	
	public List<T> getItems()
	{
		return model.items;
	}
	
	public void setEditable(int column, boolean state)
	{
		model.isEditable[column] = state;
	}
	
	public T getSelectedItem()
	{
		var index = super.getSelectedRow();
		if (index < 0) return null;
		index = super.convertRowIndexToModel(index);
		return model.items.get(index);
	}
	
	public List<T> getSelectedItems() {
		int[] rows = getSelectedRows();
		List<T> arr = new ArrayList();
		for (var row : rows) {
			row = super.convertRowIndexToModel(row);
			arr.add(model.items.get(row));
		}
		return arr;
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
		private List<T> items = new ArrayList<>();
		private List<String> columns = new ArrayList<String>();
		private boolean[] isEditable;

		public int getColumnCount() {
			return columns.size();
		}

		public int getRowsCount() {
			return items.size();
		}

		public Object getValueAt(int row, int col) {
			if (col == 0) return row + 1;
			
			T book = items.get(row);

			Class myClass = book.getClass();
			Field[] fields = myClass.getDeclaredFields();
			Object value;
			try {
				value = fields[col - 1].get(book);
			} catch (IllegalAccessException e) {
				value = null;
			}
			return value;
		}

		public String getColumnName(int col) {
			return columns.get(col);
		}

		@Override
		public int getRowCount() {
			return items.size();
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 0) return Integer.class;
			if (items.isEmpty()) {
				return Object.class;
			}
			return getValueAt(0, columnIndex).getClass();
		}

		public boolean isCellEditable(int row, int col) {
			return isEditable[col];
		}

		public void setValueAt(Object value, int row, int col) {
			T book = items.get(row);
			Class myClass = book.getClass();
			Field[] fields = myClass.getDeclaredFields();
			Object value1;
			try {
				fields[col - 1].set(book, value);
			} catch (IllegalAccessException e) {
				try {
					fields[col - 1].set(book, null);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			catch (IllegalArgumentException e) {
				try {
					fields[col - 1].set(book, null);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
//			fireTableCellUpdated(row, col);
		}

	}

}
