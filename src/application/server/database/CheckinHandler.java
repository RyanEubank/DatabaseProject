package src.application.server.database;

import java.sql.*;
import java.time.LocalDate;

import src.application.server.network.ConnectionManager;

public class CheckinHandler {

	/**
	 * 
	 * @param loanID
	 * @param dayIn
	 * @return
	 */
	public static boolean checkinBook(int loanID, LocalDate dayIn) {
		ConnectionManager connMgr = ConnectionManager.getSingleton();
		String updateLoanStatement = "UPDATE Library.Book_Loans SET date_in = ? WHERE loan_id = ?";
		
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
	 */
	private static void executeCheckinQuery(int loanID, LocalDate dayIn, PreparedStatement ps) throws SQLException {
		ps.setDate(1, Date.valueOf(dayIn));
		ps.setInt(2, loanID);
		ps.executeUpdate();
	}
}
