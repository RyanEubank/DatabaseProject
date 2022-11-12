package src.application.server.database;

import java.util.*;

import src.application.server.sql.QueryHandler;

public class LoanSearchHandler {

	public static List<LoanSearchResult> lookup(String key, String filter) {
		String loanSearchQuery = 
				"SELECT * FROM Library.Book_Loans as bl "
				+ "JOIN Library.Borrower as b ON bl.card_id = b.card_id ";
		
		// search terms used as subqueries should be empty list if not present, NOT a list
		// containing a single element: empty string
		String searchTerms[] = (key == null || key.isBlank())
				? new String[0] : new String[] {key};
				
		List<LoanSearchResult> results = QueryHandler.queryDB(
				loanSearchQuery, new LoanSearchResult.Builder(), searchTerms
		);
		
		return results;
	}

}
