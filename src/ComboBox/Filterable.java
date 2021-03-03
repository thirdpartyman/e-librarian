package ComboBox;

public interface Filterable {
	boolean tryfilter(Object obj);
	Object getFilterableField();
}
