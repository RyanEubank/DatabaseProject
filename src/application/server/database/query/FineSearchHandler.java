package src.application.server.database.query;

import src.application.server.database.records.FineSearchResult;
import src.application.server.database.records.IResultFactory;

public class FineSearchHandler extends AbstractSearchHandler<FineSearchResult> {

	@Override
	protected void setQuery(String filter) {
		this.m_query = "SELECT f.loan_id, b.card_id, b.bname, f.fine_amt, f.paid "
				+ "FROM (Library.Fines AS f JOIN Library.Book_Loans AS bl ON f.loan_id = bl.loan_id) "
				+ "JOIN Library.borrower AS b ON bl.card_id = b.card_id " 
				+ getWhereClause(filter);
	}

	private String getWhereClause(String filter) {
		switch(filter) {
		case("Loan No."):
			return "WHERE bl.loan_id LIKE ?;";
		case("Card No."):
			return "WHERE bl.card_id LIKE ?;";
		case("Name"):
			return "WHERE b.bname LIKE ?;";
		default:
			return "WHERE bl.loan_id LIKE ? OR bl.card_id LIKE ? OR b.bname LIKE ?;";
		}
	}

	@Override
	protected IResultFactory<FineSearchResult> getResultFactory() {
		return new FineSearchResult.Builder();
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
