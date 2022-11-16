package src.application.server.database;

import java.util.*;

import src.application.server.sql.QueryHandler;

public class BookSearchHandler {

	public static List<BookSearchResult> lookup(String key, String filter) {
		String bookSearchQuery = 
				"SELECT b.isbn, b.title, a.name, IF(bl.isbn IS NULL OR bl.date_in IS NOT NULL, TRUE, FALSE) as isAvailable "
				+ "FROM ((Library.Book AS b JOIN Library.Book_Authors AS ba ON b.isbn = ba.isbn) "
				+ "JOIN Library.Authors as a ON ba.author_id = a.author_id) "
				+ "LEFT JOIN Library.Book_loans AS bl ON b.isbn = bl.isbn";
		
		// search terms used as subqueries should be empty list if not present, NOT a list
		// containing a single element: empty string
		String searchTerms[] = (key == null || key.isBlank())
				? new String[0] : new String[] {key};
				
		List<BookSearchResult> results = QueryHandler.queryDB(
			bookSearchQuery, new BookSearchResult.Builder(), searchTerms
		);
		
		return results;
	}

}
