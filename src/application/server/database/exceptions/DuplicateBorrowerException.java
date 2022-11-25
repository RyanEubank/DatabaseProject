package src.application.server.database.exceptions;

public class DuplicateBorrowerException extends LibraryRuleException {
	private static final long serialVersionUID = 1L;
	
	private String m_ssn;
	
	public DuplicateBorrowerException(String ssn) {
		this.m_ssn = ssn;
	}
	
	@Override
	public String getMessage() {
		return "Borrower SSN: (" + m_ssn + ") already exists!";
	}
}
