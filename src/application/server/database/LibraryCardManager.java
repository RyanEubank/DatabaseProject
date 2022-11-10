package src.application.server.database;

import java.sql.Connection;
import java.sql.SQLException;

import src.application.server.network.ConnectionManager;

public class LibraryCardManager {

	public static void createUser(
		String ssn, String firstname, String lastname, String address, String phone
	) {
		System.out.println("New user added: " + ssn + " " + firstname + " " + lastname + " " + address + " " + phone);
		
	}

}
