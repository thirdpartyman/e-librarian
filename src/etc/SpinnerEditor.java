package etc;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class SpinnerEditor extends DefaultCellEditor {
	private ReleaseYearSpinner spinner;

	public SpinnerEditor(ReleaseYearSpinner spinner) {
		super(new JTextField());
		this.spinner = spinner;
		spinner.setBorder(null);
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		spinner.setValue(value);
		return spinner;
	}

	public Object getCellEditorValue() {
		return spinner.getShort();
	}
}