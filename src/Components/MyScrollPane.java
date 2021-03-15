package Components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JScrollPane;

public class MyScrollPane extends JScrollPane {

	public MyScrollPane() {
		init();
	}

	public MyScrollPane(Component view) {
		super(view);
		init();
	}

	public MyScrollPane(int vsbPolicy, int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);
		init();
	}

	public MyScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
		init();
	}

	private void init()
	{
		getViewport().setBackground(Color.white);
	}
}
