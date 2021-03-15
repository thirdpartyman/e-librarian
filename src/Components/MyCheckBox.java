package Components;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

public class MyCheckBox extends JCheckBox {

	public MyCheckBox() {
		init();
	}

	public MyCheckBox(Icon icon) {
		super(icon);
		init();
	}

	public MyCheckBox(String text) {
		super(text);
		init();
	}

	public MyCheckBox(Action a) {
		super(a);
		init();
	}

	public MyCheckBox(Icon icon, boolean selected) {
		super(icon, selected);
		init();
	}

	public MyCheckBox(String text, boolean selected) {
		super(text, selected);
		init();
	}

	public MyCheckBox(String text, Icon icon) {
		super(text, icon);
		init();
	}

	public MyCheckBox(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
		init();
	}
	
	
	private void init()
	{
		setSelectedIcon(new ImageIcon("icons\\check.png"));
		setIcon(new EmptyIcon(24, 24));
		setFocusPainted(false);
		setHideActionText(false);
		setHorizontalTextPosition(SwingConstants.RIGHT);
		setVerticalTextPosition(SwingConstants.CENTER);
	}
}
