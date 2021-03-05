package Forms;

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import Components.MyDialog;
import Components.MyGroupBox;
import Components.MyTextField;
import Components.ReleaseYearSpinner;
import Database.Author;

public class AuthorDialog extends MyDialog {

	MyTextField nameTextField = new MyTextField();
	ReleaseYearSpinner birthYearSpinner = new ReleaseYearSpinner(-4000);

	public AuthorDialog(Window parent) {
		super(parent);
		createLayout();// разметка

		ImageIcon icon = new ImageIcon("icons\\typewriter.png");
		setIconImage(icon.getImage());
		
		saveTitle = "Добавить запись об авторе";
		updateTitle = "Изменить запись об авторе";
	}

	
	private void createLayout() {
		
		MyGroupBox panel = new MyGroupBox();
//		Border border = panel.getBorder();
		Border margin = new EmptyBorder(10, 10, 10, 10);
//		panel.setBorder(new CompoundBorder(margin, border));
		panel.setBorder(margin);
		
		// список полей ввода с подписями
		panel.addComponentWithLabel("ФИО", nameTextField);
		panel.addComponentWithLabel("Год рождения", birthYearSpinner);

		this.getContentPane().add(panel);
		
		
		pack();
		Dimension size = getSize();
		size.width = (int)(size.height * 2.414);
		setMinimumSize(size);//минимальный размер не может быть меньше рассчитанного при создании диалога

	}
	
	
	
	@Override
	protected void getInfo()
	{
		Author author = (Author)this.object;
		author.name = nameTextField.getText().trim();
		author.birthYear = birthYearSpinner.getShort();
	}
	
	@Override
	protected void setInfo()
	{
		Author author = (Author)this.object;
		nameTextField.setText(author.name);
		birthYearSpinner.setValue(author.birthYear == null ? (short)0 : author.birthYear);
	}


	@Override
	public void show() {
		super.show(Author.class);
	}

}
