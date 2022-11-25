package src.application.server.database.query;

import src.application.server.database.records.BookSearchResult;
import src.application.server.database.records.IResultFactory;

public class BookSearchHandler extends AbstractSearchHandler<BookSearchResult> {

	@Override
	protected void setQuery(String filter) {
		this.m_query =  "SELECT b.isbn, b.title, a.name, IF(bl.isbn IS NULL, TRUE, FALSE) as isAvailable "
				+ "FROM ((Library.Book AS b JOIN Library.Book_Authors AS ba ON b.isbn = ba.isbn) "
				+ "JOIN Library.Authors as a ON ba.author_id = a.author_id) "
				+ "LEFT JOIN ("
					+ "SELECT * FROM Library.Book_Loans as temp WHERE temp.date_in IS NULL"
				+ ") AS bl ON b.isbn = bl.isbn "
				+ getWhereClause(filter);
	}

	private String getWhereClause(String filter) {
		switch(filter) {
		case("ISBN"):
			return "WHERE b.isbn LIKE ?;";
		case("Title"):
			return "WHERE b.title LIKE ?;";
		case("Author"):
			return "WHERE a.name LIKE ?;";
		default:
			return "WHERE b.isbn LIKE ? OR b.title LIKE ? OR a.name LIKE ?;";
		}
	}

	@Override
	protected IResultFactory<BookSearchResult> getResultFactory() {
		return new BookSearchResult.Builder();
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
