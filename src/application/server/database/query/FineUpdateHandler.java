package src.application.server.database.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FineUpdateHandler extends AbstractUpdateHandler {

	/**
	 * Performs update queries to update fines for books that
	 * are past due and not yet checked in.
	 * 
	 * @return
	 *  Returns the number of rows affected by the update.
	 */
	public int updateFines() {
		return onExecute();
	}
	
	/**
	 * Returns a query to add $0.25 to any fine in the fines table that
	 * matches a loan ID that has yet to be checked in.
	 */
	@Override
	protected String getQuery() {
		return "UPDATE Library.Fines AS f SET f.amount = f.amount + 0.25 " +
			   "WHERE f.loan_id IN (" + 
			   		"SELECT * FROM Library.Fines as f2 " +
			   		"JOIN Library.Book_Loans as b ON f2.loan_id = b.loan_id " +
			   		"WHERE b.date_in IS NULL);";
	}

	@Override
	protected void setSubqueries(
		PreparedStatement statement, Object... subqueries
	) throws SQLException {
		// does nothing, no subqueires in update.
	}

}
