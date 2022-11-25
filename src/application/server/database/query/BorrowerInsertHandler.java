package src.application.server.database.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import src.application.server.database.exceptions.*;

public class BorrowerInsertHandler extends AbstractUpdateHandler {

	private String m_ssn;
	private String m_name;
	private String m_address;
	
	/**
	 * Formats the specified ssn and phone number as
	 * sss-ss-ssss and (nnn)-nnn-nnnn respectively then
	 * puts the new user's info together in an insert query to the 
	 * database.
	 * 
	 * @param ssn - the ssn of the new user.
	 * @param fullName - the name of the new user.
	 * @param address - the address of the new user.
	 * @param phone - the phone number of the new user.
	 * 
	 * @return
	 *  Returns the formatted ssn string so the database can
	 *  be querired for the new borrower's card ID.
	 *  
	 * @throws Exception
	 *  Throws an exception if there is an error with the insert
	 *  or other connection issue.
	 */
	public String createUser(
		String ssn, String fullName, String address, String phone
	) throws Exception {
		this.m_ssn = formatSsn(ssn);
		this.m_name = fullName;
		this.m_address = address;
		String formattedPhone = formatPhone(phone);
		onExecuteWithException(m_ssn, fullName, address, formattedPhone);
		return this.m_ssn;
	}

	/**
	 * Formats a phone unmber from a numeric string. String must 
	 * already be checked to ensure it is made of 10 digits.
	 * 
	 * @param phone - the phone number to format.
	 * 
	 * @return
	 *  Returns a new string with the same digits formmatted
	 *  as (nnn)-nnn-nnnn
	 */
	private String formatPhone(String phone) {
		if (phone.isEmpty() || phone.isBlank())
			return null;
		return "(" + phone.substring(0, 3) + ")-" + 
				phone.substring(3, 6) + "-" + phone.substring(6, 10); 
	}

	/**
	 * Formats a ssn from a numeric string. String must already
	 * be checked to ensure it is made of 9 digits.
	 * 
	 * @param ssn - the ssn to format.
	 * 
	 * @return
	 *  Returns a new string with the same digits formmatted
	 *  as nnn-nn-nnnn
	 */
	private String formatSsn(String ssn) {
		return ssn.substring(0, 3) + "-" + 
			   ssn.substring(3, 5) + "-" + ssn.substring(5, 9);
	}

	/**
	 * Returns the SQL query to to insert the new record into the database.
	 */
	@Override
	protected String getQuery() {
		return "INSERT INTO Library.Borrower VALUES (null, ?, ?, ?, ?);";
	}

	/**
	 * Fills in all of the subqueries with user input safely.
	 */
	@Override
	protected void setSubqueries(
		PreparedStatement statement, Object... subqueries
	) throws SQLException {
		for (int i = 1; i <= subqueries.length; i++)
			statement.setString(i, (String) subqueries[i - 1]);
	}

	/**
	 * Logs the error stacktrace and sets the handler's error based
	 * on the given exception's state.s
	 */
	@Override
	protected void handleException(SQLException e) {
		super.handleException(e);
		String state = e.getSQLState();
		
		if (state.equals(SQLExceptionTypes.INTEGRITY_CONSTRAINT_VIOLATION)) {
			if (e.getMessage().contains(SQLExceptionTypes.DUPLICATE_NAME_ADDRESS))
				this.m_error = new DuplicateBorrowerException(this.m_name, this.m_address);
			else
				this.m_error = new DuplicateBorrowerException(this.m_ssn);
		} else
			this.m_error = e;
	}
}
