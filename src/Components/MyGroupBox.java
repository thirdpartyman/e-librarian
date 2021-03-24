package Components;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class MyGroupBox extends JPanel {
	
	public MyGroupBox() {
		super(new GridBagLayout());
        var border = BorderFactory.createLineBorder(Color.black);
		Border margin = new EmptyBorder(5, 5, 5, 5);
		setBorder(new CompoundBorder(border, margin));
		setOpaque(false);
	}
	
	public MyGroupBox(String title) {
		super(new GridBagLayout());
        var border = BorderFactory.createTitledBorder(title);
        border.setTitleColor(Color.black);
		Border margin = new EmptyBorder(5, 5, 5, 5);
		setBorder(new CompoundBorder(border, margin));
		setOpaque(false);
	}

	
	public void addComponentWithLabel(String labelText, Component comp) {

		JLabel label = new JLabel(labelText);
		GridBagConstraints gridBagConstraint = new GridBagConstraints();

		gridBagConstraint.fill = GridBagConstraints.BOTH;
		gridBagConstraint.insets = new Insets(0, 0, 5, 5);
		gridBagConstraint.gridx = 0;
		this.add(label, gridBagConstraint);

		gridBagConstraint.insets = new Insets(0, 5, 5, 0);
		gridBagConstraint.gridx = 1;
		gridBagConstraint.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraint.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraint.weightx = 1.0f;
		this.add(comp, gridBagConstraint);
	}

	
	public Component add(Component comp)
	{
		GridBagConstraints gridBagConstraint = new GridBagConstraints();
		gridBagConstraint.insets = new Insets(0, 0, 2, 0);
		gridBagConstraint.gridx = 0;
		gridBagConstraint.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraint.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraint.weightx = 1.0f;
		this.add(comp, gridBagConstraint);
		return comp;
	}
}
