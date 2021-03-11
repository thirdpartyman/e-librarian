package Generic;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

public class GenericComboBox<T> extends JComboBox<T> {

	Model model = new Model();
	String filter = "";

	public GenericComboBox() {
		init();
	}

	public GenericComboBox(Vector<T> items) {
		init();
		setItems(items);
	}

	public void init() {

		this.setModel(model);
		this.setEditable(true);

		Consumer<String> onFilter = (s) -> {
			model.filter(s);

			hidePopup();// для перерасчёта высоты popup-меню.
			showPopup();
		};

		this.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				super.focusLost(e);
				System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				if (filter.isEmpty())
					return;
				model.filter(filter);// если не был выбран элемент, выбираем его по введенному фильтру
				GenericComboBox.this.getEditor().setItem(model.v.get(0));
			}
		});

		this.addMouseWheelListener(e -> {
			Object item = getSelectedItem();
			if (item instanceof String)
				item = model.v.get(0);
			if (model.filtered()) {
				model.filter("");
				this.setSelectedItem(item);
			}
			int selected = getSelectedIndex();
			if (selected == getItemCount() || (selected == 0 && e.getWheelRotation() < 0))
				return;
			setSelectedIndex(getSelectedIndex() + (int) Math.signum(e.getWheelRotation()));
		});

		this.getEditor().getEditorComponent().addMouseListener((MouseListener) new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				GenericComboBox.this.getEditor().setItem(filter);
				GenericComboBox.this.showPopup();
			}
		});

		this.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
//				GenericComboBox.this.getEditor().setItem(filter);
				Object item = getSelectedItem();
				if (item instanceof String)
					GenericComboBox.this.getEditor().setItem(filter);
				else if (item != null)
					GenericComboBox.this.getEditor().setItem(((Filterable) item).getFilterableField());
			}

			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP | e.getKeyCode() == KeyEvent.VK_DOWN) {
					GenericComboBox.this.getEditor().setItem(model.getSelectedItem());
					return;
				}

				filter = (String) GenericComboBox.this.getEditor().getItem();
				model.setSelectedItem(filter);
				onFilter.accept(filter);
			}
		});

		setRenderer(new ComboBoxRenderer());

		addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					var item = ((GenericComboBox) e.getSource()).getSelectedItem();
					if (!(item instanceof String)) {						
//						filter = ((Filterable) item).getFilterableField().toString();
//						GenericComboBox.this.setSelectedItem(item);
//						System.err.println(model.getSelectedItem());
						GenericComboBox.this.getEditor().setItem(model.getSelectedItem());
//						onFilter.accept(filter);
//						model.filter("");
					}
				}
			}
		});
	}

	public void setItems(Vector vec) {
		model.vsrc = vec;
		model.filter("");
	}

	class Model extends AbstractListModel<T> implements ComboBoxModel<T> {
		T selectItem;

		public void setSelectedItem(Object anItem) {
			this.selectItem = (T) anItem;
		}

		public T getSelectedItem() {
			return selectItem;
		}

		Vector<T> vsrc = new Vector<T>();
		Vector<T> v = new Vector<T>();

		public int getSize() {
			return v.size();
		}

		public T getElementAt(int index) {
			return (T) v.get(index);
		}

		// выставляем фильтр и фильтруем содержание
		public void filter(String filter) {
			v = new Vector(vsrc.stream().filter(p -> ((Filterable) p).tryfilter(filter)).collect(Collectors.toList()));
			fireContentsChanged(this, 0, vsrc.size());
		}

		public boolean filtered() {
			return v.size() != vsrc.size();
		}
	}

	private class ComboBoxRenderer extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			T bbk = (T) value;
			label.setText(bbk.toString());
			return label;
		}
	}
}
