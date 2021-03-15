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

import Components.Utils;
import Generic.Filterable;

@Entity
@Table (name = "publish_houses", uniqueConstraints=@UniqueConstraint(columnNames = {"name", "city"})) 
@Transactional
@JsonAutoDetect
public class PublishHouse implements Serializable, Comparable<PublishHouse>, Filterable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    
	@Column (nullable = false)
	public String name;
    
	@Column (nullable = false)
	public String city;
	
	
    @Override
    public String toString()
    {
		return '"' + name + '"' + ", " + city;   	
    }
    
    @Override
    public boolean equals(Object obj) { 
    	if (obj == null) return false;
    	PublishHouse publishHouse = (PublishHouse)obj;
        return id == publishHouse.id && name.equals(publishHouse.name) && city.equals(publishHouse.city); 
    }

	@Override
	public int compareTo(PublishHouse o) {
		return name.compareTo(o.name);
	}

	@Override
	public boolean tryfilter(Object obj) {
		return name.contains((String)obj);
	}

	@Override
	public Object getFilterableField() {
		return name;
	}
}
