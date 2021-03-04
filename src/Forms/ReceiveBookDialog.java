package Forms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ComboBox.GenericComboBox;
import Database.Author;
import Database.BBK;
import Database.Book;
import Database.HibernateSessionFactoryUtil;
import Database.PublishHouse;
import etc.MyButton;
import etc.MyDialog;
import etc.MyGroupBox;
import etc.MyTextField;
import etc.ReleaseYearSpinner;

public class ReceiveBookDialog extends MyDialog {

	AuthorDialog authorDialog = new AuthorDialog(this);
	PublishHouseDialog publishHouseDialog = new PublishHouseDialog(this);
	
	MyTextField isbnTextField = new MyTextField();
	MyTextField nameTextField = new MyTextField();
	GenericComboBox bbkComboBox = new GenericComboBox<BBK>();
	GenericComboBox authorComboBox = new GenericComboBox<Author>();
	GenericComboBox publishHouseComboBox = new GenericComboBox<PublishHouse>();
	ReleaseYearSpinner releaseYearSpinner = new ReleaseYearSpinner();

	public ReceiveBookDialog(Frame parent) {
		super(parent, "Добавить книгу");
		setModalityType(ModalityType.MODELESS);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setLocationRelativeTo(getParent());
		createLayout();// разметка

		saveTitle = "Добавить запись о книге";
		updateTitle = "Изменить запись о книге";
		ImageIcon icon = new ImageIcon("icons\\book (4).png");
		setIconImage(icon.getImage());

		isbnTextField.setFormat("#-###-#####-#");

		bbkComboBox.setItems(new Vector(HibernateSessionFactoryUtil.loadAllData(BBK.class)));
		authorComboBox.setItems(new Vector(HibernateSessionFactoryUtil.loadAllData(Author.class)));
		publishHouseComboBox.setItems(new Vector(HibernateSessionFactoryUtil.loadAllData(PublishHouse.class)));
	}

	private void createLayout() {

		MyGroupBox panel = new MyGroupBox();
		Border border = panel.getBorder();
		Border margin = new EmptyBorder(10, 10, 10, 10);
		panel.setBorder(new CompoundBorder(margin, border));

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
					authorComboBox.setItems(new Vector(HibernateSessionFactoryUtil.loadAllData(Author.class)));
				}

				public void windowClosing(WindowEvent e) {
					authorComboBox.setItems(new Vector(HibernateSessionFactoryUtil.loadAllData(Author.class)));
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
					publishHouseComboBox.setItems(new Vector(HibernateSessionFactoryUtil.loadAllData(PublishHouse.class)));
				}

				public void windowClosing(WindowEvent e) {
					publishHouseComboBox.setItems(new Vector(HibernateSessionFactoryUtil.loadAllData(PublishHouse.class)));
				}
			});
			p.add(btn, BorderLayout.EAST);
			p.add(publishHouseComboBox);
			
			panel.addComponentWithLabel("Издательство", p);
		}
		
		panel.addComponentWithLabel("Год выпуска", releaseYearSpinner);

		this.getContentPane().add(panel);

		pack();
		Dimension size = getSize();
		size.width = (int) (size.height * 1.618);
		setMinimumSize(size);// минимальный размер не может быть меньше рассчитанного при создании диалога

	}

	
	@Override
	protected void getInfo()
	{
		Book book = (Book)this.object;
		book.ISBN = isbnTextField.getText();
		book.name = nameTextField.getText().trim();
		book.bbk = (BBK) bbkComboBox.getSelectedItem();
		book.author = (Author) authorComboBox.getSelectedItem();
		book.publishHouse = (PublishHouse) publishHouseComboBox.getSelectedItem();
		book.releaseYear = releaseYearSpinner.getShort();
		
		try {
			 StringWriter writer = new StringWriter();
			 ObjectMapper mapper = new ObjectMapper();			 
			 String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(book);
	         System.out.println(jsonInString);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		releaseYearSpinner.setValue(book.releaseYear == null ? (short)0 : book.releaseYear);
	}

	@Override
	public void show() {
		super.show(Book.class);
	}

}
