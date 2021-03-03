package Main;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import Forms.AuthorDialog;
import Forms.ReceiveBookDialog;
import Views.Catalog;
import etc.MyButton;

public class MainForm extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2062760208030859011L;
	
	
	Catalog catalogView = new Catalog();
	ReceiveBookDialog receiveBookDialog = new ReceiveBookDialog(this);
	AuthorDialog authorDialog = new AuthorDialog(this);

	private void createMenu() throws IOException {
		
		JPanel panes = new JPanel(new CardLayout()); 
		panes.setOpaque(false);
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBorder(BorderFactory.createEmptyBorder(5, 7, 5, 5));

		getContentPane().add(toolBar, BorderLayout.PAGE_START);
		getContentPane().add(panes);


		ArrayList<JToggleButton> views = new ArrayList<JToggleButton>();

		Dimension weight = new Dimension(5, 5);	
		Dimension sz = new Dimension(100, 150);
		
		Function<String, String> multiLine = (text) -> "<html>" + text.replaceAll(" ", "<br>") + "</html>";

		MyButton btn = null;
		JToggleButton tbtn = null;
		
		btn = (MyButton) toolBar.add(new MyButton(multiLine.apply("Выдача книг"), new ImageIcon("icons\\book (3).png")));
		btn.setMaximumSize(sz);
		btn.addActionListener(e -> authorDialog.show());
		toolBar.addSeparator(weight);
		tbtn = (JToggleButton) toolBar.add(new JToggleButton("Каталог", new ImageIcon("icons\\001-books.png"))); 
		views.add(tbtn);
		panes.add(catalogView, tbtn.getText()); 
		toolBar.addSeparator(weight);
		tbtn = (JToggleButton) toolBar.add(new JToggleButton(multiLine.apply("История выдач"), new ImageIcon("icons\\books (1).png")));
		views.add(tbtn);
		panes.add(new JButton("История выдач"), tbtn.getText()); 
		toolBar.addSeparator(weight);
		tbtn = (JToggleButton) toolBar.add(new JToggleButton("Читатели", new ImageIcon("icons\\driving-license.png")));
		views.add(tbtn);
		panes.add(new JButton("Читатели"), views.get(views.size() - 1).getText()); 
		toolBar.addSeparator(weight);
		btn = (MyButton)toolBar.add(new MyButton(multiLine.apply("Привоз книг"), new ImageIcon("icons\\book (4).png")));
		btn.setMaximumSize(sz);
		btn.addActionListener(e -> receiveBookDialog.show());
		toolBar.addSeparator(weight);
		toolBar.add(Box.createHorizontalGlue());
		btn = (MyButton) toolBar.add(new MyButton("Настройки", new ImageIcon("icons\\manual.png")));
		btn.addActionListener(e -> receiveBookDialog.show());
		
		
		for (JToggleButton button : views) {
			button.setMaximumSize(sz);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JToggleButton button = (JToggleButton) e.getSource();
			        CardLayout layout = (CardLayout)(panes.getLayout());
			        layout.show(panes, button.getText());
			        
					if (button.isSelected()) {
						for (JToggleButton btn : views)
							if (btn != e.getSource())
								btn.setSelected(false);
					}
					else button.setSelected(true);
				}
			});

		}

		
//		JPanel panel = new JPanel();
//		panel.setLayout(new FlowLayout());
//
//		Consumer<File> listFilesForFolder = (folder) -> {
//			for (final File fileEntry : folder.listFiles()) {
//				if (fileEntry.isDirectory()) {
////		            listFilesForFolder.accept(fileEntry);
//				} else {
//					BufferedImage img = null;
//					try {
//						img = ImageIO.read(new File("icons\\" + fileEntry.getName()));
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					ImagePanel cmp = (ImagePanel)panel.add(new ImagePanel(img));
//					cmp.setToolTipText(fileEntry.getName());
//					cmp.setPreferredSize(new Dimension(100, 100));
//					System.out.println(fileEntry.getName());
//				}
//			}
//		};
//
//		final File folder = new File("icons");
//		listFilesForFolder.accept(folder);
//
//		add(panel);
	}


	public MainForm() throws IOException {
		super("Библиотека");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		createMenu();
		// Выводим окно на экран
		setSize(400, 300);
		pack();
		setLocationRelativeTo(null);
		getContentPane().setBackground( Color.white );
		ImageIcon icon = new ImageIcon("icons\\online-library.png");
		setIconImage(icon.getImage());
		setVisible(true);
	}

}