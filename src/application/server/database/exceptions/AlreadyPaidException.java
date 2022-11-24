package src.application.server.database.exceptions;

public class AlreadyPaidException extends LibraryRuleException {

	private static final long serialVersionUID = 1L;
	
	private int m_LoanID;
	
	public AlreadyPaidException(int loanID) {
		this.m_LoanID = loanID;
	}
	
	@Override
	public String getMessage() {
		return "Loan ID: (" + m_LoanID + ") already paid off!";
	}
}
