package src.application.server.database;

import java.util.List;

import src.application.server.sql.QueryHandler;

public class FineSearchHandler {

	public static List<FineSearchResult> lookup(String key, String filter) {
		String fineSearchQuery = "SELECT b.card_id, b.bname, SUM(f.fine_amt) as total_owed, f.paid " +
								 "FROM (Library.Fines AS f JOIN Library.Book_Loans AS bl ON f.loan_id = bl.loan_id) " +
								 "JOIN Library.borrower AS b ON bl.card_id = b.card_id " +
								 "GROUP BY b.card_id, b.bname HAVING f.paid = FALSE";
		
		// search terms used as subqueries should be empty list if not present, NOT a list
		// containing a single element: empty string
		String searchTerms[] = (key == null || key.isBlank())
				? new String[0] : new String[] {key};
				
		List<FineSearchResult> results = QueryHandler.queryDB(
			fineSearchQuery, new FineSearchResult.Builder(), searchTerms
		);
		
		return results;
	}

}
