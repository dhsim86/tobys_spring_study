package ch07.springbook.sql.registry;

public class SqlNotFoundException extends RuntimeException {

	public SqlNotFoundException(String message) {
		super(message);
	}

	public SqlNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
