package src.application.server.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javafx.beans.property.*;
import src.application.server.sql.IResultFactory;

public class BookSearchResult {

	private final SimpleStringProperty m_isbn;
	private final SimpleStringProperty m_title;
	private final SimpleBooleanProperty m_isAvailable;
	private final Set<String> m_authors;
	private SimpleStringProperty m_authorList;
	
	/**
	 * Constructs a new book search result to populate the search table
	 * with.
	 * 
	 * @param isbn - the isbn number of the book.
	 * @param title - the title of the book.
	 * @param isAvailable - whether the book is avaiable to check out or alredy loaned.
	 * @param authors - a string array of the authors that wrote the book.
	 */
	public BookSearchResult(
		String isbn, String title, boolean isAvailable, String...authors
	) {
		this.m_isbn = new SimpleStringProperty(isbn);
		this.m_title = new SimpleStringProperty(title);
		this.m_isAvailable = new SimpleBooleanProperty(isAvailable);
		
		// get each unique author passed in and store them as
		// a comma seperated list
		this.m_authors = new HashSet<>();
		for (String author : authors) 
			this.m_authors.add(author);
		
		fillAuthorsColumn();
	}

	/**
	 * 
	 */
	private void fillAuthorsColumn() {
		String authorsList = String.join(", ", this.m_authors);
		this.m_authorList = new SimpleStringProperty(authorsList);
	}
	
	/**
	 * Getter for the book's ISBN. 
	 * (Must remain unchanged for the table's cell factories to read data dynamically).
	 * 
	 * @return
	 * Returns a string containing the book's ISBN no. 
	 */
	public String getIsbn() {
		return this.m_isbn.get();
	}
	
	/**
	 * Getter for the book's title.
	 * (Must remain unchanged for the table's cell factories to read data dynamically).
	 * 
	 * @return
	 * Returns a string containing the book's title. 
	 */
	public String getTitle() {
		return this.m_title.get();
	}
	
	/**
	 * Getter for the book's list of authors.
	 * (Must remain unchanged for the table's cell factories to read data dynamically).
	 * 
	 * @return
	 * Returns a comma seperated list of the authors as a string.
	 */
	public String getAuthors() { 
		return this.m_authorList.get();
	}
	
	/**
	 * Returns whether another BookSearchResult has the same isbn.
	 * 
	 * @return
	 * 	True if the other BookSearchResult has the same isbn as this one.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BookSearchResult))
			return false;
		return ((BookSearchResult) o).getIsbn().equals(this.getIsbn());
	}
	
	/**
	 * Getter for the book's check out status, true means the book is available to
	 * check out.
	 * (Must remain unchanged for the table's cell factories to read data dynamically).
	 * 
	 * @return
	 * Returns a boolean to indicate the book's availability.
	 */
	public boolean getIsAvailable() {
		return this.m_isAvailable.get();
	}
	
	public static class Builder 
		implements IResultFactory<BookSearchResult> {

		@Override
		public BookSearchResult createInstance(
			ResultSet results) throws SQLException 
		{
			String isbn = results.getString("isbn");
			String title = results.getString("title");
			boolean isAvailable = results.getBoolean("isAvailable");
			String author = results.getString("name");
			return new BookSearchResult(isbn, title, isAvailable, author);
		}

		@Override
		public void aggregate(BookSearchResult duplicate, BookSearchResult record) {
			duplicate.m_authors.addAll(record.m_authors);
			duplicate.fillAuthorsColumn();
		}
		
	}
}
