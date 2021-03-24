package Database;
import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import Components.Utils;
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
    
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reader", cascade = CascadeType.ALL)
    public List<Formular> issues;//выдачи
    
    
    @Override
    public String toString()
    {
		return FIO;   	
    }
    
    @Override
    public boolean equals(Object obj) { 
    	if (obj == null) return false;
    	if (!(obj instanceof Librarian)) return false;
    	Librarian librarian = (Librarian)obj;
        return id == librarian.id && FIO.equals(librarian.FIO) && phone.equals(librarian.phone) && adress.equals(librarian.adress) && password.equals(librarian.password); 
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
