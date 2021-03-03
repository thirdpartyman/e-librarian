package ComboBox;

import java.awt.Component;
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

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

import Database.BBK;
import Database.HibernateSessionFactoryUtil;

public class BBKComboBox extends JComboBox<BBK> {

	Model oModel = new Model();
	String filter = "";
	boolean flag = false;

	
	public BBKComboBox() {
		this.setModel(oModel);
		this.setEditable(true);

		Consumer<String> onFilter = (s) -> {
			oModel.setFilter(s);

			hidePopup();// для перерасчёта высоты попап-меню.
			showPopup();
		};
		
		
		this.addMouseWheelListener(e -> this.setSelectedIndex(this.getSelectedIndex() + (int)Math.signum(e.getWheelRotation())));
		
		this.getEditor().getEditorComponent().addMouseListener((MouseListener) new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				BBKComboBox.this.getEditor().setItem(filter);
				BBKComboBox.this.showPopup();
			}
		});

		this.getEditor().getEditorComponent().addKeyListener((KeyListener) new KeyAdapter() {

			public void keyReleased(KeyEvent e) {

//				if (flag)
//				{	
//				    if (e.getKeyChar() == '\b')
//				    	filter = filter.substring(0, filter.length() - 1);
//				    else
//				    	filter += e.getKeyChar();
//					BBKComboBox.this.getEditor().setItem(filter);
//					System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//					flag = false;
//				}

				
				if (e.getKeyCode() == KeyEvent.VK_UP | e.getKeyCode() == KeyEvent.VK_DOWN) {
					BBKComboBox.this.getEditor().setItem(oModel.getSelectedItem());
					return;
				}

				filter = (String) BBKComboBox.this.getEditor().getItem();
				oModel.setSelectedItem(filter);
				onFilter.accept(filter);
			}
		});

		setRenderer(new ComboBoxRenderer());
		addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					var country = ((BBKComboBox) e.getSource()).getSelectedItem();
					System.err.println(country);
					if (country instanceof BBK) {
						filter = ((BBK) country).index;
						onFilter.accept(filter);
						flag = true;
						oModel.setFilter("");
						BBKComboBox.this.grabFocus();
					}
				}
			}
		});

		var list = HibernateSessionFactoryUtil.loadAllData(BBK.class);
		this.setValue(new Vector(list));
	}

	void setValue(Vector val) {
		oModel.vsrc = val;
		oModel.setFilter("");
	}

	class Model extends AbstractListModel<BBK> implements ComboBoxModel<BBK> {
		Object selectItem;
		String filter = "";

		public void setSelectedItem(Object anItem) {
			this.selectItem = anItem;
		}

		public Object getSelectedItem() {
			return selectItem;
		}

		//////////////////////////////////////////////////////////

		Vector<BBK> vsrc = new Vector<BBK>();
		Vector<BBK> v = new Vector<BBK>();

		public int getSize() {
			return v.size();
		}

		public BBK getElementAt(int index) {
			return (BBK) v.get(index);
		}

		// выставляем фильтр и фильтруем содержание
		public void setFilter(String s) {
			if (s == null)
				filter = "";
			else
				filter = s;

			v.removeAllElements();
			for (int q = 0; q < vsrc.size(); q++) {
				if (vsrc.get(q) == null)
					continue;
				String src = vsrc.get(q).index;
				if (src == null)
					continue;
				if (src.startsWith(filter))
					v.add(vsrc.get(q));
			}

			this.fireContentsChanged(this, 0, vsrc.size());
		}
	}

	private class ComboBoxRenderer extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			BBK bbk = (BBK) value;
			label.setText(bbk.index + ' ' + bbk.text);
			return label;
		}
	}
}
