package Database;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import Generic.Filterable;
import Util.Utils;

@Entity
@Table (name = "authors", uniqueConstraints=@UniqueConstraint(columnNames = {"name", "birth_year"})) 
@Transactional
@JsonAutoDetect
public class Author implements Serializable, Comparable<Author>, Filterable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    
	@Column (nullable = false)
	public String name;
        
	@Column (name = "birth_year")
	public Short birthYear;
	
    @Override
    public String toString()
    {
		return name + (birthYear != null ? " (" + birthYear + ")" : "");   	
    }
    
    @Override
    public boolean equals(Object obj) { 
    	if (obj == null) return false;
    	if (!(obj instanceof Author)) return false;
    	Author author = (Author)obj;
        return id == author.id && name.equals(author.name) && 
        		((birthYear == null || author.birthYear == null) ? 
        				(birthYear == null && author.birthYear == null) : birthYear.equals(author.birthYear));
    }

	@Override
	public int compareTo(Author o) {
		return name.compareTo(o.name);
	}

	@Override
	public boolean tryfilter(Object obj) {
		return name.startsWith((String)obj);
	}

	@Override
	public Object getFilterableField() {
		return name;
	}
    
}
