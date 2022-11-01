package src.application.server;

import java.io.FileNotFoundException;
import java.io.IOException;

import src.application.server.ConfigReader.ConfigFormatException;

public class Configuration {

	private static ConfigReader CONFIG_READER;
	
	/**
	 * Constructs and returns a config reader to open and parse the 
	 * config file.
	 * 
	 * @return
	 * 	Returns a new config reader if the file was successfully parsed,
	 *  null otherwise.
	 *  
	 * @throws FileNotFoundException
	 *  Throws a FileNotFound Exception if the config file is not present.
	 * @throws IOException
	 *  Throws an IOExcpetion if there was an error reading the file.
	 * @throws ConfigFormatException
	 *  Throws a ConfigFormatException if the config file contains invalid
	 *   syntax.
	 */
	public static ConfigReader get() throws 
		FileNotFoundException, IOException, ConfigFormatException 
	{
		if (CONFIG_READER == null)
			CONFIG_READER = new ConfigReader("config.ini");
		return CONFIG_READER;
	}
}
