package src.application.server.database.query;

import java.sql.*;
import java.util.Optional;

import src.application.server.database.ConnectionManager;

public abstract class AbstractQueryHandler<T> {

	/**
	 * Executres the handler's query with the specified subqueries
	 * inserted in it if applicable. Subqueries do not have to be supplied.
	 * 
	 * @param subqueries - the subqueries to insert into the 
	 * 					   handler's SQL query.
	 * @return
	 *  Returns the result of the handler's query or default result if
	 *  an error occurs.
	 */
	public T onExecute(String... subqueries) {
		String query = getQuery();
		return executeQuery(query, subqueries);
	}
	
	/**
	 * Gets a connection to the database and executes the handler's query
	 * returning the result of the SQL statement.
	 * 
	 * @param query - the query to execute.
	 * @param subqueries - subqueries to insert into the main query.
	 * 
	 * @return
	 * Returns the result of the query.
	 */
	protected T executeQuery(String query, String... subqueries) {
		T result = null;
		ConnectionManager connectionMgr = ConnectionManager.getSingleton();
		
		try (
			Connection conn = connectionMgr.getConnection();
			PreparedStatement statement = conn.prepareStatement(query);	
		) {
			setSubqueries(statement, subqueries);
			result =  getQueryResult(statement);
		} catch (SQLException e) {
			e.printStackTrace();
			handleException(e);
		}
		
		return Optional.ofNullable(result).orElse(defaultResult());
	}

	/**
	 * Returns the default value for the query.
	 * 
	 * @return
	 *  Returns the object used as the default value in the event 
	 *  that the query fails.
	 */
	protected abstract T defaultResult();

	/**
	 * Returns a valid SQL statement to send to the database
	 * to perform the handler's action.
	 * 
	 * @return
	 *  Returns a string containing the SQL statement to execute.
	 */
	protected abstract String getQuery();
	
	/**
	 * Inserts the given subqueries into a prepared statement
	 * to ensure full query is formatted correctly and
	 * user subqueries are sanitized to prevent SQL injection attacks.
	 * 
	 * @param statement - the statement being populated with subqueries.
	 * @param subqueries - the subqueries to insert.
	 * 
	 * @throws SQLException
	 * 	Throws an SQL Exception if there is an error while compiling
	 *  the statement.
	 */
	protected abstract void setSubqueries(
		PreparedStatement statement, String... subqueries) throws SQLException;
	
	/**
	 * Handles a SQLException raises if the query fails.
	 * 
	 * @param e - the exception object being handled.
	 */
	protected abstract void handleException(SQLException e);
	
	/**
	 * Executes the query and returns the result.
	 * 
	 * @param statement - the prepared SQL statement to execute.
	 * 
	 * @return
	 *  Returns the result object returned by the SQL statement.
	 *  
	 * @throws SQLException 
	 *  Throws an SQL Exception if there is an error while executing
	 *  the statement.
	 */
	protected abstract T getQueryResult(PreparedStatement statement) 
		throws SQLException;
}
