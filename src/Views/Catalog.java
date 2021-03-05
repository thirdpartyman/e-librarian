package Views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

import ComboBox.GenericComboBox;
import Components.GenericTable;
import Components.MyTextField;
import Database.Author;
import Database.BBK;
import Database.Book;
import Database.PublishHouse;
import SearchPanel.SearchPanel;

public class Catalog extends Pane {

	public Catalog() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(createSearchPanel());
		add(new JScrollPane(createTable()));
	}
	
	
	private SearchPanel createSearchPanel()
	{
		SearchPanel searchPanel = new SearchPanel();
		JTextField nameTextField = new JTextField(20);
		GenericComboBox bbkComboBox = new GenericComboBox<BBK>();
		GenericComboBox authorComboBox = new GenericComboBox<Author>();
		GenericComboBox publishHouseComboBox = new GenericComboBox<PublishHouse>();
		MyTextField releaseYear1 = new MyTextField();releaseYear1.setFormat("####", ' ');releaseYear1.setColumns(3);
		MyTextField releaseYear2 = new MyTextField();releaseYear2.setFormat("####", ' ');releaseYear2.setColumns(3);
//		bbkComboBox.setItems(new Vector(HibernateSessionFactoryUtil.loadAllData(BBK.class)));
//		authorComboBox.setItems(new Vector(HibernateSessionFactoryUtil.loadAllData(Author.class)));
//		publishHouseComboBox.setItems(new Vector(HibernateSessionFactoryUtil.loadAllData(PublishHouse.class)));
		
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
		
		
		searchPanel.advancedSearchRun = () ->{
			System.out.println(nameTextField.getText());
			System.out.println(authorComboBox.getSelectedItem());
			System.out.println(bbkComboBox.getSelectedItem());
			System.out.println(publishHouseComboBox.getSelectedItem());//null
			System.out.println(releaseYear1.getText());//empty
			System.out.println(releaseYear2.getText());
		};
		
		searchPanel.simpleSearchRun = (text) ->{
			text = text.replaceAll("\\s+", "%");
			System.err.println(text);
		};
		
		return searchPanel;
	}

	
	private GenericTable createTable()
	{
		GenericTable table = new GenericTable<Book>(new String[] { "ISBN", "Название", "ББК", "Автор", "Издательство", "Год выпуска" });
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
	    
//		List<Component> cmp = new ArrayList<Component>();
//		cmp.add(new MyTextField());
//		((MyTextField)cmp.get(cmp.size() - 1)).setFormat("#-###-#####-#");
//		cmp.add(new MyTextField());
//		cmp.add(new GenericComboBox<BBK>(new Vector(HibernateSessionFactoryUtil.loadAllData(BBK.class))));
//		cmp.add(new GenericComboBox<Author>(new Vector(HibernateSessionFactoryUtil.loadAllData(Author.class))));
//		cmp.add(new GenericComboBox<PublishHouse>(new Vector(HibernateSessionFactoryUtil.loadAllData(PublishHouse.class))));
//		cmp.add(new ReleaseYearSpinner());
//		
//		int i = 0;
//		for(; i != 2; i++)
//			table.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor((JTextField)cmp.get(i)));
//		for(; i != 2+3; i++)
//			table.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor((GenericComboBox)cmp.get(i)));
//		table.getColumnModel().getColumn(i).setCellEditor(new SpinnerEditor((ReleaseYearSpinner)cmp.get(i)));
//
//		table.setItems(HibernateSessionFactoryUtil.loadAllData(Book.class));
		
		
		return table;
	}
}
