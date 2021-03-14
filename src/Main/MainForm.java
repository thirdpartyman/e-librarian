package Main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import Components.MyButton;
import Components.Utils;
import Database.HibernateUtil;
import Database.Librarian;
import Forms.AuthorizationDialog;
import Forms.GetBookDialog;
import Forms.LibrarianDialog;
import Forms.ReaderDialog;
import Forms.ReceiveBookDialog;
import Settings.ApplicationSettings;
import Views.Catalog;
import Views.Readers;

public class MainForm extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2062760208030859011L;

	Catalog catalogView = new Catalog(this);
	Readers readersView = new Readers(this);

	ReceiveBookDialog receiveBookDialog = new ReceiveBookDialog(this);
	GetBookDialog getBookDialog = new GetBookDialog(this);
	ReaderDialog newReaderDialog = new ReaderDialog(this);

	private void createMenu() throws IOException {

		JPanel panes = new JPanel(new CardLayout());
		panes.setOpaque(false);
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBorder(BorderFactory.createEmptyBorder(5, 7, 5, 5));

		getContentPane().add(toolBar, BorderLayout.PAGE_START);
		getContentPane().add(panes);

		ArrayList<JToggleButton> views = new ArrayList<JToggleButton>();

		Dimension weight = new Dimension(5, 5);
		Dimension sz = new Dimension(100, 150);

//		Function<String, String> multiLine = (text) -> "<html>" + text.replaceAll(" ", "<br>") + "</html>";

		MyButton btn = null;
		JToggleButton tbtn = null;

		btn = (MyButton) toolBar
				.add(new MyButton(Utils.makeMultiLine("Выдача книг"), new ImageIcon("icons\\book (3).png")));
		btn.setMaximumSize(sz);
		btn.addActionListener(e -> getBookDialog.show());
		toolBar.addSeparator(weight);
		tbtn = (JToggleButton) toolBar.add(new JToggleButton("Каталог", new ImageIcon("icons\\001-books.png")));
		views.add(tbtn);
		panes.add(catalogView, tbtn.getText());
		toolBar.addSeparator(weight);
		tbtn = (JToggleButton) toolBar
				.add(new JToggleButton(Utils.makeMultiLine("История выдач"), new ImageIcon("icons\\books (1).png")));
		views.add(tbtn);
		panes.add(new JButton("История выдач"), tbtn.getText());
		toolBar.addSeparator(weight);
		tbtn = (JToggleButton) toolBar.add(new JToggleButton("Читатели", new ImageIcon("icons\\driving-license.png")));
		views.add(tbtn);
		panes.add(readersView, tbtn.getText());
		toolBar.addSeparator(weight);
		btn = (MyButton) toolBar
				.add(new MyButton(Utils.makeMultiLine("Новый читатель"), new ImageIcon("icons\\add-user.png")));
		btn.setMaximumSize(sz);
		btn.addActionListener(e -> newReaderDialog.show());
		toolBar.addSeparator(weight);
		btn = (MyButton) toolBar
				.add(new MyButton(Utils.makeMultiLine("Привоз книг"), new ImageIcon("icons\\book (4).png")));
		btn.setMaximumSize(sz);
		btn.addActionListener(e -> receiveBookDialog.show());
		toolBar.addSeparator(weight);
		toolBar.add(Box.createHorizontalGlue());
		btn = (MyButton) toolBar.add(new MyButton("Настройки", new ImageIcon("icons\\manual.png")));
		btn.addActionListener(e -> receiveBookDialog.show());

		for (JToggleButton button : views) {
			button.setMaximumSize(sz);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JToggleButton button = (JToggleButton) e.getSource();
					CardLayout layout = (CardLayout) (panes.getLayout());
					layout.show(panes, button.getText());

					if (button.isSelected()) {
						for (JToggleButton btn : views)
							if (btn != e.getSource())
								btn.setSelected(false);
					} else
						button.setSelected(true);
				}
			});

		}
	}

	public MainForm() throws IOException {
		super("Электронный Библиотекарь");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		createMenu();
		pack();
		setLocationRelativeTo(null);
		getContentPane().setBackground(Color.white);
		ImageIcon icon = new ImageIcon("icons\\online-library.png");
		setIconImage(icon.getImage());
	}

	// запуск авторизации при запуске программы и регистрации библиотекаря(если в бд нет ни одной учетной записи)
	@Override
	public void show() {
		super.show();
		if (HibernateUtil.loadAllData(Librarian.class).isEmpty()) {
			LibrarianDialog librarianDialog = new LibrarianDialog();
			librarianDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			librarianDialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					if (!HibernateUtil.loadAllData(Librarian.class).isEmpty())
						librarianDialog.dispose();
				}
			});
			librarianDialog.show();
		}

		AuthorizationDialog authorizationDialog = new AuthorizationDialog();
		do
		{
			ApplicationSettings.authorizedLibrarian = authorizationDialog.show();
		}while(ApplicationSettings.authorizedLibrarian == null);
	}

}