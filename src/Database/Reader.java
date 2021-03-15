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

import Components.Utils;
import Generic.Filterable;


@Entity
@Table (name = "readers")
@Transactional
@JsonAutoDetect
public class Reader implements Serializable, Comparable<Reader>, Filterable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "library_card_number")
	public Long libraryCardNumber;
    
	@Column(nullable = false)
	public String FIO;
	@Column(nullable = false)
	public String adress;
    @Column(nullable = false, unique = true)
    public String phone;
	
	@Column (nullable = false, unique = true)
	public String passport;

    
    @Override
    public String toString()
    {
		return FIO;   	
    }
    
    @Override
    public boolean equals(Object obj) { 
    	if (obj == null) return false;
    	Reader reader = (Reader)obj;
        return libraryCardNumber == reader.libraryCardNumber && FIO.equals(reader.FIO) && phone.equals(reader.phone) && adress.equals(reader.adress) && passport.equals(reader.passport); 
    }
    
	@Override
	public int compareTo(Reader o) {
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
