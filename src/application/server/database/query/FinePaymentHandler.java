package src.application.server.database.query;

import java.sql.*;

import src.application.server.database.exceptions.*;

public class FinePaymentHandler extends AbstractUpdateHandler {

	private int m_loanID;
	
	/**
	 * Updates the library database by setting the paid status of
	 * the given loanID to true.
	 * 
	 * @param loanID - the ID of the loan to be paid.
	 * 
	 * @return
	 *  Returns the number of rows updated from the transaction.
	 *  
	 * @throws Exception
	 *  Throws an exception if the loan is already paid or there
	 *  is a problem connecting to the database.
	 */
	public int onPayFine(int loanID) 
		throws Exception 
	{
		this.m_loanID = loanID;
		return onExecuteWithException(loanID);
	}

	/**
	 * Returns the appropriate query to update a
	 * library fine and set its paid status.
	 */
	@Override
	protected String getQuery() {
		return "UPDATE Library.Fines " + 
			   "SET paid = TRUE WHERE loan_id = ?";
	}

	/**
	 * Sets the loanID to be updated in the query.
	 */
	@Override
	protected void setSubqueries(
		PreparedStatement statement, Object... subqueries) 
		throws SQLException 
	{
		statement.setInt(1, (int) subqueries[0]);
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
			if (e.getErrorCode() == SQLExceptionTypes.PAY_FINE_NOT_CHECKED_IN)
				this.m_error = new CannotPayException(m_loanID);
			else
				this.m_error = new AlreadyPaidException(m_loanID);
		}
		else if (state.equals(SQLExceptionTypes.INTEGRITY_CONSTRAINT_VIOLATION))
			this.m_error = new UnknownIDException(m_loanID);
		else this.m_error = e;
	}
}
