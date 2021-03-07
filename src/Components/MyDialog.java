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

import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;

import Database.HibernateSessionFactoryUtil;

public abstract class MyDialog extends JDialog {

	protected JToolBar statusBar = new JToolBar();

	private void init() {
		setModalityType(ModalityType.MODELESS);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setLocationRelativeTo(getParent());
		setEscapeCloseOperation(this);
		createStatusBar();
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

	private void createStatusBar() {
		statusBar.setFloatable(false);
		Border border = BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(150, 150, 150));
		Border margin = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		statusBar.setBorder(new CompoundBorder(border, margin));
		getContentPane().add(statusBar, BorderLayout.PAGE_END);
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

	public <T> void show(Class<T> type) {
		this.setTitle(saveTitle);
		showflag = true;
		try {
			this.object = type.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		setInfo();
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

	protected abstract void getInfo();

	protected abstract void setInfo();

	public abstract void show();

	
	private boolean save()
	{
		getInfo();
		try {
			if (showflag) {
				HibernateSessionFactoryUtil.insert(object);

				showflag = false;
				setTitle(updateTitle);
			} else {
				HibernateSessionFactoryUtil.update(object);
			}
			
		} catch (ConstraintViolationException e) {
			JOptionPane.showMessageDialog(MyDialog.this, e.getSQLException(), e.getMessage(),
					JOptionPane.ERROR_MESSAGE);
			return false;
		} 
		catch(PropertyValueException e)
		{
			JOptionPane.showMessageDialog(MyDialog.this, e.getLocalizedMessage(),
					e.getEntityName() + '.' + e.getPropertyName() + " is null", JOptionPane.ERROR_MESSAGE);
		}catch (PersistenceException e) {
			JOptionPane.showMessageDialog(MyDialog.this, e.getLocalizedMessage(),
					e.toString(), JOptionPane.ERROR_MESSAGE);
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
