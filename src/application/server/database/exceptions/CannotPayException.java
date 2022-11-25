package src.application.server.database.exceptions;

public class CannotPayException extends LibraryRuleException {

	private static final long serialVersionUID = 1L;

	private int m_LoanID;
	
	public CannotPayException(int loanID) {
		this.m_LoanID = loanID;
	}
	
	@Override
	public String getMessage() {
		return "Loan ID: (" + m_LoanID + ") cannot be paid, not yet checked in!";
	}
	
}
