package src.application.server.network;

import java.sql.*;

import src.application.server.sql.SQLExceptionTypes;

public class LoginHandler {
	
	public static enum LoginStatus {
		VALID,
		INVALID,
		UNAVAILABLE,
		ERROR;
	}
	
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
	public static LoginStatus login(String username, String password) {
		ConnectionManager connectionMgr = ConnectionManager.getSingleton();
		connectionMgr.setUsername(username);
		connectionMgr.setPassword(password);
		return loginAndValidate(connectionMgr);
	}

	/**
	 * Sets up and executes a query to count the number of user/password records
	 * matching the given username and password strings.
	 * 
	 * @param connectionMgr - the connection manager instance handling database 
	 * connecction.
	 * 
	 * @return
	 * 	a login status object, either valid login, invalid, or connection error.
	 */
	private static LoginStatus loginAndValidate(ConnectionManager connectionMgr) {
		LoginStatus status;
		// pass a generic query to the server to test valid connection
		String validationQuery = "SELECT COUNT(*) from library.books";
		
		// automatically closes connection resources in try block.
		try (
			Connection conn = connectionMgr.getConnection(); 
			PreparedStatement statement = conn.prepareStatement(validationQuery);
			ResultSet results = statement.executeQuery();
		) {
			return (results != null) ? LoginStatus.VALID : LoginStatus.ERROR;
		} catch(SQLException e) {
			status = handleException(e);
		}
		return status;
	}
	
	/**
	 * Determines if exception occured due to invalid credentials or from 
	 * another connectivity error.
	 * 
	 * @param e - the exception to be handled.
	 * 
	 * @return
	 * 	either an error status or invalid credentials.
	 */
	private static LoginStatus handleException(SQLException e) {
		e.printStackTrace();
		
		// occurs if the server is not running or not configured to listen on the network
		if (e.getSQLState().equals(SQLExceptionTypes.CONNECTION_FAILED)) 
			return LoginStatus.UNAVAILABLE;
		// occurs for invalid username/password
		else if (e.getSQLState().equals(SQLExceptionTypes.ACCESS_DENIED))
			return LoginStatus.INVALID;
		else // otherwise there was an sql syntax error or access was denied.
			return LoginStatus.ERROR;
	}
}
