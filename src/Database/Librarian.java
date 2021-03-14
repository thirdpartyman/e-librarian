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

@Entity
@Table (name = "librarians")
@Transactional
@JsonAutoDetect
public class Librarian implements Serializable {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
    
    @Column(nullable = false)
    public String adress;
    
    @Column(nullable = false, unique = true)
    public String phone;
    
    @Column(nullable = false)
    public String FIO;
    
    @Column(nullable = false)
    public String password;//хеш пароля
}
