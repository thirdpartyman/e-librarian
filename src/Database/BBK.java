package Database;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import Components.Utils;
import Generic.Filterable;

@Entity
@Table (name = "bbk")
@Transactional
@JsonAutoDetect
public class BBK implements Serializable, Comparable<BBK>, Filterable{
	public BBK() {}
    public BBK(String index, String text) {
    	this.index = index;
		this.text = text;
	}

	@Id
	@Column (name = "id")
	public String index;
    
    @Column(nullable = false)
    public String text;
    
    @Override
    public String toString()
    {
		return index + ' ' + text;   	
    }
    
    @Override
    public boolean equals(Object obj) { 
    	if (obj == null) return false;
    	BBK bbk = (BBK)obj;
        return index.equals(bbk.index) && text.equals(bbk.text); 
    }
    
	@Override
	public int compareTo(BBK o) {
		return index.compareTo(o.index);
	}
	
	@Override
	public boolean tryfilter(Object obj) {
		return index.startsWith((String)obj);
	}
	
	@Override
	public Object getFilterableField() {
		return index;
	}
}
