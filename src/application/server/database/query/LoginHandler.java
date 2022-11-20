package src.application.server.database.query;

import java.sql.*;

import src.application.server.database.ConnectionManager;
import src.application.server.database.exceptions.SQLExceptionTypes;

public class LoginHandler extends AbstractQueryHandler<LoginHandler.LoginStatus> {
	
	public static enum LoginStatus {
		VALID,
		INVALID,
		UNAVAILABLE,
		ERROR;
	}
	
	private LoginStatus m_failure_status;
	
	/**
	 * Attempts to login to the library database with the given
	 * username and password. Returns a login status for valid login,
	 * invalid credentials, or other error.
	 * 
	 * @param username - the username to login to the database.
	 * @param password - the password to login to the database.
	 * 
	 * @return
	 *  a LoginStatus object, either valid for successful login, invalid
	 *  if the username or password was incorrect, or error if there is
	 *  another issue with the databse such as server unavailable.
	 */
	public LoginStatus login(String username, String password) {
		ConnectionManager connectionMgr = ConnectionManager.getSingleton();
		connectionMgr.setUsername(username);
		connectionMgr.setPassword(password);
		return onExecute();
	}
	
	/**
	 * Returns the failure status set from the caught
	 * exception.
	 */
	@Override
	protected LoginStatus defaultResult() {
		return this.m_failure_status;
	}

	/**
	 * Returns a simple show tables query to test the database connection.
	 */
	@Override
	protected String getQuery() {
		return "SHOW TABLES;";
	}

	/**
	 * Does nothing: login has no subqueries
	 */
	@Override
	protected void setSubqueries(
		PreparedStatement statement, String... subqueries
	) {
		return;
	}

	/**
	 * Returns a connection status based on the provided SQLException
	 * and its error state/code.
	 */
	@Override
	protected void handleException(SQLException e) {
		String state = e.getSQLState();

		if (state.equals(SQLExceptionTypes.CONNECTION_FAILED)) 
			this.m_failure_status = LoginStatus.UNAVAILABLE;
		else if (state.equals(SQLExceptionTypes.ACCESS_DENIED))
			this.m_failure_status = LoginStatus.INVALID;
		else 
			this.m_failure_status = LoginStatus.ERROR;
	}

	/**
	 * Tests the connection to the query by executing the given statement.
	 * 
	 * @param ps - the sample statement to execute.
	 * 
	 * @return
	 *  Returns a valid login status if the statement executes sucessfully,
	 *  or an error if an exception is thrown.
	 *  
	 * @throws SQLException 
	 * 	Throws an SQLException if there is an error executing the test statement.
	 */
	@Override
	protected LoginStatus getQueryResult(PreparedStatement statement) throws SQLException {
		ResultSet results = statement.executeQuery();
		System.out.println("Successful login test, returned tables: ");
		while (results.next())
			System.out.println(results.getString(1));
		results.close();
		return LoginStatus.VALID;
	}
}
