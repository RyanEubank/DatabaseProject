package src.application.server.database.query;

import java.sql.*;
import java.util.*;

import src.application.server.database.records.IResultFactory;


public abstract class AbstractSearchHandler<T> extends AbstractQueryHandler<List<T>> {

	private static final String[] EMPTY_QUERY = new String[0];

	protected String m_query;
	
	/**
	 * Performs a query on the library database with the given search
	 * key and filter.
	 * 
	 * @param key - the search term entered by the user.
	 * @param filter - the filter or attribute to match on.
	 * 
	 * @return
	 *  Returns a list of results from the database query.
	 */
	public List<T> onLookup(String key, String filter) {
		setQuery(filter);
		String searchTerms[] = getSearchTerms(key);
		return onExecute(searchTerms);
	}
	
	/**
	 * Returns an empty list by default if the search fails.
	 */
	@Override
	protected List<T> defaultResult() {
		return Collections.emptyList();
	}

	/**
	 * Checks for exxisting search terms input by the user and inserts
	 * them into the search query.
	 */
	@Override
	protected void setSubqueries(
		PreparedStatement statement, String... subqueries
	) throws SQLException {
		if (subqueries.length > 0) {
			for (int i = 0; i < subqueries.length; i++)
				statement.setString(i, subqueries[0]);
		}
	}

	/**
	 * Logs a simple error message when the search fails.
	 */
	@Override
	protected void handleException(SQLException e) {
		System.out.println("Search query failed.");
	}

	/**
	 * Returns the SQL query to perform the requested search.
	 */
	@Override
	protected String getQuery() {
		return this.m_query;
	}
	
	/**
	 * Executes the search query to obtain a result set that is 
	 * collected and returned as a list of records.
	 * 
	 * @param <T> - the type of record the sql statement returns.
	 * 
	 * @param statement - the prepared statement that will be executed.
	 * 
	 * @return
	 *  Returns a list of records from search results.
	 * 
	 * @throws SQLException
	 *  Throws an exception if there is an error with querying the database.
	 */
	@Override
	protected List<T> getQueryResult(PreparedStatement statement) throws SQLException {
		ResultSet results = statement.executeQuery();
		IResultFactory<T> factory = getResultFactory();
		return extractResultsToList(results, factory);
	}

	/**
	 * Goes through each record returned by the query and extracts the
	 * attributes into a record object that is added to the returned list.
	 * 
	 * @param <T> - the type of records returned from the query
	 * 
	 * @param results - the ResultSet object directly returned from executing
	 *  the sql query.
	 * @param factory - the factory object used to extract attributes from the
	 *  ResultSet into record objects.
	 *  
	 * @throws SQLException 
	 *  Throws an exception if there is an issue extracting values from the query,
	 *  like losing database connection.
	 */
	private List<T> extractResultsToList(
		ResultSet queryResult, IResultFactory<T> factory
	) throws SQLException {
		List<T> results = new ArrayList<>();
		
		// go through each returned record and add it to the list or aggregate
		// it if a duplicate is returned (For example books with multiple authors).
		while (queryResult.next()) {
			T record = factory.createInstance(queryResult);
			addResult(results, record, factory);
		}
		return results;
	}

	/**
	 * Checks the list for duplicate records and either absorbs the duplicate
	 * or adds a new unique record to the result list.
	 * 
	 * @param <T> - the type of records returned by the query.
	 * 
	 * @param results - the list to populate with records.
	 * @param record - the next record to either aggregate or add.
	 * @param factory - the factory used to construct or aggregate records.
	 */
	private void addResult(List<T> list, T record, IResultFactory<T> factory) {
		int index = list.indexOf(record);
		if (index >= 0) {
			T duplicate = list.get(index);
			factory.aggregate(duplicate, record);
		} else
			list.add(record);
	}

	/**
	 * Returns the array of earch terms used as subqueries. This should be an 
	 * empty list if there was no search key provided by the user.
	 * 
	 * @param key - the search term to match records on given by the user.
	 * 
	 * @return
	 *  Returns a singleton array or empty array to provide as subqueries
	 *  to the search query.
	 */
	private String[] getSearchTerms(String key) {
		if (key == null || key.isBlank() || key.isEmpty())
			return EMPTY_QUERY;
		else 
			return new String[] {key};
	}
	
	/**
	 * Returns an appropriate SQL query based on the given attribute(s)
	 * to filter on.
	 * 
	 * @param filter - the attribute to filter queries on.
	 * 
	 * @return
	 *  Returns a string containing an SQL query to perform the
	 *  search requested by the user.
	 *
	protected abstract String getQuery(String filter);

	/**
	 * Returns a factory to construct table results from the
	 * query result set.
	 * 
	 * @return
	 *  Returns an IResultFactory for the appropriate record type
	 *  returned from the database query.
	 */
	protected abstract IResultFactory<T> getResultFactory();
	
	/**
	 * Sets the search query used based on the search filter
	 * or attributes to match to the search key.
	 * 
	 * @param filter - the names of the attribute(s) to filter
	 * 				   the search on.
	 */
	protected abstract void setQuery(String filter);
}
