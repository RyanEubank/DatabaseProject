package src.application.server.database.exceptions;

/**
 * The SQLExceptionTypes class is a container for comon error codes generated
 * by SQLExceptions for various issues, each named with an appropriate identifier.
 * More error codes can be found at the
 * {@link <a href="https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-error-sqlstates.html">Oracle Website</a>}
 */
public class SQLExceptionTypes {
	public static final String CONNECTION_FAILED = "08S01";
	public static final String ACCESS_DENIED = "28000";
	public static final String SYNTAX_ERROR = "42S02";
	public static final String PERMISSION_DENIED = "42000";
	public static final String USER_TRIGGER = "45000";
	public static final String INTEGRITY_CONSTRAINT_VIOLATION = "23000";
	
	public static int MAX_LOAN_ERROR_CODE = 1000;
	public static int BOOK_UNAVAILABLE_CODE = 1001;
	public static int LOAN_ALREADY_CHECKED_IN_CODE = 1002;
	public static int FINE_ALREADY_PAID_CODE = 1003;
}
