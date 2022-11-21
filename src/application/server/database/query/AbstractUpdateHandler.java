package src.application.server.database.query;

import java.sql.*;

import src.application.server.database.exceptions.LibraryRuleException;

public abstract class AbstractUpdateHandler extends AbstractQueryHandler<Integer> {
	
	protected LibraryRuleException m_error = null;
	
	/**
	 * Executes the handler's query with the specified subqueries
	 * inserted in it if applicable. Subqueries do not have to be supplied.
	 * Any errors caught suring execution are interpreted and rethrown.
	 * 
	 * @param subqueries - the subqueries to insert into the 
	 * 					   handler's SQL query.
	 * @return
	 *  Returns the result of the handler's query or default result if
	 *  an error occurs.
	 *  
	 * @throws
	 *  Throws a library rule exception if the insert or update fails
	 *  due to an integrity violation or other business rule.
	 */
	protected int onExecuteWithException(Object... subqueries) 
		throws LibraryRuleException, SQLException
	{
		int numRowsAffected = onExecute(subqueries);
		if (m_error != null)
			throw m_error;
		return numRowsAffected;
	}
	
	/**
	 * By default if there is an error then 0 rows have been updated.
	 */
	@Override
	protected Integer defaultResult() {
		return 0;
	}

	/**
	 * Logs a simple error message to console when the update fails.
	 */
	@Override
	protected void handleException(SQLException e) {
		System.out.println("Update failed.");
	}

	/**
	 * Returns the number of rows affected by the database update.
	 */
	@Override
	protected Integer getQueryResult(PreparedStatement statement) 
		throws SQLException 
	{
		int affectedRows = statement.executeUpdate();
		return affectedRows;
	}
}
