package src.application.server.database;

import java.time.LocalDate;

import javafx.beans.property.*;

public class LoanSearchResult {

	private final SimpleIntegerProperty m_loanID;
	private final SimpleIntegerProperty m_borrowerID;
	private final SimpleStringProperty m_name;
	private final SimpleStringProperty m_isbn;
	private final SimpleObjectProperty<LocalDate> m_checkoutDate;
	private final SimpleObjectProperty<LocalDate> m_dueDate;
	private final SimpleObjectProperty<LocalDate> m_checkinDate;
	
	public LoanSearchResult(
		int loanID, int borrowerID, String name, String isbn, 
		LocalDate checkout, LocalDate due, LocalDate checkin
	) {
		this.m_loanID = new SimpleIntegerProperty(loanID);
		this.m_borrowerID = new SimpleIntegerProperty(borrowerID);
		this.m_name = new SimpleStringProperty(name);
		this.m_isbn = new SimpleStringProperty(isbn);
		this.m_checkoutDate = new SimpleObjectProperty<LocalDate>(checkout);
		this.m_dueDate = new SimpleObjectProperty<LocalDate>(due);
		this.m_checkinDate = new SimpleObjectProperty<LocalDate>(checkin);
	}
	
	public int getLoanID() {
		return this.m_loanID.get();
	}
	
	public int getBorrowerID() {
		return this.m_borrowerID.get();
	}

	public String getName() {
		return this.m_name.get();
	}
	
	public String getISBN() {
		return this.m_isbn.get();
	}
	
	public LocalDate getCheckoutDate() {
		return this.m_checkoutDate.get();
	}
	
	public LocalDate getDueDate() {
		return this.m_dueDate.get();
	}
	
	public LocalDate getCheckinDate() {
		return this.m_checkinDate.get();
	}
}
