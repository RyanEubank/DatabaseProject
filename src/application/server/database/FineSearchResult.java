package src.application.server.database;

import java.sql.*;

import javafx.beans.property.*;
import src.application.server.sql.IResultFactory;

public class FineSearchResult {

	private final SimpleIntegerProperty m_borrowerID;
	private final SimpleStringProperty m_name;
	private final SimpleDoubleProperty m_amount;
	private final SimpleBooleanProperty m_isPaid;
	
	public FineSearchResult(
		int borrowerID, String name, double amount, boolean isPaid
	) {
		this.m_borrowerID = new SimpleIntegerProperty(borrowerID);
		this.m_name = new SimpleStringProperty(name);
		this.m_amount = new SimpleDoubleProperty(amount);
		this.m_isPaid = new SimpleBooleanProperty(isPaid);
	}
	
	public int getBorrowerID() {
		return this.m_borrowerID.get();
	}

	public String getName() {
		return this.m_name.get();
	}
	
	public double getAmount() {
		return this.m_amount.get();
	}
	
	public boolean getIsPaid() {
		return this.m_isPaid.get();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FineSearchResult))
			return false;
		return this.getBorrowerID() == ((FineSearchResult) o).getBorrowerID();
	}
	
	public static class Builder implements IResultFactory<FineSearchResult> {

		@Override
		public FineSearchResult createInstance(ResultSet results) throws SQLException {
			int borrowerID = results.getInt("card_id");
			String name = results.getString("bname");
			double amount = results.getDouble("total_owed");
			boolean isPaid = results.getBoolean("paid");
			
			return new FineSearchResult(borrowerID, name, amount, isPaid);
		}

		@Override
		public void aggregate(FineSearchResult duplicate, FineSearchResult record) {
			// this should not be used to group by card id, that should be done in query.
			throw new RuntimeException("Duplicate fine returned from search.");
		}
		
	}
}
