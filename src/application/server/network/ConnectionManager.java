package src.application.server.network;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import src.application.server.config.ConfigReader;
import src.application.server.config.Configuration;
import src.application.server.config.ConfigReader.ConfigFormatException;

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
	 * Clears the set username and password from the connection properties.
	 */
	public void clearProperties() {
		this.m_properties.clear();
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

		// read in database network settings from the config file
		Map<String, String> dbProperties = getDatabaseProperties();

		String host = dbProperties.getOrDefault("host", "localhost");
		String port = dbProperties.getOrDefault("port", "3306");
		int timeout = Integer.parseInt(dbProperties.getOrDefault("timeout", "5"));
		
		// set the connection timeout and url
		DriverManager.setLoginTimeout(timeout);
		this.m_url = "jdbc:mysql://" + host + ":" + port + "/Library";
		this.m_properties = new Properties();
	}

	/**
	 * Obtains database properties read from the config and 
	 * returns a map of the values read.
	 * 
	 * @return
	 *  Returns the map of key-value pairs containing properties
	 *  read in from the config file or an empty map if not available.
	 */
	private Map<String, String> getDatabaseProperties() {
		Map<String, String> properties;
		
		try {
			// attempt to read config file
			ConfigReader reader = Configuration.get();
			properties = reader.getValues("database");
			
			// create empty map if there is no database header defined in config
			if (properties == null)
				properties = new HashMap<>();	
			
		} catch (IOException | ConfigFormatException e) {
			// create empty properties if the config cannot be read
			properties = new HashMap<>();
			e.printStackTrace();
		}
		return properties;
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
