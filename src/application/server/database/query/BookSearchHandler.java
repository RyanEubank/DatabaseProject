package src.application.server.database.query;

import src.application.server.database.records.BookSearchResult;
import src.application.server.database.records.IResultFactory;

public class BookSearchHandler extends AbstractSearchHandler<BookSearchResult> {

	@Override
	protected void setQuery(String filter) {
		this.m_query =  "SELECT b.isbn, b.title, a.name, IF(bl.isbn IS NULL, TRUE, FALSE) as isAvailable "
				+ "FROM ((Library.Book AS b JOIN Library.Book_Authors AS ba ON b.isbn = ba.isbn) "
				+ "JOIN Library.Authors as a ON ba.author_id = a.author_id) "
				+ "LEFT JOIN (SELECT * FROM Library.Book_Loans as temp WHERE temp.date_in IS NULL) AS bl ON b.isbn = bl.isbn ";
	}

	@Override
	protected IResultFactory<BookSearchResult> getResultFactory() {
		return new BookSearchResult.Builder();
	}
}
