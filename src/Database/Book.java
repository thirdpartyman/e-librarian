package Database;
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

@Entity
@Table (name = "books")
@Transactional
@JsonAutoDetect
public class Book {
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
}
