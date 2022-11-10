package src.application.server.sql;

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
	 * 
	 * @param duplicate
	 * @param record
	 */
	public void aggregate(T duplicate, T record);

}
