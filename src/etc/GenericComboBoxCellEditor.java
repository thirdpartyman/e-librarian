package etc;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;

import ComboBox.GenericComboBox;

public class GenericComboBoxCellEditor extends DefaultCellEditor {

	private GenericComboBox comboBox;

	public <T> GenericComboBoxCellEditor(GenericComboBox<T> comboBox) {
		super(comboBox);
		this.comboBox = comboBox;
		comboBox.setBorder(null);
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		comboBox.setSelectedItem(value);
		return comboBox;
	}

	public Object getCellEditorValue() {
		return comboBox.getSelectedItem();
	}
}
