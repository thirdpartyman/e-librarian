
package Components;

import java.awt.Component;
import java.text.ParseException;

import javax.swing.DefaultCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

public class SpinnerEditor extends DefaultCellEditor implements CellEditorListener {
	private MySpinner spinner;

	public SpinnerEditor(MySpinner spinner) {
		super(new JTextField());
		this.spinner = spinner;
		this.spinner.setBorder(null);
		this.addCellEditorListener(this);
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		spinner.setValue(value);
		return spinner;
	}

	public Object getCellEditorValue() {
		try {
			((JSpinner.DefaultEditor) spinner.getEditor()).commitEdit();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return spinner.getShort();
	}

	@Override
	public void editingStopped(ChangeEvent e) {
		// TODO Auto-generated method stub
		System.out.println("editingStopped");
	}

	@Override
	public void editingCanceled(ChangeEvent e) {
		// TODO Auto-generated method stub
		System.out.println("editingCanceled");
	}
}
