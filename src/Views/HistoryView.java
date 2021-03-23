package Views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.hibernate.Session;

import Components.MyScrollPane;
import Components.Utils;
import Database.Book;
import Database.Formular;
import Database.HibernateUtil;
import Database.Reader;
import Forms.ReturnBookDialog;
import Generic.GenericComboBox;
import Generic.GenericTable;
import Main.MainForm;

public class HistoryView extends Pane {
	SearchPanel searchPanel;
	GenericTable<Formular> table;
	ViewMenu tableMenu;

	ReturnBookDialog returnBookDialog;

	public HistoryView() {
		init();
		returnBookDialog = new ReturnBookDialog();
	}

	public HistoryView(MainForm mainForm) {
		init();
		returnBookDialog = new ReturnBookDialog(mainForm);
	}

	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(createSearchPanel());
		add(new MyScrollPane(createTable()));
		add(createViewMenu());
	}

	private SearchPanel createSearchPanel() {
		searchPanel = new SearchPanel();
		searchPanel.simpleSearchBox.setVisible(false);
		searchPanel.advancedSearchPanel.setVisible(true);

		GenericComboBox readerComboBox = new GenericComboBox<Reader>();
		GenericComboBox bookComboBox = new GenericComboBox<Book>();
		readerComboBox.setItems(new Vector(HibernateUtil.loadAllData(Reader.class)));
		bookComboBox.setItems(new Vector(HibernateUtil.loadAllData(Book.class)));

		ButtonGroup checkBoxGroup = new ButtonGroup();
		JRadioButton showAll = new JRadioButton("все", true);
		JRadioButton showIssued = new JRadioButton("выданные", false);
		JRadioButton showExpired = new JRadioButton("долги", false);
		showAll.setActionCommand("все");
		showIssued.setActionCommand("выданные");
		showExpired.setActionCommand("долги");
		checkBoxGroup.add(showAll);
		checkBoxGroup.add(showIssued);
		checkBoxGroup.add(showExpired);

		JPanel p;
		p = new JPanel();
		p.add(new JLabel("Читатель"));
		p.add(readerComboBox);
		searchPanel.add(p);
		p = new JPanel();
		p.add(new JLabel("Книга"));
		p.add(bookComboBox);
		searchPanel.add(p);

		searchPanel.add(Box.createVerticalGlue());

		searchPanel.add(showAll);
		searchPanel.add(showIssued);
		searchPanel.add(showExpired);

		searchPanel.advancedSearchRun = () -> {

			Session session = HibernateUtil.getSessionFactory().openSession();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Formular> cr = cb.createQuery(Formular.class);
			Root<Formular> root = cr.from(Formular.class);

			List<Predicate> params = new ArrayList<Predicate>();
			if (readerComboBox.getSelectedItem() != null)
				params.add(cb.equal(root.get("reader"), readerComboBox.getSelectedItem()));
			if (bookComboBox.getSelectedItem() != null)
				params.add(cb.equal(root.get("book"), bookComboBox.getSelectedItem()));
			
			switch(checkBoxGroup.getSelection().getActionCommand())
			{
			case "все":
				break;
			case "выданные":
				params.add(cb.isNull(root.get("returnDate")));
				break;
			case "долги":
				params.add(cb.isNull(root.get("returnDate")));
				
				Calendar instance = Calendar.getInstance();
				instance.setTime(Utils.removeTime(new Date()));
				instance.roll(Calendar.DATE, -Settings.ApplicationSettings.Configuration.maxPeriodBookHolding);
				Date returnDate = instance.getTime();

				params.add(cb.greaterThan(root.get("issueDate"), returnDate ));
				break;
			}

			cr.select(root).where(params.toArray(new Predicate[] {}));

			Query query = session.createQuery(cr);
			List<Formular> results = query.getResultList();

			table.setItems(results);

			session.close();
		};
		
		ItemListener itemListener = e -> searchPanel.advancedSearchRun.run();
		readerComboBox.addItemListener(itemListener);
		bookComboBox.addItemListener(itemListener);
		
		ActionListener listener = e -> searchPanel.advancedSearchRun.run();
		showAll.addActionListener(listener);
		showIssued.addActionListener(listener);
		showExpired.addActionListener(listener);

		return searchPanel;
	}

	private GenericTable createTable() {
		table = new GenericTable<Formular>(Formular.class,
				new String[] { "Читатель", "Книга", "Дата выдачи", "Дата возврата", "Библиотекарь" });

		Font font = new Font("Verdana", Font.PLAIN, 12);
		table.setFont(font);
		table.setRowHeight(30);
		table.setBackground(Color.orange);
//		table.setForeground(Color.white);

		JTableHeader header = table.getTableHeader();
		header.setPreferredSize(new Dimension(header.getPreferredSize().width, 32));
		table.getTableHeader().setOpaque(false);
		table.getTableHeader().setBackground(new Color(93, 163, 226));
		table.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 12));

		for (int i = 0; i != table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setCellRenderer(new FormularsTableRenderer());

		table.removeKeyListener(table.getKeyListeners()[0]);

		final JPopupMenu popup = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Открыть", new ImageIcon("icons/newproject.png"));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				returnBookDialog.show(table.getItems().get(table.rowAtPoint(popup.getLocation())));
			}
		});
		popup.add(menuItem);
		menuItem = new JMenuItem("Удалить", new ImageIcon("icons/newproject.png"));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.removeItems(table.getSelectedItems());
			}
		});
		popup.add(menuItem);
		menuItem = new JMenuItem("Книга возвращена", new ImageIcon("icons/newproject.png"));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(Formular item : table.getSelectedItems())	
					item.returnDate = new java.sql.Date(Utils.removeTime(new Date()).getTime());
				table.updateUI();
			}
		});
		popup.add(menuItem);

		
//		table.setComponentPopupMenu(popup);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
					returnBookDialog.show(table.getSelectedItem());
				else
					if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1)
					{
						int[] selectedRows = table.getSelectedRows();
						int currentRow = table.rowAtPoint(e.getPoint());
						if (!Arrays.stream(selectedRows).anyMatch(i -> i == currentRow))
							table.changeSelection(currentRow, currentRow, false, false);
						
						popup.show(table, e.getX(), e.getY());
					}
			}
		});

		table.reload();

		return table;
	}

	public static class FormularsTableRenderer extends JLabel implements TableCellRenderer {

		public FormularsTableRenderer() {
			super.setOpaque(true);
		}

		private static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			GenericTable genericTable = (GenericTable) table;

			Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);

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

				renderer.setBackground((returnDate.after(nowDate)) ? new Color(220, 73, 85) : new Color(183, 229, 70));
			}

			return renderer;
		}

	}

	private ViewMenu createViewMenu() {
		tableMenu = new ViewMenu();
		tableMenu.addReloadButtonListener(e -> table.reload());

		tableMenu.addSaveChangesButtonListener(e -> table.saveChanges());

//		tableMenu.enableEdit.setSelected(Settings.ApplicationSettings.Configuration.enableEditReadersView);
		tableMenu.enableEdit.setEnabled(false);
//		tableMenu.addEnableEditListener(e -> {
//			boolean value = e.getStateChange() == ItemEvent.SELECTED;
//			Settings.ApplicationSettings.Configuration.enableEditReadersView = value;
//			Settings.ApplicationSettings.save();
//			for(int index = 1; index < table.getColumnCount(); index++)
//				table.setEditable(index, value);
//		});
		return tableMenu;
	}
}
