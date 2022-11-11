package src.application.server.sql;

import java.sql.*;
import java.util.*;

import src.application.server.network.ConnectionManager;

public class QueryHandler {

	/**
	 * Queries the database with the specified sql statement
	 * and user subqueries and returns a list of the resulting records 
	 * populated by record objects constructed with the specified factory.
	 * 
	 * @param <T> - the type of record the sql statement returns.
	 * 
	 * @param sqlStatement - the statement to query the database with.
	 * @param factory - the factory used to construct record
	 *  objects and populate their fields with the query result.
	 * 
	 * @return
	 * Returns a list of the records retrieved from the database.
	 */
	public static <T> List<T> queryDB(
		String sqlStatement, IResultFactory<T> factory, String... subqueries
	) {
		List<T> extractedValues = null;
		ConnectionManager connMgr = ConnectionManager.getSingleton();
		
		try (
			Connection conn = connMgr.getConnection();
			PreparedStatement statement = conn.prepareStatement(sqlStatement);
		) {
			extractedValues = getQueryResult(factory, statement, subqueries);
		} catch (SQLException e) {
			System.out.println("Query failed.");
			e.printStackTrace();
		}
		
		return Optional.ofNullable(extractedValues).orElse(Collections.emptyList());
	}

	/**
	 * @param <T>
	 * @param factory
	 * @param statement
	 * @param subqueries
	 * @return
	 * @throws SQLException
	 */
	private static <T> List<T> getQueryResult(
		IResultFactory<T> factory, 
		PreparedStatement statement,
		String... subqueries
	) throws SQLException {
		// sanitize and insert subqueries into the sql statement to 
		// prevent injection
		if (subqueries.length > 0) {
			for (int i = 0; i < subqueries.length; i++)
				statement.setString(i, subqueries[i]);
		}
		// execute the query and return the results list
		ResultSet results = statement.executeQuery();
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
	private static <T> List<T> extractResultsToList(
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
	private static <T> void addResult(List<T> list, T record, IResultFactory<T> factory) {
		int index = list.indexOf(record);
		
		if (index >= 0) {
			T duplicate = list.get(index);
			factory.aggregate(duplicate, record);
		} else
			list.add(record);
	}
}
