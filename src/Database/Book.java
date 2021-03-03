package Database;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@Entity
@Table (name = "books")
@JsonAutoDetect
public class Book {
    @Id
    public String ISBN;
    
    @Column(nullable = false)
	public String name;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public BBK bbk;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public Author author;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "publish_house")
	public PublishHouse publishHouse;
	
	public Short releaseYear;
}
