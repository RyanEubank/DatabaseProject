package src.application.server.config;

import java.io.*;
import java.util.*;

public class ConfigReader {

	/**
	 * Exception class to singal invalid formatting in a config
	 * file.
	 *
	 */
	public static class ConfigFormatException extends Exception {

		/**
		 * Constructs a new exception to signal a formatting error
		 * was found while parsing the config file.
		 * @param msg
		 */
		public ConfigFormatException(String msg) {
			super(msg);
		}

		//Unused where serialization is not important...
		private static final long serialVersionUID = 1L;
		
	}
	
	private Map<String, Map<String, String>> m_mapping;
	
	/**
	 * Constructs a new ConfigReader and opens the given configuration 
	 * file for reading.
	 * 
	 * @param filename - the path to the file to be read.
	 * 
	 * @throws FileNotFoundException
	 * 	Throws an exception if the file cannot be opened.
	 * @throws IOException
	 * 	Throws an IOException if an error occurs while reading the file.
	 * @throws ConfigFormatException
	 * 	Throws a ConfigFormatException if the file given is not valid .ini
	 * 	syntax.
	 */
	public ConfigReader(String filename) throws 
		FileNotFoundException, IOException, ConfigFormatException
	{
		// the mapping will contain a hashmap of header strings mapped
		// to the key/value pairs underneath them
		this.m_mapping = new HashMap<>();

		// open the file and read line by line
		try(FileReader reader = new FileReader(filename);) {
			parseFile(reader);
		}
	}
	
	/**
	 * Returns the map of key/value pairs under the specified header.
	 * @param header
	 * @return
	 */
	public Map<String, String> getValues(String header) {
		return this.m_mapping.get(header);
	}
	
	/**
	 * Reads the current file opened in the specified file reader line by line
	 * to parse headers and config settings in an .ini file
	 * 
	 * @throws ConfigFormatException 
	 *  Throws a formatting exception if the config file cannot be parsed.
	 * @throws IOException 
	 * 	Throws an IOException if there is an error while reading the file.
	 */
	private void parseFile(FileReader reader) 
		throws ConfigFormatException, IOException 
	{
		BufferedReader buffer = new BufferedReader(reader);
		int lineNum = 1;
		String currentHeader = null;
		String next;
		
		// parse until every line has been read
		while ((next = buffer.readLine()) != null) {
			
			// skip white space and comments
			if (next.isBlank() || next.matches("\\s*#+.*"))
				continue;
			
			// matches any string of the form "[...]<trailing whitespace>"
			// where the ellipses can be any non-whitespace printable characters
			else if (next.matches("\\[[\\p{Graph}]+]\\s*")) 
				currentHeader = parseHeader(next);
			
			// matches any string of the form "key = value" where the 
			// key is a valid identifier and the value can be any non-whitespace 
			// printable characters. A single space or no spaces is allowed 
			// between the '=' character
			else if (next.matches("[A-Za-z]+\\p{Alnum}* ??= ??\\p{Graph}+\\s*")) 
				parseKeyValuePair(currentHeader, next);
			
			 // line is not blank, there is no comment, header, or key/value pair
			else
				throw new ConfigFormatException(
					String.format("Invalid format at line: %d", lineNum++));
		}
		buffer.close();
	}
	
	/**
	 * Parses a header from the given line by copying everything
	 * between the square brackets [] and returning the result.
	 * 
	 * @param line - the line of text to parse
	 * 
	 * @return
	 *  A string containing the sequence of characters inside the header 
	 *  brackets.
	 */
	private String parseHeader(String line) {
		StringBuffer header = new StringBuffer();
		int index = 1;
		
		// append everything until the closing bracket
		while (line.charAt(index) != ']')
			header.append(line.charAt(index++));
		
		// extract the header string, put it into the file mapping and return
		String hdr = header.toString();
		this.m_mapping.put(hdr, new HashMap<>());
		return hdr;
	}

	/**
	 * Parses a key/value pair from the given line of text and stores
	 * its mapping under the given header.
	 * 
	 * @param currentHeader - the current header to map settings to
	 * @param line - the line of text to parse
	 * 
	 * @throws ConfigFormatException
	 *  Throws a ConfigFormatException if the key/value pair is parsed
	 *  before a valid header has been mapped.
	 */
	private void parseKeyValuePair(String currentHeader, String line) 
		throws ConfigFormatException 
	{
		String key, value;
		
		try {
			Map<String, String> map = this.m_mapping.get(currentHeader);
			
			// tokenize the string by spaces and '='
			String[] tokens = line.split(" ??= *");
			
			// extract the key and value, and map the current header to them
			key = tokens[0];
			value = tokens[1];
			map.put(key, value);
			
		// check that the header has been mapped first, otherwise error
		} catch (NullPointerException e) {
			throw new ConfigFormatException(
				"Key/Value pair found before valid header section");
		}
	}
}
