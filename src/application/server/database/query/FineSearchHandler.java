package src.application.server.database.query;

import src.application.server.database.records.FineSearchResult;
import src.application.server.database.records.IResultFactory;

public class FineSearchHandler extends AbstractSearchHandler<FineSearchResult> {

	@Override
	protected void setQuery(String filter) {
		this.m_query = "SELECT f.loan_id, b.card_id, b.bname, f.fine_amt, f.paid " +
				"FROM (Library.Fines AS f JOIN Library.Book_Loans AS bl ON f.loan_id = bl.loan_id) " +
				"JOIN Library.borrower AS b ON bl.card_id = b.card_id";
	}

	@Override
	protected IResultFactory<FineSearchResult> getResultFactory() {
		return new FineSearchResult.Builder();
	}

}
