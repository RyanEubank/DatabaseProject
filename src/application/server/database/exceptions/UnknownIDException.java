package src.application.server.database.exceptions;

public class UnknownIDException extends LibraryRuleException {
	private static final long serialVersionUID = 1L;
	
	private int m_id;
	
	public UnknownIDException(int id) {
		this.m_id = id;
	}
	
	@Override
	public String getMessage() {
		return "ID: (" + m_id + ") not found!";
	}
}
