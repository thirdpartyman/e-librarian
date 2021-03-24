package Forms;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import Components.MyDialog;
import Components.MyScrollPane;
import Components.MyTextField;
import Database.Reader;
import Util.Utils;
import Views.HistoryViewTable;

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

		createAssociatedFormularsTable();
		
		pack();
//		setResizable(false);
	}
	
	JPanel formularsPanel = new JPanel();
	HistoryViewTable table = new HistoryViewTable();
	private void createAssociatedFormularsTable()
	{
		formularsPanel.setLayout(new BoxLayout(formularsPanel, BoxLayout.Y_AXIS));
		getContentPane().add(formularsPanel, 1);
		JPanel header = new JPanel();
		header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
		header.setOpaque(false);
		header.add(Box.createHorizontalGlue());
		header.add(new JLabel("Взятые книги", new ImageIcon("icons\\backpack (1).png"), SwingConstants.LEADING )).setFont(new Font("Verdana", Font.BOLD, 14));
		JCheckBox checkBox = new JCheckBox();
		checkBox.setOpaque(false);
		checkBox.setIcon(new ImageIcon("icons\\shutterstock_366984632.png"));
		checkBox.setSelectedIcon(new ImageIcon("icons\\shutterstock_366984632 (1).png"));
		header.add(Box.createHorizontalGlue());
		header.add(checkBox);
		formularsPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		formularsPanel.add(header);
		formularsPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		var res = formularsPanel.add(new MyScrollPane(table));
		res.setVisible(false);
		header.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		header.addMouseListener(new MouseAdapter() {
			@Override
	        public void mouseClicked(MouseEvent e) {
				checkBox.setSelected(!checkBox.isSelected());
	         } 
		});
		checkBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
            	res.setVisible(e.getStateChange() == ItemEvent.SELECTED);
            	formularsPanel.updateUI();
            	table.setPreferredScrollableViewportSize(table.getPreferredSize());
            	table.setFillsViewportHeight(true);
            	Dimension size = getSize();
            	ReaderDialog.super.pack();
            	size.height = getHeight();
            	setSize(size);
            }
        });
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
		
		table.saveChanges();
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
		
		if (reader.takenBooks != null)
		{
			table.setItems(reader.takenBooks);
			formularsPanel.setVisible(!reader.takenBooks.isEmpty());
		}
		else
			formularsPanel.setVisible(false);
//		super.pack();
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