package src.application.server.database.records;

import javafx.beans.property.*;

public class FineSummaryResult {

	private final SimpleIntegerProperty m_borrowerID;
	private final SimpleStringProperty m_name;
	private final SimpleDoubleProperty m_totalOwed;
	private final SimpleDoubleProperty m_totalPaid;
	
	public FineSummaryResult(
		int borrowerID, String name, double totalOwed, double totalPaid
	) {
		this.m_borrowerID = new SimpleIntegerProperty(borrowerID);
		this.m_name = new SimpleStringProperty(name);
		this.m_totalOwed = new SimpleDoubleProperty(totalOwed);
		this.m_totalPaid = new SimpleDoubleProperty(totalPaid);
	}

	public int getBorrowerID() {
		return this.m_borrowerID.get();
	}
	
	public String getName() {
		return this.m_name.get();
	}
	
	public double getTotalOwed() {
		return this.m_totalOwed.get();
	}
	
	public double getTotalPaid() {
		return this.m_totalPaid.get();
	}
}