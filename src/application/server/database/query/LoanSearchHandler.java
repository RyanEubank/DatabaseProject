package src.application.server.database.query;

import src.application.server.database.records.IResultFactory;
import src.application.server.database.records.LoanSearchResult;

public class LoanSearchHandler extends AbstractSearchHandler<LoanSearchResult>{

	@Override
	protected void setQuery(String filter) {
		this.m_query = "SELECT * FROM Library.Book_Loans as bl " + 
				"JOIN Library.Borrower as b ON bl.card_id = b.card_id ";
	}

	@Override
	protected IResultFactory<LoanSearchResult> getResultFactory() {
		return new LoanSearchResult.Builder();
	}

}
