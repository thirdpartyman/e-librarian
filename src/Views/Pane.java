package Views;

import java.awt.LayoutManager;

import javax.swing.JPanel;

public class Pane extends JPanel {

	public Pane() {
		setOpaque(false);
	}

	public Pane(LayoutManager layout) {
		super(layout);
		setOpaque(false);
	}

	public Pane(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		setOpaque(false);
	}

	public Pane(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		setOpaque(false);
	}

}
