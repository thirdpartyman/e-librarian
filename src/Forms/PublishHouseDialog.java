package Forms;

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import Database.Author;
import Database.PublishHouse;
import etc.MyDialog;
import etc.MyGroupBox;
import etc.MyTextField;

public class PublishHouseDialog extends MyDialog {

	MyTextField nameTextField = new MyTextField();
	MyTextField cityTextField = new MyTextField();

	public PublishHouseDialog(Window parent) {
		super(parent);
		createLayout();// разметка

		ImageIcon icon = new ImageIcon("icons\\multifunction-printer.png");
		setIconImage(icon.getImage());
		
		saveTitle = "Добавить запись об издательстве";
		updateTitle = "Изменить запись об издательстве";
	}

	
	private void createLayout() {
		
		MyGroupBox panel = new MyGroupBox();
//		Border border = panel.getBorder();
		Border margin = new EmptyBorder(10, 10, 10, 10);
//		panel.setBorder(new CompoundBorder(margin, border));
		panel.setBorder(margin);
		
		// список полей ввода с подписями
		panel.addComponentWithLabel("Название", nameTextField);
		panel.addComponentWithLabel("Город", cityTextField);

		this.getContentPane().add(panel);
		
		
		pack();
		Dimension size = getSize();
		size.width = (int)(size.height * 2.414);
		setMinimumSize(size);//минимальный размер не может быть меньше рассчитанного при создании диалога

	}
	
	
	
	@Override
	protected void getInfo()
	{
		PublishHouse author = (PublishHouse)this.object;
		author.name = nameTextField.getText().trim();
		author.city = cityTextField.getText().trim();

	}
	
	@Override
	protected void setInfo()
	{
		PublishHouse author = (PublishHouse)this.object;
		nameTextField.setText(author.name);
		cityTextField.setText(author.city);
	}


	@Override
	public void show() {
		super.show(PublishHouse.class);
	}

}
