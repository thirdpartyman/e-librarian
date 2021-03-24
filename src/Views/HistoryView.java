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
import Database.Book;
import Database.Formular;
import Database.HibernateUtil;
import Database.Reader;
import Forms.ReturnBookDialog;
import Generic.GenericComboBox;
import Generic.GenericTable;
import Main.MainForm;
import Util.Utils;

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
//				instance.setTime(new Date());
				instance.roll(Calendar.DATE, -Settings.ApplicationSettings.Configuration.maxPeriodBookHolding);
				Date returnDate = instance.getTime();
				
				System.err.println(returnDate);

				params.add(cb.lessThan(root.get("issueDate"), returnDate ));
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
		table = new HistoryViewTable();

		table.reload();

		return table;
	}


	private ViewMenu createViewMenu() {
		tableMenu = new ViewMenu();
		tableMenu.addReloadButtonListener(e -> table.reload());

		tableMenu.addSaveChangesButtonListener(e -> table.saveChanges());

//		tableMenu.enableEdit.setSelected(Settings.ApplicationSettings.Configuration.enableEditReadersView);
		tableMenu.enableEdit.setVisible(false);
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
