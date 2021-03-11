package Views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import Components.HintTextField;
import Components.MyButton;
import Components.WrapLayout;

public class SearchPanel extends JPanel {

	JPanel simpleSearchBox = new JPanel();
	HintTextField searchTextBox = new HintTextField("Поиск");
	JToggleButton advancedSearchToggle = new JToggleButton();
//	WrapLayout lay = new WrapLayout(FlowLayout.LEFT, 0, 0);
	JPanel advancedSearchPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 0, 0));
	MyButton searchButton = new MyButton("Найти");

	public SearchPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		simpleSearchBox.setLayout(new BoxLayout(simpleSearchBox, BoxLayout.X_AXIS));
		simpleSearchBox.add(searchTextBox).setFont(new Font("Verdana", Font.PLAIN, 14));
		simpleSearchBox.add(searchButton);
		simpleSearchBox.add(Box.createHorizontalStrut(1));
		simpleSearchBox.setMaximumSize(new Dimension(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width, 40));

		advancedSearchToggle.setIcon(new ImageIcon("icons\\caret-down (1).png"));
		advancedSearchToggle.setSelectedIcon(new ImageIcon("icons\\caret-arrow-up (2).png"));
		advancedSearchToggle.setFocusPainted(false);
		advancedSearchToggle.setToolTipText("Расширенный поиск");
		var border = new EmptyBorder(10, 20, 10, 20);
		advancedSearchToggle.setBorder(border);
		var defaultBorder = searchButton.getBorder();
		searchButton.setBorder(border);
		simpleSearchBox.add(advancedSearchToggle);
		super.add(simpleSearchBox);

		super.add(advancedSearchPanel);
		advancedSearchPanel.setVisible(false);

		advancedSearchToggle.addActionListener(e -> {
			searchTextBox.setEnabled(!advancedSearchToggle.isSelected());
			advancedSearchPanel.setVisible(advancedSearchToggle.isSelected());
			if (advancedSearchPanel.isVisible()) {
				advancedSearchPanel.add(searchButton);
				searchButton.setBorder(defaultBorder);
				advancedSearchPanel.setSize(new Dimension(getSize().width, 1));
//				var sz = advancedSearchPanel.getSize();
//				sz.width = getSize().width;
//				advancedSearchPanel.setSize(sz);
//				var sz = lay.preferredLayoutSize(advancedSearchPanel);
//				sz.height += simpleSearchBox.getHeight();
//				sz.width = SearchPanel.this.getWidth();
//				advancedSearchPanel.setMaximumSize(sz);
//				advancedSearchPanel.setLocation(0, advancedSearchPanel.getY());
			} else {
				simpleSearchBox.add(searchButton, 1);
				searchButton.setBorder(border);
			}
		});

		searchButton.addActionListener(e -> {
			if (advancedSearchPanel.isVisible()) {
				if (advancedSearchRun != null)
					advancedSearchRun.run();
			} else {
				if (simpleSearchRun != null)
					simpleSearchRun.accept(searchTextBox.getText());
			}
		});

		searchTextBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (simpleSearchRun != null)
						simpleSearchRun.accept(searchTextBox.getText());
		        }
			}
		});

	}

	@Override
	public Component add(Component c) {
		advancedSearchPanel.add(c);
		return c;
	}

	public Runnable advancedSearchRun = null;
	public Consumer<String> simpleSearchRun = null;
	
	
	public void setSimpleSearchBoxHint(String hint)
	{
		searchTextBox.setHint(hint);
	}
	
	public void enableAdvancedSearch(boolean flag)
	{
		advancedSearchToggle.setVisible(flag);
	}
}
