package src.application.server.database;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

import src.application.server.network.ConnectionManager;

public class CheckoutHandler {

	/**
	 * 
	 * @param isbn
	 * @param borrowerID
	 * @return
	 */
	public static boolean checkoutBook(String isbn, int borrowerID) {
		ConnectionManager connMgr = ConnectionManager.getSingleton();
		String insertLoanStatement = "INSERT INTO Library.Book_Loans (isbn, card_id, date_out, due_date) VALUES (?, ?, ?, ?);";
		
		try (
			Connection conn = connMgr.getConnection();
			PreparedStatement ps = conn.prepareStatement(insertLoanStatement);
		) {
			insertValuesIntoStatement(conn, ps, isbn, borrowerID);
			int rowCount = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param conn
	 * @param ps
	 * @param isbn
	 * @param borrowerID
	 * @throws SQLException
	 */
	private static void insertValuesIntoStatement(
		Connection conn, PreparedStatement ps, String isbn, int borrowerID
	) throws SQLException {
		Date checkout = Date.valueOf(LocalDate.now());
		Date due = getDueDateFromCheckout(checkout);
		setStatementSubqueries(ps, isbn, borrowerID, checkout, due);
	}

	/**
	 * 
	 * @param ps
	 * @param isbn
	 * @param borrowerID
	 * @param checkout
	 * @param due
	 * @throws SQLException
	 */
	private static void setStatementSubqueries(
		PreparedStatement ps, String isbn, 
		int borrowerID, Date checkout, Date due
	) throws SQLException {
		// card_id is auto-incremented, do not need to specify in insert
		ps.setString(1, isbn);
		ps.setInt(2, borrowerID);
		ps.setDate(3, checkout);
		ps.setDate(4, due);
		// date in is null for new loans
	}

	/**
	 * Returns the due date based on the given checout time.
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
}
