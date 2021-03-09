package Database;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;


@Entity
@Table (name = "readers")
@Transactional
@JsonAutoDetect
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "library_card_number")
	Long libraryCardNumber;
    
	@Column(nullable = false)
	String FIO;
	@Column(nullable = false)
	String adress;
    @Column(nullable = false, unique = true)
	String phone;
	
	@Column (name = "passport_number", nullable = false)
	int passportNumber;//6 цифр
}
