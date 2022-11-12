package src.application.server.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.beans.property.*;
import src.application.server.sql.IResultFactory;

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
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof LoanSearchResult))
			return false;
		return this.getLoanID() == ((LoanSearchResult) o).getLoanID();
	}
	
	public static class Builder implements IResultFactory<LoanSearchResult> {

		@Override
		public LoanSearchResult createInstance(ResultSet results) throws SQLException {
			int loanID = results.getInt("loan_id");
			int borrowerID = results.getInt("card_id");
			String name = results.getString("bname");
			String isbn = results.getString("isbn");
			LocalDate checkedOut = results.getDate("date_out").toLocalDate();
			LocalDate due = results.getDate("due_date").toLocalDate();
			LocalDate checkedIn = results.getDate("date_in").toLocalDate();
			return new LoanSearchResult(loanID, borrowerID, name, isbn, checkedOut, due, checkedIn);
		}

		@Override
		public void aggregate(LoanSearchResult duplicate, LoanSearchResult record) {
			// unlike books, there are no multivalued attributes in loans so no 
			// duplicates should appear in query
			throw new RuntimeException("Duplicate loan returned from search.");
		}
		
	}
}
