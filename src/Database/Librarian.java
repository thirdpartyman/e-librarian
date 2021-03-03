package Database;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "librarians")
public class Librarian implements Serializable {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
    
    @Column(nullable = false)
    String adress;
    
    @Column(nullable = false, unique = true)
	String phone;
    
    @Column(nullable = false)
	String FIO;
    
    @Column(nullable = false)
	String password;//хеш пароля
}
