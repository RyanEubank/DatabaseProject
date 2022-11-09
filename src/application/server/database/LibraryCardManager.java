package src.application.server.database;

public class LibraryCardManager {

	public static void createUser(
		String ssn, String firstname, String lastname, String address, String phone
	) {
		System.out.println("New user added: " + ssn + " " + firstname + " " + lastname + " " + address + " " + phone);
		
	}

}
