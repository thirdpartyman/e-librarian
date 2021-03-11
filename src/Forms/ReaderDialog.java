package Forms;

import java.awt.Dimension;
import java.awt.Window;
import java.text.ParseException;

import javax.swing.ImageIcon;
import javax.swing.JSpinner;

import Components.MyDialog;
import Components.MySpinner;
import Components.MyTextField;
import Components.Utils;
import Database.Author;
import Database.HibernateUtil;
import Database.Reader;

public class ReaderDialog extends MyDialog {

	MyTextField libraryCardNumberSpinner = new MyTextField();
	MyTextField fioField = new MyTextField();
	MyTextField adressField = new MyTextField();
	MyTextField phoneField = new MyTextField();
	MyTextField passportField = new MyTextField();

	public ReaderDialog() {
		super();
		init();
	}
	
	public ReaderDialog(Window parent) {
		super(parent);
		init();
	}
	
	private void init()
	{
		createLayout();// разметка

		ImageIcon icon = new ImageIcon("icons\\add-user.png");
		setIconImage(icon.getImage());
		
		saveTitle = "Добавить учетную запись читателя";
		updateTitle = "Изменить учетную запись читателя";

		phoneField.setFormat("+#(###)###-##-##");
		passportField.setFormat("## ##  ######");
//		((JSpinner.DefaultEditor)libraryCardNumberSpinner.getEditor()).getTextField().setEditable(false);
		libraryCardNumberSpinner.setEditable(false);
		
		super.onInsert = ()->{
			libraryCardNumberSpinner.setText(String.format("%08d", ((Reader)object).libraryCardNumber));
			var comps = panel.getComponents();
			comps[0].setVisible(true);
			comps[1].setVisible(true);
			pack();
		};
	}

	
	private void createLayout() {		
		// список полей ввода с подписями
		panel.addComponentWithLabel(Utils.makeMultiLine("Номер читательского билета"), libraryCardNumberSpinner);
		panel.addComponentWithLabel("ФИО", fioField);
		panel.addComponentWithLabel("Адрес", adressField);
		panel.addComponentWithLabel("Телефон", phoneField);
		panel.addComponentWithLabel("Номер паспорта", passportField);

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
			System.out.println(phoneField.formatter.stringToValue(phoneField.getText()));
		} catch (ParseException e) {
			e.addSuppressed(new Exception("invalid phone"));
			throw e;
		}
		
		try {
			System.out.println(passportField.formatter.stringToValue(passportField.getText()));
		} catch (ParseException e) {
			e.addSuppressed(new Exception("invalid passport"));
			throw e;
		}
		
		Reader reader = (Reader)this.object;
//		reader.libraryCardNumber = libraryCardNumberSpinner.getValue();
		reader.FIO = fioField.getText().trim();
		reader.adress = adressField.getText().trim();
		reader.phone = phoneField.getText();
		reader.passport = passportField.getText();
		
		Utils.print(reader);
	}
	
	@Override
	protected void setInfo()
	{
		Reader reader = (Reader)this.object;
		libraryCardNumberSpinner.setText(String.format("%08d", ((Reader)object).libraryCardNumber));
//		libraryCardNumberSpinner.setValue(reader.libraryCardNumber);
		fioField.setText(reader.FIO);
		adressField.setText(reader.adress);
		phoneField.setText(reader.phone);
		passportField.setText(reader.passport);
	}


	@Override
	public void show() {
		super.show(Reader.class);
		var comps = panel.getComponents();
		comps[0].setVisible(false);
		comps[1].setVisible(false);
		pack();
	}

}