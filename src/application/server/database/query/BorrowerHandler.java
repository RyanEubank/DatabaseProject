package src.application.server.database.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import src.application.server.database.exceptions.*;

public class BorrowerHandler extends AbstractUpdateHandler {

	private String m_ssn;
	
	public int createUser(
		String ssn, String fullName, String address, String phone
	) throws Exception {
		this.m_ssn = formatSsn(ssn);
		String formattedPhone = formatPhone(phone);
		return onExecuteWithException(m_ssn, fullName, address, formattedPhone);
	}

	private String formatPhone(String phone) {
		if (phone.isEmpty() || phone.isBlank())
			return null;
		return "(" + phone.substring(0, 3) + ")-" + phone.substring(3, 6) + "-" + phone.substring(6, 10); 
	}

	private String formatSsn(String ssn) {
		return ssn.substring(0, 3) + "-" + ssn.substring(3, 5) + "-" + ssn.substring(5, 9);
	}

	@Override
	protected String getQuery() {
		return "INSERT INTO Library.Borrower VALUES (null, ?, ?, ?, ?);";
	}

	@Override
	protected void setSubqueries(
		PreparedStatement statement, Object... subqueries
	) throws SQLException {
		for (int i = 1; i <= subqueries.length; i++)
			statement.setString(i, (String) subqueries[i - 1]);
	}

	/**
	 * Logs a simple error message to console when the update fails.
	 */
	@Override
	protected void handleException(SQLException e) {
		super.handleException(e);
		String state = e.getSQLState();
		
		if (state.equals(SQLExceptionTypes.INTEGRITY_CONSTRAINT_VIOLATION)) {
			this.m_error = new DuplicateBorrowerException(this.m_ssn);
		}
	}
}
