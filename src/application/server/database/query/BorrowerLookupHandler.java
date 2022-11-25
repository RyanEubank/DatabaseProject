package src.application.server.database.query;

import java.sql.*;

public class BorrowerLookupHandler extends AbstractQueryHandler<Integer> {

	public int lookup(String formattedSsn) {
		return onExecute(formattedSsn);
	}
	
	@Override
	protected Integer defaultResult() {
		return -1;
	}

	@Override
	protected String getQuery() {
		return "SELECT b.card_id FROM Library.Borrower AS b " +
			   "WHERE b.ssn = ?";
	}

	@Override
	protected void setSubqueries(
		PreparedStatement statement, Object... subqueries
	) throws SQLException {
		statement.setString(1, (String) subqueries[0]);
	}

	@Override
	protected void handleException(SQLException e) {
		e.printStackTrace();
	}

	@Override
	protected Integer getQueryResult(PreparedStatement statement) 
		throws SQLException 
	{
		ResultSet rs = statement.executeQuery();
		rs.next();
		return rs.getInt(1);
	}
}
