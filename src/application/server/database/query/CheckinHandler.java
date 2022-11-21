package src.application.server.database.query;

import java.sql.*;
import java.time.LocalDate;

import src.application.server.database.exceptions.LibraryRuleException;

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
	 * Attempts to check in a loan with the given load ID and
	 * date of return.
	 * 
	 * @param loanID - the loan ID of the book loan to update.
	 * @param dayIn - the check in date to update the loan with.
	 * @return
	 *
	public static boolean checkinBook(int loanID, LocalDate dayIn) {
		ConnectionManager connMgr = ConnectionManager.getSingleton();
		String updateLoanStatement = "UPDATE Library.Book_Loans " + 
									 "SET date_in = ? WHERE loan_id = ?";
		try (
			Connection conn = connMgr.getConnection();
			PreparedStatement ps = conn.prepareStatement(updateLoanStatement);
		) {
			executeCheckinQuery(loanID, dayIn, ps);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param loanID
	 * @param dayIn
	 * @param ps
	 * @throws SQLException
	 *
	private static void executeCheckinQuery(
		int loanID, LocalDate dayIn, PreparedStatement ps
	) throws SQLException {
		ps.setDate(1, Date.valueOf(dayIn));
		ps.setInt(2, loanID);
		ps.executeUpdate();
	} */
}
