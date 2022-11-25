package src.application.server.database.query;

import src.application.server.database.records.IResultFactory;
import src.application.server.database.records.LoanSearchResult;

public class LoanSearchHandler extends AbstractSearchHandler<LoanSearchResult>{

	@Override
	protected void setQuery(String filter) {
		this.m_query = "SELECT * FROM Library.Book_Loans as bl " + 
				"JOIN Library.Borrower as b ON bl.card_id = b.card_id "
				+ getWhereClause(filter);
	}

	private String getWhereClause(String filter) {
		switch(filter) {
		case("ISBN"):
			return "WHERE bl.isbn LIKE ?;";
		case("Card No."):
			return "WHERE bl.card_id LIKE ?;";
		case("Name"):
			return "WHERE b.bname LIKE ?;";
		default:
			return "WHERE bl.isbn LIKE ? OR bl.card_id LIKE ? OR b.bname LIKE ?;";
		}
	}

	@Override
	protected IResultFactory<LoanSearchResult> getResultFactory() {
		return new LoanSearchResult.Builder();
	}

	@Override
	protected int getNumSubqueries(String filter) {
		switch(filter) {
		case("Any"):
			return 3;
		default:
			return 1;
		}
	}

}
