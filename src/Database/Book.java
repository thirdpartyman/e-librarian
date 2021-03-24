package Database;
import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import Components.Utils;
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
    
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book", cascade = CascadeType.ALL)
    public List<Formular> formular;
    
    
    @Override
    public String toString()
    {
		return '\"' + name + '\"' + (releaseYear != null ? " (" + releaseYear + ")" : "") + " - " + (releaseYear != null ? author.name : "");   	
    }
    
    @Override
    public boolean equals(Object obj) { 
    	if (obj == null) return false;
    	if (!(obj instanceof Book)) return false;
    	Book book = (Book)obj;
        return ISBN.equals(book.ISBN) && 
        		name.equals(book.name) && 
        		((author == null || book.author == null) ? 
        				(author == null && book.author == null) : author.equals(book.author)) && 
        		((publishHouse == null || book.publishHouse == null) ? 
        				(publishHouse == null && book.publishHouse == null) : publishHouse.equals(book.publishHouse)); 
    }
    
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
