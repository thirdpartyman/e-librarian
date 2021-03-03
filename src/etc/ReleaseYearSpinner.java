package etc;

import javax.swing.SpinnerNumberModel;

public class ReleaseYearSpinner extends MySpinner {

	public ReleaseYearSpinner() {
		super(new SpinnerNumberModel(Utils.currentYear,	(short) 1500, Utils.currentYear, (short) 1));
	}

	public ReleaseYearSpinner(int min) {
		super(new SpinnerNumberModel(Utils.currentYear,
				(short) min, Utils.currentYear, (short) 1));
	}
	
	@Override
	public Short getValue()
	{
		if (super.getValue() instanceof Short)
			return null;
		else
		{
			int val = (Integer)super.getValue();
			return (short)val;
		}
	}
	
}
