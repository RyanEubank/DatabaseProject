package src.application.server.database.query;

import java.sql.*;
import java.time.*;
import java.util.Calendar;

import src.application.server.database.exceptions.*;

public class CheckoutHandler extends AbstractUpdateHandler {

	private int m_borrowerID;
	private String m_isbn;
	
	public int onCheckout(String isbn, int borrowerID, LocalDate currentDate) 
		throws Exception 
	{
		this.m_borrowerID = borrowerID;
		this.m_isbn = isbn;
		
		Date checkout = Date.valueOf(currentDate);
		Date due = getDueDateFromCheckout(checkout);
		return onExecuteWithException(isbn, borrowerID, checkout, due);
	}
	
	/**
	 * Returns the due date based on the given checkout time.
	 * 
	 * @param checkout - the date a book was checked out from
	 *  the library.
	 *  
	 * @return
	 *  Returns a date object representd the date a book is due.
	 */
	private static Date getDueDateFromCheckout(Date checkout) {
		Calendar c = Calendar.getInstance();
		c.setTime(checkout);
		c.add(Calendar.DATE, 14);
		LocalDate dueLocal = c.getTime().toInstant()
			.atZone(ZoneId.systemDefault()).toLocalDate();
		Date due = Date.valueOf(dueLocal);
	    return due;
	}
	
	@Override
	protected String getQuery() {
		return "INSERT INTO Library.Book_Loans (isbn, card_id, date_out, due_date) " 
			 + "VALUES (?, ?, ?, ?);";
	}

	@Override
	protected void setSubqueries(
		PreparedStatement statement, Object... subqueries) throws SQLException 
	{
		// card_id is auto-incremented, do not need to specify in insert
		statement.setString(1, (String) subqueries[0]);
		statement.setInt(2, (int) subqueries[1]);
		statement.setDate(3, (Date) subqueries[2]);
		statement.setDate(4, (Date) subqueries[3]);
		// date in is null for new loans
	}

	/**
	 * Logs the error to console when the update fails and
	 * sets the error to be reinterpreted and rethrown when 
	 * the handler has finished.
	 */
	@Override
	protected void handleException(SQLException e) {
		super.handleException(e);
		String state = e.getSQLState();
		
		if (state.equals(SQLExceptionTypes.USER_TRIGGER)) {
			if (e.getErrorCode() == SQLExceptionTypes.MAX_LOAN_ERROR_CODE)
				this.m_error = new MaximumLoanException(m_borrowerID);
			else if (e.getErrorCode() == SQLExceptionTypes.BOOK_UNAVAILABLE_CODE)
				this.m_error = new BookUnavailableException(m_isbn);
		}
		else if (state.equals(SQLExceptionTypes.INTEGRITY_CONSTRAINT_VIOLATION))
			this.m_error = new UnknownIDException(m_borrowerID);
		else this.m_error = e;
	}
}
