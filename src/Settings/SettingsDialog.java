package Settings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.table.JTableHeader;

import Components.MyCheckBox;
import Components.MyDialog;
import Components.MyScrollPane;
import Components.Utils;
import Database.Librarian;
import Forms.AuthorizationDialog;
import Forms.LibrarianDialog;
import Generic.GenericTable;

public class SettingsDialog extends MyDialog {

	public SettingsDialog() {
		init();
	}

	public SettingsDialog(Frame owner) {
		super(owner);
		init();
	}

	JTabbedPane panes = new JTabbedPane();
	JPanel settingsPanel = new JPanel();
	JPanel accountsPanel = new JPanel();

	private void init() {
		saveTitle = "Настройки";
		updateTitle = "Настройки";
		ImageIcon icon = new ImageIcon("icons\\settings.png");
		setIconImage(icon.getImage());
		
		super.panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		super.panel.setBorder(BorderFactory.createEmptyBorder());
		super.panel.add(panes);
		panes.addTab("Интерфейc", new ImageIcon("icons\\bookshelf (2).png"), settingsPanel);
		panes.addTab("Учетные записи", new ImageIcon("icons\\account.png"), accountsPanel);
		createSettingsTab();
		createAccountsTab();

		super.saveButton.setText("Применить");
		super.saveAndUpdateButton.setText("    OK     ");

		pack();
	}

	private void createSettingsTab() {
		settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
		
		
		JCheckBox checkBox = null;
		
		checkBox = new MyCheckBox("Сохранять ширину столбцов", 
				Settings.ApplicationSettings.Configuration.saveTablesColumns);
		checkBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
            	Settings.ApplicationSettings.Configuration.saveTablesColumns = e.getStateChange() == ItemEvent.SELECTED;
            }
        });
		settingsPanel.add(checkBox);
		settingsPanel.add(new JSeparator(SwingConstants.HORIZONTAL)).setMaximumSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width * 3, 3));

		checkBox = new MyCheckBox("Показывать подписи кнопок главного меню", 
				Settings.ApplicationSettings.Configuration.showMainMenuButtonsTitles);
		checkBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
            	Settings.ApplicationSettings.Configuration.showMainMenuButtonsTitles = e.getStateChange() == ItemEvent.SELECTED;
            }
        });
		settingsPanel.add(checkBox);
		settingsPanel.add(new JSeparator(SwingConstants.HORIZONTAL)).setMaximumSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width * 3, 3));

		checkBox = new MyCheckBox("Показывать подписи кнопок меню таблиц", 
				Settings.ApplicationSettings.Configuration.showTablesMenuButtonsTitles);
		checkBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
            	Settings.ApplicationSettings.Configuration.showTablesMenuButtonsTitles = e.getStateChange() == ItemEvent.SELECTED;
            }
        });
		settingsPanel.add(checkBox);
		settingsPanel.add(new JSeparator(SwingConstants.HORIZONTAL)).setMaximumSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width * 3, 3));
		settingsPanel.add(Box.createVerticalGlue());
	}

	

	GenericTable table = new GenericTable<Librarian>(Librarian.class,
			new String[] { "ФИО", "Телефон" });

	private LibrarianDialog accountDialog = new LibrarianDialog();
	private AuthorizationDialog authorizationDialog = new AuthorizationDialog();
	private void createAccountsTab() {

		Font font = new Font("Verdana", Font.PLAIN, 12);
		table.setFont(font);
		table.setRowHeight(30);
		table.setBackground(Color.orange);
//		table.setForeground(Color.white);

		JTableHeader header = table.getTableHeader();
		header.setPreferredSize(new Dimension(header.getPreferredSize().width, 32));
		table.getTableHeader().setOpaque(false);
		table.getTableHeader().setBackground(new Color(93, 163, 226));
		table.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 12));;

		table.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) {
	            if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
	        		Librarian account = authorizationDialog.show((Librarian) table.getSelectedItem());
	        		if (account != null)
	        			accountDialog.show(account);
	        		Utils.print(account);
	            }
	        }
	    });		
		table.reload();
		
		
		accountDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				table.reload();
			}
			@Override
			public void windowClosed(WindowEvent e) {
				table.reload();
			}
		});
		
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		var btn = toolBar.add(new AbstractAction("Добавить учётную запись", new ImageIcon("icons\\plus (2).png")) {
			public void actionPerformed(ActionEvent event) {
				accountDialog.show();
			}
		});
		btn.setHideActionText(false);
		btn.setHorizontalTextPosition(SwingConstants.RIGHT);
		btn.setVerticalTextPosition(SwingConstants.CENTER);
		toolBar.add(Box.createHorizontalGlue());
		
		
		accountsPanel.setLayout(new BoxLayout(accountsPanel, BoxLayout.Y_AXIS));
		accountsPanel.add(new MyScrollPane(table));
		accountsPanel.add(toolBar);
	}

	@Override
	protected void getInfo() throws ParseException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setInfo() {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void insert(Object object)
	{
		table.saveChanges();
		Settings.ApplicationSettings.save();
	}
	
	@Override
	protected void update(Object object)
	{
		table.saveChanges();
		Settings.ApplicationSettings.save();
	}

	@Override
	public void show() {
		super.show(Object.class);
		table.reload();
	}

}
