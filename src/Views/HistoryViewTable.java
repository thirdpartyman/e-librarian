package Views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import Components.Utils;
import Database.Formular;
import Forms.ReturnBookDialog;
import Generic.GenericTable;

public class HistoryViewTable extends GenericTable<Formular> {

	ReturnBookDialog returnBookDialog = new ReturnBookDialog();

	
	public HistoryViewTable() {
		super(Formular.class, new String[] { "Читатель", "Книга", "Дата выдачи", "Дата возврата", "Библиотекарь" });
		init();
	}
	
	
	private void init()
	{
		Font font = new Font("Verdana", Font.PLAIN, 12);
		setFont(font);
		setRowHeight(30);
		setBackground(Color.orange);
//		setForeground(Color.white);

		JTableHeader header = getTableHeader();
		header.setPreferredSize(new Dimension(header.getPreferredSize().width, 32));
		getTableHeader().setOpaque(false);
		getTableHeader().setBackground(new Color(93, 163, 226));
		getTableHeader().setFont(new Font("Verdana", Font.BOLD, 12));

		for (int i = 0; i != getColumnCount(); i++)
			this.getColumnModel().getColumn(i).setCellRenderer(new FormularsTableRenderer());

		removeKeyListener(getKeyListeners()[0]);
		
		

		final JPopupMenu popup = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Открыть", new ImageIcon("icons/newproject.png"));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				returnBookDialog.show(getItems().get(rowAtPoint(popup.getLocation())));
			}
		});
		popup.add(menuItem);
		menuItem = new JMenuItem("Удалить", new ImageIcon("icons/newproject.png"));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeItems(getSelectedItems());
			}
		});
		popup.add(menuItem);
		menuItem = new JMenuItem("Книга возвращена", new ImageIcon("icons/newproject.png"));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(Formular item : getSelectedItems())	
					item.returnDate = new java.sql.Date(Utils.removeTime(new Date()).getTime());
				updateUI();
			}
		});
		popup.add(menuItem);

		
//		table.setComponentPopupMenu(popup);

		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
					returnBookDialog.show(getSelectedItem());
				else
					if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1)
					{
						int[] selectedRows = getSelectedRows();
						int currentRow = rowAtPoint(e.getPoint());
						if (!Arrays.stream(selectedRows).anyMatch(i -> i == currentRow))
							changeSelection(currentRow, currentRow, false, false);
						
						popup.show(getParent(), e.getX(), e.getY());
					}
			}
		});

	}
	
	
	public static class FormularsTableRenderer extends DefaultTableCellRenderer {

		public FormularsTableRenderer() {
			super.setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (isSelected) {
				renderer.setBackground(table.getSelectionBackground());
				return renderer;
			}

			if (table.getValueAt(row, 4) != null)
				renderer.setBackground(Color.LIGHT_GRAY);
			else {
				Date issueDate = (Date) table.getValueAt(row, 3);
				Calendar instance = Calendar.getInstance();
				instance.setTime(issueDate);
				instance.add(Calendar.DATE, Settings.ApplicationSettings.Configuration.maxPeriodBookHolding);
				Date returnDate = instance.getTime();

				Date nowDate = Utils.removeTime(new Date());

				renderer.setBackground((nowDate.after(returnDate)) ? 
						new Color(220, 73, 85) :	//красный - просрочен
						new Color(183, 229, 70)); 	//зеленый
			}

			return renderer;
		}

	}


}
