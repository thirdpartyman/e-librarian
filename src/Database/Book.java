package Database;
import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import Generic.Filterable;

@Entity
@Table (name = "books")
@Transactional
@JsonAutoDetect
public class Book implements Serializable, Comparable<Book>, Filterable{
    @Id
    public String ISBN;
    
    @Column(nullable = false)
	public String name;
	
 
	@ManyToOne(fetch = FetchType.EAGER)//для @ManyToOne не нужно прописывать CascadeType
	public BBK bbk;
	
	@ManyToOne(fetch = FetchType.EAGER)
	public Author author;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "publish_house")
	public PublishHouse publishHouse;
	
    @Column(name = "release_year")
	public Short releaseYear;
    
    
	@Override
	public int compareTo(Book o) {
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
