package Views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

import org.hibernate.Session;

import Components.MyScrollPane;
import Components.MySpinner;
import Components.MyTextField;
import Components.SpinnerEditor;
import Database.Author;
import Database.BBK;
import Database.Book;
import Database.HibernateUtil;
import Database.PublishHouse;
import Forms.ReceiveBookDialog;
import Generic.GenericComboBox;
import Generic.GenericTable;
import Main.MainForm;

public class Catalog extends Pane {
	SearchPanel searchPanel;
	GenericTable table;
	ViewMenu tableMenu;
	
	ReceiveBookDialog receiveBookDialog;
	
	public Catalog() {
		init();
		receiveBookDialog = new ReceiveBookDialog();
	}

	public Catalog(MainForm mainForm) {
		init();
		receiveBookDialog = new ReceiveBookDialog(mainForm);
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
		searchPanel.setSimpleSearchBoxHint("Поиск книг по названию и автору");
		JTextField nameTextField = new JTextField(20);
		GenericComboBox bbkComboBox = new GenericComboBox<BBK>();
		GenericComboBox authorComboBox = new GenericComboBox<Author>();
		GenericComboBox publishHouseComboBox = new GenericComboBox<PublishHouse>();
		MyTextField releaseYear1 = new MyTextField();
		releaseYear1.setFormat("####", ' ');
		releaseYear1.setColumns(3);
		MyTextField releaseYear2 = new MyTextField();
		releaseYear2.setFormat("####", ' ');
		releaseYear2.setColumns(3);
		bbkComboBox.setItems(new Vector(HibernateUtil.loadAllData(BBK.class)));
		authorComboBox.setItems(new Vector(HibernateUtil.loadAllData(Author.class)));
		publishHouseComboBox.setItems(new Vector(HibernateUtil.loadAllData(PublishHouse.class)));

		JPanel p;
		p = new JPanel();
		p.add(new JLabel("Название"));
		p.add(nameTextField);
		Border border = nameTextField.getBorder();
		Border margin = new EmptyBorder(3, 3, 3, 3);
		nameTextField.setBorder(new CompoundBorder(border, margin));
		searchPanel.add(p);
		p = new JPanel();
		p.add(new JLabel("Автор"));
		p.add(authorComboBox);
		searchPanel.add(p);
		p = new JPanel();
		p.add(new JLabel("ББК"));
		p.add(bbkComboBox);
		searchPanel.add(p);
		p = new JPanel();
		p.add(new JLabel("Издательство"));
		p.add(publishHouseComboBox);
		searchPanel.add(p);
		p = new JPanel();

		searchPanel.add(Box.createHorizontalGlue());
//		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(new JLabel("Год выпуска:"));
		p.add(releaseYear1);
		p.add(new JLabel("− "));
		p.add(releaseYear2);
		searchPanel.add(p);

		searchPanel.advancedSearchRun = () -> {
			System.out.println(nameTextField.getText());
			System.out.println(authorComboBox.getSelectedItem());
			System.out.println(bbkComboBox.getSelectedItem());
			System.out.println(publishHouseComboBox.getSelectedItem());// null
			System.out.println(releaseYear1.getText());// empty
			System.out.println(releaseYear2.getText());
			
						
			Session session = HibernateUtil.getSessionFactory().openSession();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Book> cr = cb.createQuery(Book.class);
			Root<Book> root = cr.from(Book.class);

			List<Predicate> params = new ArrayList<Predicate>();
			if (!nameTextField.getText().isEmpty())
				params.add(cb.like(root.get("name"), '%' + nameTextField.getText() + '%'));
			if (authorComboBox.getSelectedItem() != null)
				params.add(cb.equal(root.get("author"), authorComboBox.getSelectedItem()));
			if (bbkComboBox.getSelectedItem() != null)
				params.add(cb.equal(root.get("bbk"), bbkComboBox.getSelectedItem()));
			if (publishHouseComboBox.getSelectedItem() != null)
				params.add(cb.equal(root.get("publishHouse"), publishHouseComboBox.getSelectedItem()));
			if (!releaseYear1.getText().matches("^\\s*$"))
				params.add(cb.gt(root.get("releaseYear"), Integer.parseInt(releaseYear1.getText())));
			if (!releaseYear2.getText().matches("^\\s*$"))
				params.add(cb.le(root.get("releaseYear"), Integer.parseInt(releaseYear2.getText())));
			
			
            cr.select(root).where(params.toArray(new Predicate[] {}));

			Query query = session.createQuery(cr);
			List<Book> results = query.getResultList();
			
			table.setItems(results);
			
			session.close();	    
		};

		searchPanel.simpleSearchRun = (text) -> {
			if (text.isEmpty())
			{
				table.setItems(HibernateUtil.loadAllData(Book.class));
				return;
			}
			
			String[] words = text.split("\\s+");

			MessageFormat format = new MessageFormat("(book.name LIKE {0} OR book.author.name LIKE {0})");
			String sql = "from Book book where " + Arrays.stream(words)
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
		table = new GenericTable<Book>(Book.class,
				new String[] { "ISBN", "Название", "ББК", "Автор", "Издательство", "Год выпуска" });
//		table.setHeader(new String[] { "ISBN", "Название", "ББК", "Автор", "Издательство", "Год выпуска" });
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

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

		List<Component> cmp = new ArrayList<Component>();
		cmp.add(new MyTextField());
		((MyTextField) cmp.get(cmp.size() - 1)).setFormat("#-###-#####-#");
		cmp.add(new MyTextField());
		cmp.add(new GenericComboBox<BBK>(new Vector(HibernateUtil.loadAllData(BBK.class))));
		cmp.add(new GenericComboBox<Author>(new Vector(HibernateUtil.loadAllData(Author.class))));
		cmp.add(new GenericComboBox<PublishHouse>(new Vector(HibernateUtil.loadAllData(PublishHouse.class))));
		cmp.add(new MySpinner());

		int i = 0;
		for (; i != 2; i++)
			table.getColumnModel().getColumn(i + 1).setCellEditor(new DefaultCellEditor((MyTextField) cmp.get(i)));
		for (; i != 2 + 3; i++)
			table.getColumnModel().getColumn(i + 1).setCellEditor(new DefaultCellEditor((GenericComboBox) cmp.get(i)));
		table.getColumnModel().getColumn(i + 1).setCellEditor(new SpinnerEditor((MySpinner) cmp.get(i)));
		
//		table.getColumnModel().getColumn(6).setMaxWidth(table.getColumnModel().getColumn(6).getPreferredWidth() + 4);

		table.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) {
	            if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
	            	receiveBookDialog.show(table.getSelectedItem());
	            }
	        }
	    });		
		
		
		table.reload();

		return table;
	}
	

	private ViewMenu createViewMenu()
	{
		tableMenu = new ViewMenu();
		tableMenu.addReloadButtonListener(e -> table.reload());
		
		tableMenu.addSaveChangesButtonListener(e -> table.saveChanges());
		
		tableMenu.addEnableEditListener(e -> {
			boolean value = e.getStateChange() == ItemEvent.SELECTED;
			for(int index = 2; index < 2 + 5; index++)
				table.setEditable(index, value);
		});
		return tableMenu;
	}
}
