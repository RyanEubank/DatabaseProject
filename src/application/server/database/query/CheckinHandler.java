package src.application.server.database.query;

import java.sql.*;
import java.time.LocalDate;

import src.application.server.database.exceptions.*;

public class CheckinHandler extends AbstractUpdateHandler {

	private int m_loanID;
	
	public int onCheckin(int loanID) 
		throws Exception 
	{
		this.m_loanID = loanID;
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
			this.m_error = new AlreadyCheckedInException(m_loanID);
		}
		else if (state.equals(SQLExceptionTypes.INTEGRITY_CONSTRAINT_VIOLATION))
			this.m_error = new UnknownIDException(m_loanID);
		else this.m_error = e;
	}
}
