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

public class GetBookDialog extends MyDialog {

	DetailedComboBox readerComboBox = new DetailedComboBox(
			new String[] { "Читательски билет №", "ФИО", "Телефон", "Адрес", "Номер паспорта" },
			new int[] { 50, 100, 100, 100, 100 }, 1);
	DetailedComboBox librarianComboBox = new DetailedComboBox(new String[] { "ФИО", "Телефон", "Адрес" },
			new int[] { 150, 100, 100 }, 0);
	JDatePicker dateTimePicker = new JDatePicker();
	List<DetailedComboBox> bookComboBoxes = new ArrayList<DetailedComboBox>();
	JPanel booksPanel = new JPanel();


	public GetBookDialog() {
		super("Выдать книги");
		init();
	}

	public GetBookDialog(Window parent) {
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
		dateTimePicker.setEnabled(false);
	}

	private void createLayout() {

		// список полей ввода с подписями
		panel.addComponentWithLabel("Читатель", readerComboBox);

		GridBagConstraints gridBagConstraint = new GridBagConstraints();
		gridBagConstraint.insets = new Insets(0, 0, 2, 0);
		gridBagConstraint.gridx = 0;
		gridBagConstraint.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraint.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraint.weightx = 1.0f;
		panel.add(new JLabel("Книги:"), gridBagConstraint);
		gridBagConstraint.insets = new Insets(0, 0, 5, -4);
		booksPanel.setLayout(new BoxLayout(booksPanel, BoxLayout.Y_AXIS));
		panel.add(booksPanel, gridBagConstraint);
		addNewBookComboBox();

		panel.addComponentWithLabel("Дата выдачи", dateTimePicker);
		panel.addComponentWithLabel("Кем выданы", librarianComboBox);

		pack();
//		setResizable(false);
		
		super.object = new ArrayList<Formular>();
	}

	@Override
	public void pack()
	{
		super.pack();
		Dimension size = getSize();
		size.width = (int) (size.height * 1.618);
		setSize(size);
	}

	private void addNewBookComboBox() {
		DetailedComboBox bookComboBox = new DetailedComboBox(
				new String[] { "ISBN", "Название", "ББК", "Автор", "Издательство", "Год выпуска" },
				new int[] { 100, 100, 100, 100, 100, 100 }, 
				(obj) -> {
					Book book = (Book)obj;
					return '\"' + book.name + '\"' + ", " + ((book.author != null) ? book.author.name : "");
				});
		bookComboBox.setTableData(new Vector(HibernateUtil.loadAllData(Book.class)));
		bookComboBox.setSelectedIndex(-1);
		bookComboBoxes.add(bookComboBox);

		JCheckBox btn = new JCheckBox();
		btn.setSelectedIcon(new ImageIcon("icons\\minus.png"));
		btn.setIcon(new ImageIcon("icons\\add.png"));
		btn.setBackground(Color.white);
		btn.setFocusPainted(false);

		JPanel p = new JPanel(new BorderLayout());
		p.add(btn, BorderLayout.EAST);
		p.add(bookComboBox);
		booksPanel.add(p);

		btn.addItemListener(e -> {
			boolean selected = e.getStateChange() == ItemEvent.SELECTED;
			if (selected)
				addNewBookComboBox();
			else {
				booksPanel.remove(p);
				bookComboBoxes.remove(bookComboBox);
			}
			
			Dimension size = getSize();
			size.height += booksPanel.getComponents()[0].getHeight() * ((selected) ? 1 : -1);
			setSize(size);
			booksPanel.updateUI();
//			pack();
		});

	};

	@Override
	protected void getInfo() throws ParseException {
		List<Formular> formulars = (List<Formular>) object;
		formulars.clear();
		for (var bookComboBox : bookComboBoxes)
		{
			Formular formular = new Formular();
			formular.reader = (Reader) readerComboBox.getValue();
			formular.librarian = (Librarian) librarianComboBox.getValue();
			formular.issueDate = ((Calendar) dateTimePicker.getModel().getValue()).getTime();
			formular.book = (Book) bookComboBox.getValue();
			formulars.add(formular);
		}
	}

	@Override
	protected void setInfo() {
		readerComboBox.setTableData(new Vector(HibernateUtil.loadAllData(Reader.class)));
		librarianComboBox.setTableData(new Vector(HibernateUtil.loadAllData(Librarian.class)));
		
		readerComboBox.setSelectedIndex(-1);
//		librarianComboBox.setSelectedIndex(-1);
		librarianComboBox.setValue(Settings.ApplicationSettings.authorizedLibrarian);

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		var model = dateTimePicker.getModel();
		model.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		model.setSelected(true);

		
		bookComboBoxes.clear();
		booksPanel.removeAll();
		addNewBookComboBox();
		List<Formular> formulars = (List<Formular>) object;
		formulars.clear();
	}

	@Override
	public void show() {
		super.show(ArrayList.class);		
		pack();
	}

	public void show(Reader reader) {
		super.show(ArrayList.class);
		readerComboBox.setSelectedItem(reader);		
		pack();
	}


	@Override
	protected void insert(Object object)
	{
		List<Formular> formulars = (List<Formular>) object;
		Utils.print(formulars);
		HibernateUtil.insert(formulars);
	}

}
