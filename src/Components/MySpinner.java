package Components;

import java.awt.EventQueue;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;

import javax.swing.AbstractSpinnerModel;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import ComboBox.Filterable;
import ComboBox.GenericComboBox;

public class MySpinner extends JSpinner {

	public MySpinner() {
		super();
		setModel(new SpinnerModel());
		((JSpinner.DefaultEditor) getEditor()).getTextField().setEditable(true);		
		setLeftAligment();
		enableSelectionOnClick();
	}

	private void setLeftAligment() {
		JComponent editor = this.getEditor();
		JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) editor;
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.LEFT);
	}

	private void enableSelectionOnClick() {
		JComponent editor = this.getEditor();
		JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) editor;
		spinnerEditor.getTextField().addFocusListener(new SpinnerSelectOnFocusGainedHandler());
	}

	// выделение текста JSpinner JTextComponent при установке фокуса
	private static class SpinnerSelectOnFocusGainedHandler extends FocusAdapter {
		@Override
		public void focusGained(FocusEvent e) {
			JTextComponent textComponent = (JTextComponent) e.getComponent();
			EventQueue.invokeLater(() -> textComponent.selectAll());
		}
	}
	
	public void setValue(Object obj)
	{
//		System.err.println("setValue()");
		((JSpinner.DefaultEditor) getEditor()).getTextField().setText((obj == null) ? "" : obj.toString());
		super.setValue(obj);
		
	}
	

	public Short getShort() {
		return ((Number) this.getModel().getValue()).get();
	}

	public class SpinnerModel extends AbstractSpinnerModel {

		private Number t = new Number(null);

		@Override
		public Object getValue() {
//			System.out.println("SpinnerModel getValue()");
			return t;
		}

		@Override
		public void setValue(Object o) {
			if (o == null) {
				t.set(new Number(null));
//				System.err.println(t.get() == null);
//				System.err.println("o == null");
				return;
			}
			if (o instanceof Short)
			{
				t.set((Short)o);
//				System.err.println("o instanceof Short");
				return;
			}
			t.set(Number.parseNumber(o.toString()));
			fireStateChanged();
		}

		@Override
		public Object getNextValue() {

			return t.add((short) 1);
		}

		@Override
		public Object getPreviousValue() {
			return t.add((short) -1);
		}
	}

	static class Number {

		private Short sec;

		public Number() {
			sec = 0;
		}

		public void set(Number number) {
			this.sec = number.sec;
		}

		public Number(Short sec) {
			this.sec = sec;
		}

		public void set(Short sec) {
			this.sec = sec;
		}

		public Short get() {
			return this.sec;
		}

		public Number add(Short n) {
			if (sec == null)
				sec = 0;
			sec = (short) (sec.intValue() + n);
			return this;
		}

		public Number add(Number n) {
			if (sec == null)
				sec = 0;
			sec = (short) (sec.intValue() + n.sec);
			return this;
		}

		@Override
		public String toString() {
			if (this.sec == null)
				return "";
			return sec.toString();
		}

		public static Number parseNumber(String s) {
			String regex = "(\\-)?[0-9]+";
			return new Number(s.matches(regex) ? Short.valueOf(s) : null);
		}
	}

}
