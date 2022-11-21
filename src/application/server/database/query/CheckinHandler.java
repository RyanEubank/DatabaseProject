package src.application.server.database.query;

import java.sql.*;
import java.time.LocalDate;

import src.application.server.database.exceptions.BookUnavailableException;
import src.application.server.database.exceptions.LibraryRuleException;
import src.application.server.database.exceptions.MaximumLoanException;
import src.application.server.database.exceptions.SQLExceptionTypes;
import src.application.server.database.exceptions.UnknownBorrowerException;

public class CheckinHandler extends AbstractUpdateHandler {

	public int onCheckin(int loanID) 
		throws LibraryRuleException, SQLException 
	{
		Date checkinDate = Date.valueOf(LocalDate.now());
		return onExecuteWithException(checkinDate, loanID);
	}
	
	@Override
	protected String getQuery() {
		return "UPDATE Library.Book_Loans " + 
			   "SET date_in = ? WHERE loan_id = ?";
	}

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
			if (e.getErrorCode() == SQLExceptionTypes.MAX_LOAN_ERROR_CODE)
				this.m_error = new MaximumLoanException(m_borrowerID);
			else
				this.m_error = new BookUnavailableException(m_isbn);
		}
		if (state.equals(SQLExceptionTypes.INTEGRITY_CONSTRAINT_VIOLATION))
			this.m_error = new UnknownBorrowerException(m_borrowerID);
	}
}
