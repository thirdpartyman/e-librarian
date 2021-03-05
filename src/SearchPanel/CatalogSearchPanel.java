package SearchPanel;

import ComboBox.GenericComboBox;
import Components.MyTextField;
import Components.ReleaseYearSpinner;
import Database.Author;
import Database.BBK;
import Database.PublishHouse;

public class CatalogSearchPanel extends SearchPanel {

	MyTextField nameTextField = new MyTextField();
	GenericComboBox bbkComboBox = new GenericComboBox<BBK>();
	GenericComboBox authorComboBox = new GenericComboBox<Author>();
	GenericComboBox publishHouseComboBox = new GenericComboBox<PublishHouse>();
	ReleaseYearSpinner releaseYearSpinner = new ReleaseYearSpinner();
	
	public CatalogSearchPanel() {
		// TODO Auto-generated constructor stub
	}

}
