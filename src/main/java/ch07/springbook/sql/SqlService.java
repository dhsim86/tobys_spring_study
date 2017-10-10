package ch07.springbook.sql;

public interface SqlService {
	String getSql(String key) throws SqlRetrievalFailureException;
}
