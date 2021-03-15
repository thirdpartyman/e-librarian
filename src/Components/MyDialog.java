package Components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

import javax.persistence.PersistenceException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;

import Database.HibernateUtil;

public abstract class MyDialog extends JDialog {

	protected JToolBar statusBar = new JToolBar();
	protected MyGroupBox panel = new MyGroupBox();

	private void init() {
		setModalityType(ModalityType.MODELESS);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setLocationRelativeTo(getParent());
		setEscapeCloseOperation(this);
		
		createStatusBar();
		getContentPane().add(statusBar, BorderLayout.PAGE_END);
		
		getContentPane().add(panel);
		Border border = panel.getBorder();
		Border margin = new EmptyBorder(10, 10, 10, 10);
		panel.setBorder(new CompoundBorder(margin, border));
		
		getContentPane().setBackground( Color.white );
	}

	public MyDialog() {
		init();
	}

	public MyDialog(Frame owner) {
		super(owner);
		init();
	}

	public MyDialog(Dialog owner) {
		super(owner);
		init();
	}

	public MyDialog(Window owner) {
		super(owner);
		init();
	}

	public MyDialog(Frame owner, boolean modal) {
		super(owner, modal);
		init();
	}

	public MyDialog(Frame owner, String title) {
		super(owner, title);
		init();
	}

	public MyDialog(Dialog owner, boolean modal) {
		super(owner, modal);
		init();
	}

	public MyDialog(Dialog owner, String title) {
		super(owner, title);
		init();
	}

	public MyDialog(Window owner, ModalityType modalityType) {
		super(owner, modalityType);
		init();
	}

	public MyDialog(Window owner, String title) {
		super(owner, title);
		init();
	}

	public MyDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		init();
	}

	public MyDialog(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
		init();
	}

	public MyDialog(Window owner, String title, ModalityType modalityType) {
		super(owner, title, modalityType);
		init();
	}

	public MyDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		init();
	}

	public MyDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		init();
	}

	public MyDialog(Window owner, String title, ModalityType modalityType, GraphicsConfiguration gc) {
		super(owner, title, modalityType, gc);
		init();
	}

	public MyDialog(String title) {
		setTitle(title);
		init();
	}

	private void createStatusBar() {
		statusBar.setFloatable(false);
		Border border = BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(150, 150, 150));
		Border margin = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		statusBar.setBorder(new CompoundBorder(border, margin));
		statusBar.add(Box.createHorizontalGlue());
		statusBar.add(new SaveAction()).setFocusPainted(false);
		statusBar.addSeparator(Utils.weight);
		statusBar.add(new SaveAndExitAction()).setFocusPainted(false);
	}

	// закрытие окна диалога по нажатию клавиши Escape
	private static void setEscapeCloseOperation(final JDialog dialog) {
		final KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		final String dispatchWindowClosingActionMapKey = "com.spodding.tackline.dispatch:WINDOW_CLOSING";

		Action dispatchClosing = new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
			}
		};
		JRootPane root = dialog.getRootPane();
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
		root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
	}

	protected String saveTitle;
	protected String updateTitle;

	protected <T> void show(Class<T> type) {
		this.setTitle(saveTitle);
		showflag = true;
		try {
			this.object = type.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setInfo();
		super.show();
	}

	boolean showflag = true;

	protected Object object;

	public <T> void show(T author) {
		this.setTitle(updateTitle);
		showflag = false;
		this.object = author;
		setInfo();
		super.show();
	}

	protected abstract void getInfo() throws ParseException;

	protected abstract void setInfo();

	public abstract void show();

	
	protected void insert(Object object)
	{
		HibernateUtil.insert(object);
		if (onInsert != null) onInsert.run();
	}
	
	protected void update(Object object)
	{
		HibernateUtil.update(object);
		if (onUpdate != null) onUpdate.run();
	}
	
	protected Runnable onInsert = null;
	protected Runnable onUpdate = null;
	
	private boolean save()
	{
		try {
			getInfo();

			if (showflag) {
				insert(object);

				showflag = false;
				setTitle(updateTitle);
			} else {
				update(object);
			}
			
		} catch (ConstraintViolationException e) {
			JOptionPane.showMessageDialog(MyDialog.this, e.getSQL(), e.getConstraintName(),
					JOptionPane.ERROR_MESSAGE);
			return false;
		} 

		catch(PropertyValueException e)
		{
			JOptionPane.showMessageDialog(MyDialog.this, e.getLocalizedMessage(),
					e.getEntityName() + '.' + e.getPropertyName() + " is null", JOptionPane.ERROR_MESSAGE);
			return false;
		}catch (PersistenceException e) {
			ConstraintViolationException e1 = (ConstraintViolationException) e.getCause();
			
			if (e1 == null)
			{
				JOptionPane.showMessageDialog(MyDialog.this, e.getLocalizedMessage(),
				e.toString(), JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				JdbcSQLIntegrityConstraintViolationException  e2 = (JdbcSQLIntegrityConstraintViolationException ) e1.getCause();
				JOptionPane.showMessageDialog(MyDialog.this, e2.getLocalizedMessage(), e.getLocalizedMessage(),
						JOptionPane.ERROR_MESSAGE);
			}
			
			return false;
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(MyDialog.this, ((Exception)e.getSuppressed()[0]).getMessage(),
					e.getLocalizedMessage(), JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}
	
	
	class SaveAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		SaveAction() {
			putValue(NAME, "Сохранить");
		}

		public void actionPerformed(ActionEvent event) {
			save();
		}
	}

	class SaveAndExitAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		SaveAndExitAction() {
			putValue(NAME, "Сохранить и выйти");
		}

		public void actionPerformed(ActionEvent event) {
			if (save())
				dispose();//generates windowClosed event
		}
	}

}
