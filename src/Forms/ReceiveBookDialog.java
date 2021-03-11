package Forms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.util.Vector;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.hibernate.Transaction;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;

import ComboBox.GenericComboBox;
import Components.MyButton;
import Components.MyDialog;
import Components.MySpinner;
import Components.MyTextField;
import Database.Author;
import Database.BBK;
import Database.Book;
import Database.HibernateUtil;
import Database.PublishHouse;

public class ReceiveBookDialog extends MyDialog {

	AuthorDialog authorDialog = new AuthorDialog(this);
	PublishHouseDialog publishHouseDialog = new PublishHouseDialog(this);
	
	MyTextField isbnTextField = new MyTextField();
	MyTextField nameTextField = new MyTextField();
	GenericComboBox bbkComboBox = new GenericComboBox<BBK>();
	GenericComboBox authorComboBox = new GenericComboBox<Author>();
	GenericComboBox publishHouseComboBox = new GenericComboBox<PublishHouse>();
	MySpinner releaseYearSpinner = new MySpinner();

	public ReceiveBookDialog() {
		super("Добавить книгу");
		init();
	}
	
	public ReceiveBookDialog(Window parent) {
		super(parent, "Добавить книгу");
		init();
	}
	
	private void init()
	{
		createLayout();// разметка

		saveTitle = "Добавить запись о книге";
		updateTitle = "Изменить запись о книге";
		ImageIcon icon = new ImageIcon("icons\\book (4).png");
		setIconImage(icon.getImage());

		isbnTextField.setFormat("#-###-#####-#");

		bbkComboBox.setItems(new Vector(HibernateUtil.loadAllData(BBK.class)));
		authorComboBox.setItems(new Vector(HibernateUtil.loadAllData(Author.class)));
		publishHouseComboBox.setItems(new Vector(HibernateUtil.loadAllData(PublishHouse.class)));

	}

	private void createLayout() {


		// список полей ввода с подписями
		panel.addComponentWithLabel("ISBN", isbnTextField);
		panel.addComponentWithLabel("Название", nameTextField);
		panel.addComponentWithLabel("ББК", bbkComboBox);

		
		{
			JPanel p = new JPanel();
			p.setLayout(new BorderLayout());
			MyButton btn = new MyButton("∙∙∙");
			btn.addActionListener(e -> authorDialog.show());
			authorDialog.addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent e) {
					authorComboBox.setItems(new Vector(HibernateUtil.loadAllData(Author.class)));
				}

				public void windowClosing(WindowEvent e) {
					authorComboBox.setItems(new Vector(HibernateUtil.loadAllData(Author.class)));
				}
			});
			p.add(btn, BorderLayout.EAST);
			p.add(authorComboBox);
			
			panel.addComponentWithLabel("Автор", p);
		}

		
		{
			JPanel p = new JPanel();
			p.setLayout(new BorderLayout());
			MyButton btn = new MyButton("∙∙∙");
			btn.addActionListener(e -> publishHouseDialog.show());
			publishHouseDialog.addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent e) {
					publishHouseComboBox.setItems(new Vector(HibernateUtil.loadAllData(PublishHouse.class)));
				}

				public void windowClosing(WindowEvent e) {
					publishHouseComboBox.setItems(new Vector(HibernateUtil.loadAllData(PublishHouse.class)));
				}
			});
			p.add(btn, BorderLayout.EAST);
			p.add(publishHouseComboBox);
			
			panel.addComponentWithLabel("Издательство", p);
		}
		
		panel.addComponentWithLabel("Год выпуска", releaseYearSpinner);

		pack();
		Dimension size = getSize();
		size.width = (int) (size.height * 1.618);
		setMinimumSize(size);// минимальный размер не может быть меньше рассчитанного при создании диалога

	}

	
	@Override
	protected void getInfo() throws ParseException
	{
		try {
			System.out.println(isbnTextField.formatter.stringToValue(isbnTextField.getText()));
		} catch (ParseException e) {
			e.addSuppressed(new Exception("invalid ISBN"));
			throw e;
		}
		
		Book book = (Book)this.object;
		book.ISBN = isbnTextField.getText();
		book.name = nameTextField.getText().trim();
		book.bbk = (BBK) bbkComboBox.getSelectedItem();
		book.author = (Author) authorComboBox.getSelectedItem();
		book.publishHouse = (PublishHouse) publishHouseComboBox.getSelectedItem();
		book.releaseYear = releaseYearSpinner.getShort();
		
		System.err.println(book.releaseYear);
		
//		Utils.print(book);
	}
	
	@Override
	protected void setInfo()
	{
		Book book = (Book)this.object;
		isbnTextField.setText(book.ISBN);
		nameTextField.setText(book.name);
		bbkComboBox.setSelectedItem(book.bbk);
		authorComboBox.setSelectedItem(book.author);
		publishHouseComboBox.setSelectedItem(book.publishHouse);
		releaseYearSpinner.setValue(book.releaseYear);
	}

	@Override
	public void show() {
		super.show(Book.class);
		old_isbn = null;
	}
	
	public void show(Book book) {
		super.show(book);
		old_isbn = ((Book)book).ISBN;
	}
	String old_isbn = null;

	@Override
	protected void update(Object object)
	{
		Book book = (Book) object;
		var session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		if (old_isbn != null && !old_isbn.equals(book.ISBN))
			session.delete(session.get(Book.class, old_isbn));
		session.saveOrUpdate(object);
		transaction.commit();
		session.close();
	}
	
}
