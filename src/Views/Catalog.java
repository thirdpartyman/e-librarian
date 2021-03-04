package Views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;

import ComboBox.GenericComboBox;
import Database.Author;
import Database.BBK;
import Database.Book;
import Database.HibernateSessionFactoryUtil;
import Database.PublishHouse;
import etc.GenericTable;
import etc.MyTextField;
import etc.ReleaseYearSpinner;
import etc.SpinnerEditor;

public class Catalog extends Pane {

	GenericTable table = new GenericTable<Book>(
			new String[] { "ISBN", "Название", "ББК", "Автор", "Издательство", "Год выпуска" });

	public Catalog() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

//		JTable table = new JTable(5, 5);
		Font font = new Font("Verdana", Font.PLAIN, 12);
		table.setFont(font);
		table.setRowHeight(30);
		table.setBackground(Color.orange);
//		table.setForeground(Color.white);
		
		
	    JTableHeader header = table.getTableHeader();
	    header.setPreferredSize(new Dimension(header.getPreferredSize().width, 32));
	    table.getTableHeader().setOpaque(false);
	    table.getTableHeader().setBackground(new Color(93, 163, 226));

		
		
		List<Component> cmp = new ArrayList<Component>();
		cmp.add(new MyTextField());
		((MyTextField)cmp.get(cmp.size() - 1)).setFormat("#-###-#####-#");
		cmp.add(new MyTextField());
		cmp.add(new GenericComboBox<BBK>(new Vector(HibernateSessionFactoryUtil.loadAllData(BBK.class))));
		cmp.add(new GenericComboBox<Author>(new Vector(HibernateSessionFactoryUtil.loadAllData(Author.class))));
		cmp.add(new GenericComboBox<PublishHouse>(new Vector(HibernateSessionFactoryUtil.loadAllData(PublishHouse.class))));
		cmp.add(new ReleaseYearSpinner());
		
		ReleaseYearSpinner releaseYearSpinner = new ReleaseYearSpinner();
		
		int i = 0;
		for(; i != 2; i++)
			table.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor((JTextField)cmp.get(i)));
		for(; i != 2+3; i++)
			table.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor((GenericComboBox)cmp.get(i)));
		table.getColumnModel().getColumn(i).setCellEditor(new SpinnerEditor((ReleaseYearSpinner)cmp.get(i)));

		add(new JScrollPane(table));
		
		table.setItems(HibernateSessionFactoryUtil.loadAllData(Book.class));
	}

}
