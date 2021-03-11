package Database;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "formulars")
public class Formular implements Serializable {

	@Id
	private int id;
	
//	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reader_card_number")
	Reader reader;

//	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ISBN")
	Book book;

//	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "issue_date", nullable = false)
	Date issueDate;

////	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "return_date")
	Date returnDate;

//	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "librarian_id")
	Librarian librarian;
}
