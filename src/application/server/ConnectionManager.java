package src.application.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
	
	// Singleton instance for the connection manager
	private static ConnectionManager INSTANCE;
	
	private String m_database; 			// name of the database host
	private String m_port;				// port number for the database
	private String m_user;				// username for database login
	private String m_password;			// password for database login
	private Connection m_connection;	// connection interface to the database
	private Properties m_properties;	// properties for the database connection
	
	/**
	 * Returns an instance of the connection manager, or creates a new
	 * instance if it does not yet exist: i.e. the first call to this
	 * method.
	 * 
	 * @return
	 * 	a static instance of the connection manager for the library system.
	 */
	public static ConnectionManager getSingleton() {
		if (INSTANCE == null)
			INSTANCE = new ConnectionManager();
		return INSTANCE;
	}
	
	/**
	 * Constructs a new ConnectionManager by reading the system configuration
	 * files to get the database host name and port number then establishing
	 * a new connection via JDBC.
	 */
	private ConnectionManager() {
		// this step is not strictly required as of JDBC 4, but included just in case
		registerJDBCDriver();
		
		this.m_properties = new Properties();
		
		//TODO read in the connection info instead of hard-coding values.
		this.m_database = "localhost";
		this.m_port = "3306";
		this.m_user = "Librarian";
		this.m_password = "password";
		
		makeConnection();
	}

	/**
	 * Loads and registers the mysql jdbc driver required to connect
	 * to a mysql database. Logs an error and exits if the driver cannot
	 * be loaded.
	 */
	private void registerJDBCDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch(ClassNotFoundException e) {
			System.out.println("Error: Unable to register ConnectorJ Drivers.");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * Attempts to connect to the library database. Logs an error and exits
	 * if the connection cannot be made.
	 */
	private void makeConnection() {
		String url = "jdbc:mysql://" + this.m_database + ":" + this.m_port + "/";
		m_properties.put("user", m_user);
		m_properties.put("password", m_password);
		try {
			this.m_connection = DriverManager.getConnection(url, m_properties);
		} catch (SQLException e) {
			System.out.println("Error: Unable to establish connection with database.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Get method to retrieve the database connection for the system.
	 * 
	 * @return
	 * 	The connection instance to the library database.
	 */
	public Connection getConnection() {
		return this.m_connection;
	}
}
