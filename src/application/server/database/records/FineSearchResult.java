package src.application.server.database.records;

import java.sql.*;

import javafx.beans.property.*;

public class FineSearchResult {

	private final SimpleIntegerProperty m_loanID;
	private final SimpleIntegerProperty m_borrowerID;
	private final SimpleStringProperty m_name;
	private final SimpleDoubleProperty m_amount;
	private final SimpleBooleanProperty m_isPaid;
	
	public FineSearchResult(
		int loanID, int borrowerID, String name, double amount, boolean isPaid
	) {
		this.m_loanID = new SimpleIntegerProperty(loanID);
		this.m_borrowerID = new SimpleIntegerProperty(borrowerID);
		this.m_name = new SimpleStringProperty(name);
		this.m_amount = new SimpleDoubleProperty(amount);
		this.m_isPaid = new SimpleBooleanProperty(isPaid);
	}
	
	public int getLoanID() {
		return this.m_loanID.get();
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
	

	public void setIsPaid(boolean isPaid) {
		this.m_isPaid.set(isPaid);
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FineSearchResult))
			return false;
		return this.getLoanID() == ((FineSearchResult) o).getLoanID();
	}
	
	public static class Builder implements IResultFactory<FineSearchResult> {

		@Override
		public FineSearchResult createInstance(ResultSet results) throws SQLException {
			int loanID = results.getInt("loan_id");
			int borrowerID = results.getInt("card_id");
			String name = results.getString("bname");
			double amount = results.getDouble("fine_amt");
			boolean isPaid = results.getBoolean("paid");
			
			return new FineSearchResult(loanID, borrowerID, name, amount, isPaid);
		}

		@Override
		public void aggregate(FineSearchResult duplicate, FineSearchResult record) {
			// no duplicates shoud be returned from the query.
			throw new RuntimeException("Duplicate fine returned from search.");
		}
		
	}
}
