package ch07.springbook.sql;

public class SqlRetrievalFailureException extends RuntimeException {

	public SqlRetrievalFailureException(String message) {
		super(message);
	}

	public SqlRetrievalFailureException(String message, Throwable cause) {
		super(message, cause);
	}

	public SqlRetrievalFailureException(Throwable cause) {
		super(cause);
	}
}
