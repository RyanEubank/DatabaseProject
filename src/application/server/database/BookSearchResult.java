package src.application.server.database;

import javafx.beans.property.*;

public class BookSearchResult {

	private final SimpleStringProperty m_isbn;
	private final SimpleStringProperty m_title;
	private final SimpleStringProperty m_authors;
	private final SimpleBooleanProperty m_isAvailable;
	
	/**
	 * Constructs a new book search result to populate the search table
	 * with.
	 * 
	 * @param isbn - the isbn number of the book.
	 * @param title - the title of the book.
	 * @param isAvailable - whether the book is avaiable to check out or alredy loaned.
	 * @param authors - a string array of the authors that wrote the book.
	 */
	public BookSearchResult(String isbn, String title, boolean isAvailable, String...authors) {
		this.m_isbn = new SimpleStringProperty(isbn);
		this.m_title = new SimpleStringProperty(title);
		this.m_isAvailable = new SimpleBooleanProperty(isAvailable);
		
		// read each author and construct a comma seperated list to populate the authors column
		String authorsList = "";
		for (int i = 0; i < authors.length; i++) {
			authorsList += authors[i];
			if (i < authors.length - 1)
				authorsList += ", ";
		}
		
		this.m_authors = new SimpleStringProperty(authorsList);
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
		return this.m_authors.get();
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
	
	/**
	 * Returns the availability property for the book search result.
	 * @return
	 *  The simple boolean property bounds to the book's availability status.
	 */
	public SimpleBooleanProperty AvailabilityProperty() {
		return this.m_isAvailable;
	}
}
