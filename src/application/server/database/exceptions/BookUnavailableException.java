package src.application.server.database.exceptions;

public class BookUnavailableException extends LibraryRuleException {

	private static final long serialVersionUID = 1L;
	
	private String m_isbn;
	
	public BookUnavailableException(String isbn) {
		this.m_isbn = isbn;
	}
	
	@Override
	public String getMessage() {
		return "Book ISBN: (" + m_isbn + ") already checked out!";
	}
}
