package Views;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JToolBar;

import Components.MyButton;

public class ViewMenu extends JToolBar {

	public ViewMenu() {
		init();
	}

	public ViewMenu(int orientation) {
		super(orientation);
		init();
	}

	public ViewMenu(String name) {
		super(name);
		init();
	}

	public ViewMenu(String name, int orientation) {
		super(name, orientation);
		init();
	}
	
	
	private MyButton reloadButton = new MyButton("Обновить", new ImageIcon("icons\\003-reload.png"));
	private MyButton saveChangesButton = new MyButton("Сохранить изменения", new ImageIcon("icons\\008-upload.png"));
	public JCheckBox enableEdit = new JCheckBox("Разрешить редактирование");
	
	private void init()
	{
		setFloatable(false);
//		setBorder(BorderFactory.createEmptyBorder(5, 7, 5, 5));
		add(reloadButton);
		add(saveChangesButton);
		add(enableEdit);
		add(Box.createHorizontalGlue());
		enableEdit.setSelectedIcon(new ImageIcon("icons\\checked.png"));
		enableEdit.setIcon(new ImageIcon("icons\\checked — копия.png"));
		enableEdit.setFocusPainted(false);
		enableEdit.setSelected(false);
	}

	public void addReloadButtonListener(ActionListener eventHandler)
	{
		reloadButton.addActionListener(eventHandler);
	}
	
	public void addSaveChangesButtonListener(ActionListener eventHandler)
	{
		saveChangesButton.addActionListener(eventHandler);
	}
	
	public void addEnableEditListener(ItemListener eventHandler)
	{
		enableEdit.addItemListener(eventHandler);
	}
}
