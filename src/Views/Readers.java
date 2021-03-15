package Views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Query;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import Components.MyScrollPane;
import Components.MyTextField;
import Database.Book;
import Database.HibernateUtil;
import Database.Reader;
import Forms.ReaderDialog;
import Generic.GenericTable;
import Main.MainForm;

public class Readers extends Pane{
	SearchPanel searchPanel;
	GenericTable table;
	ViewMenu tableMenu;
	
	ReaderDialog readerDialog;
	
	public Readers() {
		init();
		readerDialog = new ReaderDialog();
	}

	public Readers(MainForm mainForm) {
		init();
		readerDialog = new ReaderDialog(mainForm);
	}
	
	private void init()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(createSearchPanel());
		add(new MyScrollPane(createTable()));
		add(createViewMenu());
	}

	private SearchPanel createSearchPanel() {
		searchPanel = new SearchPanel();
		searchPanel.setSimpleSearchBoxHint("Поиск читателей по ФИО");
		searchPanel.enableAdvancedSearch(false);

		searchPanel.simpleSearchRun = (text) -> {
			if (text.isEmpty())
			{
				table.setItems(HibernateUtil.loadAllData(Reader.class));
				return;
			}
			
			String[] words = text.split("\\s+");

			MessageFormat format = new MessageFormat("(reader.FIO LIKE {0})");
			String sql = "from Reader reader where " + Arrays.stream(words)
					 .map(word -> format.format(new Object[]{"\'%" + word + "%\'"}))
					 .collect(Collectors.joining(" AND "));
			System.err.println(sql);

			var session = HibernateUtil.getSessionFactory().openSession();			
			
			//CriteriaBuilder throws exception using LIKE
			Query query= session.createQuery(sql);
			List<Book> list = query.getResultList();

			table.setItems(list);
			
			session.close();
		};

		return searchPanel;
	}

	private GenericTable createTable() {
		table = new GenericTable<Reader>(Reader.class,
				new String[] { "Номер читательского билета", "ФИО", "Адрес", "Телефон", "Номер паспорта" });

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

		List<MyTextField> editors = new ArrayList<MyTextField>();
		editors.add(new MyTextField());
		editors.add(new MyTextField());
		editors.add(new MyTextField());
		editors.add(new MyTextField());
		editors.add(new MyTextField());
		editors.get(editors.size() - 1).setFormat("## ##  ######");
		editors.get(editors.size() - 2).setFormat("+#(###)###-##-##");
		//чтобы выводить номер билета в красивом формате с нуоликами
		table.getColumnModel().getColumn(1).setCellRenderer( new LibraryCardRenderer() );
		
		int i = 0;
		for (MyTextField editor : editors)
			table.getColumnModel().getColumn(++i).setCellEditor(new DefaultCellEditor(editor));


		table.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) {
	            if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
	            	readerDialog.show(table.getSelectedItem());
	            }
	        }
	    });		
		
		
		table.reload();

		return table;
	}
	
	
	public class LibraryCardRenderer extends DefaultTableCellRenderer {

		public void setValue(Object value) {
			super.setValue(String.format("%08d", value));
		}

	}
	

	private ViewMenu createViewMenu()
	{
		tableMenu = new ViewMenu();
		tableMenu.addReloadButtonListener(e -> table.reload());
		
		tableMenu.addSaveChangesButtonListener(e -> table.saveChanges());
		
		tableMenu.enableEdit.setSelected(Settings.ApplicationSettings.Configuration.enableEditReadersView);
		tableMenu.addEnableEditListener(e -> {
			boolean value = e.getStateChange() == ItemEvent.SELECTED;
			Settings.ApplicationSettings.Configuration.enableEditReadersView = value;
			Settings.ApplicationSettings.save();
			for(int index = 2; index < 2 + 5; index++)
				table.setEditable(index, value);
		});
		return tableMenu;
	}
}
