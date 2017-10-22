package ch07.springbook.sql.registry;

public interface SqlRegistry {
	void registerSql(String key, String sql);
	String findSql(String key) throws SqlNotFoundException;
}
