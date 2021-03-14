package Main;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.UnsupportedLookAndFeelException;

import Database.HibernateUtil;
import Database.Librarian;
import Forms.LibrarianDialog;


public class Program {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		System.out.println("Hello, world!");
			
		SplashScreen splash = new SplashScreen();
    	MainForm form = new MainForm();

    	form.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowOpened(WindowEvent e) {
	        	splash.dispatchEvent(new WindowEvent(e.getWindow(), WindowEvent.WINDOW_CLOSING));
	        }
	    });
    	form.show();
	}
}