package Database;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import Generic.Filterable;

@Entity
@Table (name = "librarians")
@Transactional
@JsonAutoDetect
public class Librarian implements Serializable, Comparable<Librarian>, Filterable {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
    
    @Column(nullable = false)
    public String FIO;
   
    @Column(nullable = false, unique = true)
    public String phone;
    
    @Column(nullable = false)
    public String adress;

    
    @Column(nullable = false)
    public String password;//хеш пароля
    
    
    @Override
    public String toString()
    {
		return FIO;   	
    }
    
	@Override
	public int compareTo(Librarian o) {
		return FIO.compareTo(o.FIO);
	}

	@Override
	public boolean tryfilter(Object obj) {
		return FIO.contains((String)obj);
	}

	@Override
	public Object getFilterableField() {
		return FIO;
	}
}
