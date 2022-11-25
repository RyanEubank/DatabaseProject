package src.application.server.database.exceptions;

public class DuplicateBorrowerException extends LibraryRuleException {
	private static final long serialVersionUID = 1L;
	
	private String m_ssn;
	private String m_name;
	private String m_addr;
	
	public DuplicateBorrowerException(String ssn) {
		this.m_ssn = ssn;
		this.m_name = "";
		this.m_addr = "";
	}
	
	public DuplicateBorrowerException(String name, String address) {
		this.m_ssn = "";
		this.m_name = name;
		this.m_addr = address;
	}
	
	@Override
	public String getMessage() {
		if (this.m_ssn.isEmpty())
			return "Borrower NAME, ADDRESS: (" + this.m_name + 
				   ", " + this.m_addr.substring(0, 6) + "...) already exists";
		else 
			return "Borrower SSN: (" + m_ssn + ") already exists!";
	}
}
