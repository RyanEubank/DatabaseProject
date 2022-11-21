package src.application.server.database.query;

import java.sql.*;
import src.application.server.database.exceptions.LibraryRuleException;

public class FinePaymentHandler extends AbstractUpdateHandler {

	public void onPayFine(int loanID) throws LibraryRuleException, SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setSubqueries(PreparedStatement statement, Object... subqueries) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
