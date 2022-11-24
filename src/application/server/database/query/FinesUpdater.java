package src.application.server.database.query;

import java.sql.*;
import java.time.LocalDate;

public class FinesUpdater extends AbstractUpdateHandler {
	
	/**
	 * Updates the fines accrued for the provided date.
	 * 
	 * @param next - the new date to update the database with.
	 */
	public static void setDate(LocalDate next) {
		
	}

	@Override
	protected String getQuery() {
		return "source update_fines.sql;";
	}
	
	@Override
	protected void setSubqueries(
		PreparedStatement statement, Object... subqueries) throws SQLException 
	{
		// no subqueries needed
	}
}
