package src.application.server.database.query;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class FineInsertHandler extends AbstractUpdateHandler {

	/**
	 * Inserts new fines into the database based for past due books 
	 * based on the specified date.
	 * 
	 * @param current - the date to compare to see if a loan is past due.
	 * 
	 * @return
	 *  Returns the number of new rows inserted into the database.
	 */
	public int insertFines(LocalDate current) {
		return onExecute(Date.valueOf(current));
	}
	
	@Override
	protected String getQuery() {
		return "INSERT INTO Library.Fines (loan_id, fine_amt, paid) " +
			   "SELECT loan_id, 0.25, FALSE FROM Library.Book_Loans AS b " + 
			   "WHERE b.date_in IS NULL AND b.due_date < ?";
	}

	@Override
	protected void setSubqueries(
		PreparedStatement statement, Object... subqueries
	) throws SQLException {
		statement.setDate(1, (Date) subqueries[0]);
	}
}
