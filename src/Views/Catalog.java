package Views;

import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.fasterxml.jackson.databind.ObjectMapper;

import Database.Book;
import Database.HibernateSessionFactoryUtil;

public class Catalog extends Pane {

	Table table = new Table<Book>(Book.class, new String[] { "ISBN", "Название", "ББК", "Автор", "Издательство", "Год выпуска" });

	public Catalog() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		add(new JScrollPane(table));
	}

	class Table<T> extends JTable {
		TableModel model = new TableModel();

		Table(Class<T> clazz, String[] columns) {
			model.columns = columns;
			setModel(model);
			setAutoCreateRowSorter(true);
//			setDefaultRenderer(String.class, new TableCellRenderer());
			model.items = HibernateSessionFactoryUtil.loadAllData(clazz);

			
//			System.out.println(model.items.size());
//			getColumnModel().getColumn(0).setMaxWidth(100);
//			getColumnModel().getColumn(0).setPreferredWidth(100);
//			getColumnModel().getColumn(2).setMaxWidth(150);
//			getColumnModel().getColumn(2).setPreferredWidth(150);

//			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//			centerRenderer.setHorizontalAlignment( JLabel.CENTER );
//			getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
//			getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
		}



		private class TableCellRenderer extends DefaultTableCellRenderer {

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				c.setBackground(Color.white);
				return c;
			}
		}

		class TableModel extends AbstractTableModel {
			public List<T> items = new ArrayList<>();
			private String[] columns;

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

		}

	}
}
