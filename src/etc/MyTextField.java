package etc;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

public class MyTextField extends JFormattedTextField {

	public MyTextField() {
		enableSelectionOnClick();
	}

	public MyTextField(String text) {
		super(text);
		enableSelectionOnClick();
	}

	public MyTextField(int columns) {
		super(columns);
		enableSelectionOnClick();
	}

	public void setFormat(String s)
	{
	    try {
		    MaskFormatter formatter = new MaskFormatter(s);
	        formatter.setPlaceholderCharacter('_');
	        formatter.install(this);
	    } catch (java.text.ParseException exc) {
	        System.err.println("formatter is bad: " + exc.getMessage());
	        System.exit(-1);
	    }
	}

	private void enableSelectionOnClick() {
		this.addFocusListener(new TextFieldSelectOnFocusGainedHandler());
	}

	// выделение текста JTextField при установке фокуса
	private static class TextFieldSelectOnFocusGainedHandler extends FocusAdapter {
		@Override
		public void focusGained(FocusEvent e) {
			((JTextField) e.getSource()).selectAll();
		}
	}
}
