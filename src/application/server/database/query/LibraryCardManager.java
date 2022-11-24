package src.application.server.database.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LibraryCardManager extends AbstractUpdateHandler {

	public int createUser(
		String ssn, String firstname, 
		String lastname, String address, String phone
	) throws Exception {
		System.out.println("New user added: " + ssn + " " + firstname + " " + lastname + " " + address + " " + phone);
		String fullName = firstname + " " + lastname;
		return onExecuteWithException(ssn, fullName, address, phone);
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
