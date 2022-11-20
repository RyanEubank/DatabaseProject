package src.application.server.database.records;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IResultFactory<T> {

	/**
	 * Creates a new instance of a record object by extracting attribute values
	 * into the corresponding fields in the record.
	 * 
	 * @param results - The ResultSet returned from executing an SQL query.
	 * 
	 * @return
	 *  The next record in the reult set.
	 * 
	 * @throws SQLException
	 *  Throws an exception if there is an issue extracting records.
	 */
	public T createInstance(ResultSet results) throws SQLException;

	/**
	 * Combines duplicate and existing records identified by record.equals().
	 * For example, two records for the same book with multiple
	 * authors should include the duplicate's author in the existing record's
	 * author list.
	 * 
	 * @param duplicate - the duplicate record to aggregate
	 * @param record - the existing record to add to
	 */
	public void aggregate(T duplicate, T record);

}
