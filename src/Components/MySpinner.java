package Components;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.text.JTextComponent;


public class MySpinner extends JSpinner {

	public MySpinner(SpinnerModel model) {
		super(model);
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
}
