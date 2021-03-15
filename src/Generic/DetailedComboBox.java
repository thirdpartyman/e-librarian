package Generic;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class DetailedComboBox extends JComboBox {
	public static enum Alignment {
		LEFT, RIGHT
	}

	private List<Object> tableData;
	private String[] columnNames;
	private int[] columnWidths;
	private Integer displayColumn;
	UnaryOperator<Object> displayString = null;
	private Alignment popupAlignment = Alignment.LEFT;

	/**
	 * Construct a TableComboBox object
	 */
	public DetailedComboBox(String[] colNames, int[] colWidths, Integer displayColumnIndex) {
		super();
		this.columnNames = colNames;
		this.columnWidths = colWidths;
		this.displayColumn = displayColumnIndex;

		setUI(new TableComboBoxUI());
		setEditable(false);
	}

	public DetailedComboBox(String[] colNames, int[] colWidths, UnaryOperator<Object> displayString) {
		this(colNames, colWidths, 0);
		this.displayColumn = null;
		this.displayString = displayString;

		setUI(new TableComboBoxUI());
		setEditable(false);
	}

	/**
	 * Set the type of alignment for the popup table
	 */
	public void setPopupAlignment(Alignment alignment) {
		popupAlignment = alignment;
	}

	/**
	 * Populate the combobox and drop-down table with the supplied data. If the
	 * supplied List is non-null and non-empty, it is assumed that the data is a
	 * List of Lists to be used for the drop-down table. The combobox is also
	 * populated with the column data from the column defined by
	 * <code>displayColumn</code>.
	 */
	public void setTableData(List<Object> tableData) {
		this.tableData = (tableData == null ? new ArrayList<Object>() : tableData);

		// even though the incoming data is for the table, we must also
		// populate the combobox's data, so first clear the previous list.
		removeAllItems();

		// then load the combobox with data from the appropriate column
		Iterator<Object> iter = this.tableData.iterator();
		while (iter.hasNext()) {
			Object rowData = iter.next();
			var row = rowData.getClass().getFields();
			try {
				addItem((displayColumn != null) ? row[displayColumn].get(rowData) : displayString.apply(rowData));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public Object getValue() {
		int index = getSelectedIndex();
		return index == -1 ? null : tableData.get(getSelectedIndex());
	}

	/**
	 * The handler for the combobox's components
	 */
	private class TableComboBoxUI extends MetalComboBoxUI {
		/**
		 * Create a popup component for the ComboBox
		 */
		@Override
		protected ComboPopup createPopup() {
			return new TableComboPopup(comboBox, this);
		}

		/**
		 * Return the JList component
		 */
		public JList getList() {
			return listBox;
		}
	}

	/**
	 * The drop-down of the combobox, which is a JTable instead of a JList.
	 */
	private class TableComboPopup extends BasicComboPopup implements ListSelectionListener, ItemListener {
		private final JTable table;

		private TableComboBoxUI comboBoxUI;
		private PopupTableModel tableModel;
		private JScrollPane scroll;
//    private JList list = new JList();
//    private ListSelectionListener selectionListener;
//    private ItemListener itemListener;

		/**
		 * Construct a popup component that's a table
		 */
		public TableComboPopup(JComboBox combo, TableComboBoxUI ui) {
			super(combo);
			this.comboBoxUI = ui;

			tableModel = new PopupTableModel();
			table = new JTable(new PopupTableModel()) {
				// Implement table cell tool tips.
				public String getToolTipText(MouseEvent e) {
					String tip = null;
					java.awt.Point p = e.getPoint();
					int rowIndex = rowAtPoint(p);
					int colIndex = columnAtPoint(p);
					int realColumnIndex = this.convertColumnIndexToModel(colIndex);

					if (realColumnIndex == 2) { // Sport column
						tip = "This person's favorite sport to " + "participate in is: "
								+ getValueAt(rowIndex, colIndex);
					} else if (realColumnIndex == 4) { // Veggie column
						TableModel model = getModel();
						String firstName = (String) model.getValueAt(rowIndex, 0);
						String lastName = (String) model.getValueAt(rowIndex, 1);
						Boolean veggie = (Boolean) model.getValueAt(rowIndex, 4);
						if (Boolean.TRUE.equals(veggie)) {
							tip = firstName + " " + lastName + " is a vegetarian";
						} else {
							tip = firstName + " " + lastName + " is not a vegetarian";
						}
					} else {
						// You can omit this part if you know you don't
						// have any renderers that supply their own tool
						// tips.
						tip = super.getToolTipText(e);
					}
					return tip;
				}

				// Implement table header tool tips.
				protected JTableHeader createDefaultTableHeader() {
					return new JTableHeader(columnModel) {
						public String getToolTipText(MouseEvent e) {
							String tip = null;
							java.awt.Point p = e.getPoint();
							int index = columnModel.getColumnIndexAtX(p.x);
							int realIndex = columnModel.getColumn(index).getModelIndex();
							return columnNames[realIndex];
						}
					};
				}
			};
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.getTableHeader().setReorderingAllowed(false);

			TableColumnModel tableColumnModel = table.getColumnModel();
			tableColumnModel.setColumnSelectionAllowed(false);

			for (int index = 0; index < table.getColumnCount(); index++) {
				TableColumn tableColumn = tableColumnModel.getColumn(index);
				tableColumn.setPreferredWidth(columnWidths[index]);
			}

			scroll = new JScrollPane(table);
			scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

			ListSelectionModel selectionModel = table.getSelectionModel();
			selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			selectionModel.addListSelectionListener(this);
			combo.addItemListener(this);

			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent event) {
					Point p = event.getPoint();
					int row = table.rowAtPoint(p);

					comboBox.setSelectedIndex(row);
					hide();
				}
			});

			table.setBackground(UIManager.getColor("ComboBox.listBackground"));
			table.setForeground(UIManager.getColor("ComboBox.listForeground"));
		}

		/**
		 * This method is overridden from BasicComboPopup
		 */
		@Override
		public void show() {
			if (isEnabled()) {
				super.removeAll();

				int scrollWidth = table.getPreferredSize().width
						+ ((Integer) UIManager.get("ScrollBar.width")).intValue() + 1;
				int scrollHeight = comboBoxUI.getList().getPreferredScrollableViewportSize().height;
				scroll.setPreferredSize(new Dimension(scrollWidth, scrollHeight));

				super.add(scroll);

				ListSelectionModel selectionModel = table.getSelectionModel();
				selectionModel.removeListSelectionListener(this);
				selectRow();
				selectionModel.addListSelectionListener(this);

				int scrollX = 0;
				int scrollY = comboBox.getBounds().height;

				if (popupAlignment == Alignment.RIGHT) {
					scrollX = comboBox.getBounds().width - scrollWidth;
				}

				show(comboBox, scrollX, scrollY);
			}
		}

		/**
		 * Implemention of ListSelectionListener
		 */
		public void valueChanged(ListSelectionEvent event) {
			comboBox.setSelectedIndex(table.getSelectedRow());
		}

		/**
		 * Implemention of ItemListener
		 */
		public void itemStateChanged(ItemEvent event) {
			if (event.getStateChange() != ItemEvent.DESELECTED) {
				ListSelectionModel selectionModel = table.getSelectionModel();
				selectionModel.removeListSelectionListener(this);
				selectRow();
				selectionModel.addListSelectionListener(this);
			}
		}

		/**
		 * Sync the selected row of the table with the selected row of the combo.
		 */
		private void selectRow() {
			int index = comboBox.getSelectedIndex();

			if (index != -1) {
				table.setRowSelectionInterval(index, index);
				table.scrollRectToVisible(table.getCellRect(index, 0, true));
			}
		}
	}

	/**
	 * A model for the popup table's data
	 */
	private class PopupTableModel extends AbstractTableModel {
		/**
		 * Return the # of columns in the drop-down table
		 */
		public int getColumnCount() {
			return columnNames.length;
		}

		/**
		 * Return the # of rows in the drop-down table
		 */
		public int getRowCount() {
			return tableData == null ? 0 : tableData.size();
		}

		/**
		 * Determine the value for a given cell
		 */
		public Object getValueAt(int row, int col) {
			if (tableData == null || tableData.size() == 0) {
				return "";
			}

			try {
				return tableData.get(row).getClass().getFields()[col].get(tableData.get(row));
			} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}

		/**
		 * All cells in the drop-down table are uneditable
		 */
		@Override
		public boolean isCellEditable(int row, int col) {
			return false;
		}

		/**
		 * Pull the column names out of the tableInfo object for the header
		 */
		@Override
		public String getColumnName(int column) {
			String columnName = null;

			if (column >= 0 && column < columnNames.length) {
				columnName = columnNames[column].toString();
			}

			return (columnName == null) ? super.getColumnName(column) : columnName;
		}
	}
}