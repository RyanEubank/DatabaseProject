package src.application.server.database.query;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class FineUpdateHandler extends AbstractUpdateHandler {

	/**
	 * Performs update queries to update fines for books that
	 * are past due and not yet checked in.
	 *
	 * @param current - the date to compare to see if a loan is past due.
	 * 
	 * @return
	 *  Returns the number of rows affected by the update.
	 */
	public int updateFines(LocalDate current) {
		return onExecute(Date.valueOf(current));
	}
	
	/**
	 * Returns a query to add $0.25 to any fine in the fines table that
	 * matches a loan ID that has yet to be checked in.
	 */
	@Override
	protected String getQuery() {
		return "UPDATE Library.Fines AS f JOIN Library.Book_Loans AS b " + 
			   "ON f.loan_id = b.loan_id " +
			   "SET f.fine_amt = f.fine_amt + 0.25 " + 
			   "WHERE f.paid = FALSE AND b.date_in IS NULL AND b.due_date < ?";
	}

	@Override
	protected void setSubqueries(
		PreparedStatement statement, Object... subqueries
	) throws SQLException {
		statement.setDate(1, (Date) subqueries[0]);
	}

}
