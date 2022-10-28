package src.application.server.network;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import src.application.server.ConfigReader;
import src.application.server.ConfigReader.ConfigFormatException;

public class ConnectionManager {

	// Singleton instance for the connection manager
	private static ConnectionManager INSTANCE;
	
	private String m_url;				// the full connection url to the database
	private Properties m_properties;	// current properties for the database connection

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
	 * Sets the database login username.
	 * @param username - the username to connect to the library database with.
	 */
	public void setUsername(String username) {
		if (this.m_properties.containsKey("user"))
			this.m_properties.replace("user", username);
		this.m_properties.put("user", username);
	}
	
	/**
	 * Sets the database login password.
	 * @param password - the password to connect to the library database with.
	 */
	public void setPassword(String password) {
		if (this.m_properties.containsKey("password"))
			this.m_properties.replace("password", password);
		this.m_properties.put("password", password);
	}
	
	/**
	 * Calls the JDBC driver manager to open a new connection to the library database
	 * with the set username and password in the connection properties. Call 
	 * {@link #setUsername(String)} and {@link #setPassword(String)} before making the 
	 * initial connection.
	 * 
	 * @return
	 * 	A new connection to the library database.
	 * 
	 * @throws SQLException
	 * 	An sql exception is thrown is the server does not respond or access denied.
	 */
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(this.m_url, this.m_properties);
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
		String host;
		String port;
		
		// read in the port number ond host address from the config file
		try {
			ConfigReader reader = new ConfigReader("config.ini");
			Map<String, String> database_section = reader.getValues("database");
			
			// create empty map if there is no database header defined in config
			if (database_section == null)
				database_section = new HashMap<>();
			
			// get the hostname and port number
			host = database_section.getOrDefault("host", "localhost");
			port = database_section.getOrDefault("port", "3306");
			this.m_url = "jdbc:mysql://" + host + ":" + port + "/Library";
		} catch (IOException | ConfigFormatException e) {
			e.printStackTrace();
		}
		
		//DriverManager.setLoginTimeout(5);
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
}
