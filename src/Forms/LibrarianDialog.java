package Forms;

import java.awt.Dimension;
import java.awt.Window;
import java.text.ParseException;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Components.MyDialog;
import Components.MyTextField;
import Components.Utils;
import Database.Librarian;

public class LibrarianDialog extends MyDialog {

	MyTextField fioField = new MyTextField();
	MyTextField adressField = new MyTextField();
	MyTextField phoneField = new MyTextField();
	JPasswordField passwordField1 = new JPasswordField();
	JPasswordField passwordField2 = new JPasswordField();
	
//	Long id;
//    String adress;
//	String phone;
//	String FIO;
//	String password;//хеш пароля

	public LibrarianDialog() {
		super();
		init();
	}
	
	public LibrarianDialog(Window parent) {
		super(parent);
		init();
	}
	
	private void init()
	{
		setModalityType(ModalityType.APPLICATION_MODAL);
		createLayout();// разметка

		ImageIcon icon = new ImageIcon("icons\\librarian (1).png");
		setIconImage(icon.getImage());
		
		saveTitle = "Регистрация";
		updateTitle = "Редактирование учетной записи";

		phoneField.setFormat("+#(###)###-##-##");
		
		passwordField1.setEchoChar('*');
		passwordField2.setEchoChar('*');
	}

	
	private void createLayout() {		
		// список полей ввода с подписями
		panel.addComponentWithLabel("ФИО", fioField);
		panel.addComponentWithLabel("Адрес", adressField);
		panel.addComponentWithLabel("Телефон", phoneField);
		panel.addComponentWithLabel("Пароль", passwordField1);
		panel.addComponentWithLabel("Подтвердите пароль", passwordField2);

		pack();
		setResizable(false);
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
		try {
			phoneField.formatter.stringToValue(phoneField.getText());
		} catch (ParseException e) {
			e.addSuppressed(new Exception("invalid phone"));
			throw e;
		}
		
		if (!Arrays.equals(passwordField1.getPassword(),passwordField2.getPassword()))
		{
			ParseException e = new ParseException("Password error", 0);
			e.addSuppressed(new Exception("passwords are not equal"));
			throw e;
		}
		
		Librarian librarian = (Librarian)this.object;
		librarian.FIO = fioField.getText().trim();
		librarian.adress = adressField.getText().trim();
		librarian.phone = phoneField.getText();
		librarian.password = Utils.hash(passwordField1.getPassword());
		
		Utils.print(librarian);
	}
	
	@Override
	protected void setInfo()
	{
		Librarian librarian = (Librarian)this.object;
		fioField.setText(librarian.FIO);
		adressField.setText(librarian.adress);
		phoneField.setText(librarian.phone);
		passwordField1.setText("");
		passwordField2.setText("");
	}


	@Override
	public void show() {
		super.show(Librarian.class);
	}

}