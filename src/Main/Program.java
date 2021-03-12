package Main;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.UnsupportedLookAndFeelException;

import org.hibernate.Session;
import org.hibernate.Transaction;

import Database.BBK;


public class Program {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		System.out.println("Hello, world!");
			
		SplashScreen splash = new SplashScreen();
    	MainForm form = new MainForm();

    	form.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowActivated(WindowEvent e) {
	        	splash.dispatchEvent(new WindowEvent(e.getWindow(), WindowEvent.WINDOW_CLOSING));
	        }
	    });
    	form.show();
	}
	
	
	
	


	
	static void resetBBKtable(Session session) throws IOException
	{
		Transaction transaction = session.beginTransaction();
		
		File file = new File("BBK");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        while (line != null) {
            var arr = line.split("\t");
            if (arr.length != 2)
            {  
            	System.err.println(arr[0]);
            	continue;
            }
            System.out.print(arr.length);
            String index = arr[0];
            String text = arr[1];
            BBK bbk = new BBK(index, text);
            session.save(bbk);
            System.out.println(index);

            line = reader.readLine();
        }
        System.out.println("!!!");
        transaction.commit();	

	}
}