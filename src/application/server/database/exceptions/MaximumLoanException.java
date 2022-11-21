package src.application.server.database.exceptions;

public class MaximumLoanException extends LibraryRuleException {

	private static final long serialVersionUID = 1L;
	
	private int m_borrowerID;
	
	public MaximumLoanException(int borrowerID) {
		this.m_borrowerID = borrowerID;
	}
	
	@Override
	public String getMessage() {
		return "Borrower ID: (" + m_borrowerID + 
			") has exceeded maximum allowed checkouts";
	}
}
