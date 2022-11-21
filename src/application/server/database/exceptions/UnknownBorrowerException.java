package src.application.server.database.exceptions;

public class UnknownBorrowerException extends LibraryRuleException {
	private static final long serialVersionUID = 1L;
	
	private int m_borrowerID;
	
	public UnknownBorrowerException(int borrowerID) {
		this.m_borrowerID = borrowerID;
	}
	
	@Override
	public String getMessage() {
		return "Borrower ID: (" + m_borrowerID + ") not found!";
	}
}
