package src.application.server.database.query;

import java.sql.*;
import java.time.LocalDate;

import src.application.server.database.exceptions.*;

public class CheckinHandler extends AbstractUpdateHandler {

	private int m_loanID;
	
	/**
	 * Attempts to update the record with the given loanID to set
	 * it as checked in at the specified date.
	 * 
	 * @param loanID - the loanID of the record to update.
	 * @param currentDate - the current date the book is checked in.
	 * 
	 * @return
	 *  Returns the number of rows updated.
	 *  
	 * @throws Exception
	 *  Throws an excpetion if there is an error updating the database.
	 */
	public int onCheckin(int loanID, LocalDate currentDate) 
		throws Exception 
	{
		this.m_loanID = loanID;
		Date checkinDate = Date.valueOf(currentDate);
		return onExecuteWithException(checkinDate, loanID);
	}
	
	/**
	 * Returns an SQL query to update the correct loan with
	 * the gioven date.
	 */
	@Override
	protected String getQuery() {
		return "UPDATE Library.Book_Loans " + 
			   "SET date_in = ? WHERE loan_id = ?";
	}

	/**
	 * Prepares the SQL statement by setting the correct fields with
	 * the given subqueries.
	 */
	@Override
	protected void setSubqueries(
		PreparedStatement statement, Object... subqueries) throws SQLException 
	{
		statement.setDate(1, (Date) subqueries[0]);
		statement.setInt(2, (int) subqueries[1]);
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
			this.m_error = new AlreadyCheckedInException(m_loanID);
		}
		else if (state.equals(SQLExceptionTypes.INTEGRITY_CONSTRAINT_VIOLATION))
			this.m_error = new UnknownIDException(m_loanID);
		else this.m_error = e;
	}
}
