package Forms;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import Components.MyGroupBox;
import Components.Utils;
import Database.HibernateUtil;
import Database.Librarian;
import Generic.GenericComboBox;

public class AuthorizationDialog {

	MyGroupBox panel = new MyGroupBox();
	GenericComboBox librarianComboBox = new GenericComboBox<Librarian>();
	JPasswordField passwordField = new JPasswordField();
	String[] options = new String[] { "Войти", "Выйти из приложения" };
	ImageIcon icon = new ImageIcon("icons\\librarian (1).png");

	public AuthorizationDialog() {
		librarianComboBox.setItems(new Vector(HibernateUtil.loadAllData(Librarian.class)));
		panel.addComponentWithLabel("User", librarianComboBox);
		panel.addComponentWithLabel("Password", passwordField);
	}

	public Librarian show() {
		int option = JOptionPane.showOptionDialog(null, panel, "Авторизация", JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, icon, options, null);

		if (option == 1) System.exit(0);
		
		if (option == 0 && librarianComboBox.getSelectedIndex() > -1) {
			var librarian = (Librarian) librarianComboBox.getSelectedItem();
			var password = Utils.hash(passwordField.getPassword());
			return librarian.password.equals(password) ? librarian : null;
		}
		return null;
	}

}
