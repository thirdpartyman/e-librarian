package Forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.jdatepicker.JDatePicker;

import Components.MyDialog;
import Database.Book;
import Database.Formular;
import Database.HibernateUtil;
import Database.Librarian;
import Database.Reader;
import Generic.DetailedComboBox;
import Util.Utils;

public class ReturnBookDialog extends MyDialog {

	DetailedComboBox readerComboBox = new DetailedComboBox(
			new String[] { "Читательски билет №", "ФИО", "Телефон", "Адрес", "Номер паспорта" },
			new int[] { 50, 100, 100, 100, 100 }, 1);
	DetailedComboBox librarianComboBox = new DetailedComboBox(new String[] { "ФИО", "Телефон", "Адрес" },
			new int[] { 150, 100, 100 }, 0);
	JDatePicker issueDateTimePicker = new JDatePicker();
	JDatePicker returnDateTimePicker = new JDatePicker();
	DetailedComboBox bookComboBox = new DetailedComboBox(
			new String[] { "ISBN", "Название", "ББК", "Автор", "Издательство", "Год выпуска" },
			new int[] { 100, 100, 100, 100, 100, 100 }, 
			(obj) -> {
				Book book = (Book)obj;
				return '\"' + book.name + '\"' + ", " + ((book.author != null) ? book.author.name : "");
			});


	public ReturnBookDialog() {
		super("Выдать книги");
		init();
	}

	public ReturnBookDialog(Window parent) {
		super(parent, "Выдать книги");
		init();
	}

	private void init() {
		createLayout();// разметка

		saveTitle = "Выдача книг читателю";
		ImageIcon icon = new ImageIcon("icons\\book (3).png");
		setIconImage(icon.getImage());
	
		statusBar.remove(1);
		librarianComboBox.setEnabled(false);
		readerComboBox.setEnabled(false);
		bookComboBox.setEnabled(false);
		issueDateTimePicker.setEnabled(false);
	}

	private void createLayout() {

		// список полей ввода с подписями
		panel.addComponentWithLabel("Читатель", readerComboBox);
		panel.addComponentWithLabel("Книга", bookComboBox);
		panel.addComponentWithLabel("Дата выдачи", issueDateTimePicker);
		panel.addComponentWithLabel("Дата возврата", returnDateTimePicker);
		panel.addComponentWithLabel("Кем выданы", librarianComboBox);

		pack();
//		setResizable(false);
	}

	@Override
	public void pack()
	{
		super.pack();
		Dimension size = getSize();
		size.width = (int)(size.height * 1.618);
		setSize(size);
	}

	@Override
	protected void getInfo() throws ParseException
	{	
		Formular formular = (Formular) this.object;
		
//		formular.reader = (Reader) readerComboBox.getValue();
//		formular.librarian = (Librarian) librarianComboBox.getValue();
//		formular.issueDate = ((Calendar) issueDateTimePicker.getModel().getValue()).getTime();
		formular.issueDate = ((Calendar) returnDateTimePicker.getModel().getValue()).getTime();
//		formular.book = (Book) bookComboBox.getValue();
	}
	
	@Override
	protected void setInfo()
	{
		Formular formular = (Formular) this.object;
		
		
		readerComboBox.setTableData(new Vector(HibernateUtil.loadAllData(Reader.class)));
		librarianComboBox.setTableData(new Vector(HibernateUtil.loadAllData(Librarian.class)));
		bookComboBox.setTableData(new Vector(HibernateUtil.loadAllData(Book.class)));
		
		readerComboBox.setValue(formular.reader);
		bookComboBox.setValue(formular.book);
		librarianComboBox.setValue(formular.librarian);
		

		Calendar cal = Calendar.getInstance();
		cal.setTime(formular.issueDate);
		var model = issueDateTimePicker.getModel();
		model.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		model.setSelected(true);

		boolean flag = formular.returnDate == null;
		returnDateTimePicker.setEnabled(flag);
		Date returnDate = flag ? new Date() : formular.returnDate;				
		{
			model = returnDateTimePicker.getModel();
			cal.setTime(returnDate);
			model.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			model.setSelected(true);
		}

	}


	@Override
	public void show() {
		super.show(Librarian.class);
	}
}
