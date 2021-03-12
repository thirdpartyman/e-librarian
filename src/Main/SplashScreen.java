package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.event.MouseInputAdapter;

public class SplashScreen extends JDialog {

	public SplashScreen() {

		JToolBar statusBar = new JToolBar();
		statusBar.add(Box.createHorizontalGlue());
		statusBar.add(new JLabel("Электронный Библиотекарь")).setFont(new Font("Verdana", Font.PLAIN, 18));
		statusBar.add(Box.createHorizontalGlue());
		getContentPane().add(statusBar, BorderLayout.PAGE_END);

		BufferedImage img;
		try {
			img = ImageIO.read(new File("icons/online-library.png"));
			ImageIcon icon = new ImageIcon(img);
			JLabel label = new JLabel(icon);
			getContentPane().add(label);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setUndecorated(true);
		statusBar.setFloatable(false);
		this.setBackground(new Color(0, 0, 0, 0));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

		DragListener drag = new DragListener();
		addMouseListener(drag);
		addMouseMotionListener(drag);
	}

	public class DragListener extends MouseInputAdapter {
		Point location;
		MouseEvent pressed;

		public void mousePressed(MouseEvent me) {
			pressed = me;
		}

		public void mouseDragged(MouseEvent me) {
			Component component = me.getComponent();
			location = component.getLocation(location);
			int x = location.x - pressed.getX() + me.getX();
			int y = location.y - pressed.getY() + me.getY();
			component.setLocation(x, y);
		}
	}
}
