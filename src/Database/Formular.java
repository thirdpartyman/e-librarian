package Database;

import java.awt.Color;
import java.io.Serializable;
import java.util.Calendar;
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

import Generic.Filterable;
import Util.Utils;

@Entity
@Table(name = "formulars")
public class Formular implements Serializable {

//	@Id
//	private int id;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "reader_card_number")
	public Reader reader;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "book_id")
	public Book book;

	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "issue_date", nullable = false)
	public Date issueDate;

//	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "return_date")
	public Date returnDate;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "librarian_id")
	public Librarian librarian;
	
	boolean isIssued()
	{
		return returnDate == null;
	}
	
	boolean isExpired()
	{
		Calendar instance = Calendar.getInstance();
		instance.setTime(issueDate);
		instance.add(Calendar.DATE, Settings.ApplicationSettings.Configuration.maxPeriodBookHolding);
		Date returnDate = instance.getTime();
		Date nowDate = Utils.removeTime(new Date());
		return !returnDate.after(nowDate);
	}
	
	
    @Override
    public boolean equals(Object obj) { 
    	if (obj == null) return false;
    	if (!(obj instanceof Formular)) return false;
    	Formular formular = (Formular)obj;
        return reader.equals(formular.reader) && book.equals(formular.book) && librarian.equals(formular.librarian) && issueDate.equals(formular.issueDate); 
    }
}
