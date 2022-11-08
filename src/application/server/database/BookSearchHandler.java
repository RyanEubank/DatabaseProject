package src.application.server.database;

import java.util.*;

public class BookSearchHandler {

	public static List<BookSearchResult> lookup(String key, String filter) {
		List<BookSearchResult> results = new ArrayList<BookSearchResult>();
		results.add(new BookSearchResult("1-23-4567-89-x", "Example Title", true, "Ryan Eubank"));
		results.add(new BookSearchResult("4-03-4117-09-2", "Books for Dummies", false, "Jeff Gomez"));
		results.add(new BookSearchResult("0-00-0000-00-0", "Fairy Tales", true, "Arthur Peter"));
		return results;
	}

}
