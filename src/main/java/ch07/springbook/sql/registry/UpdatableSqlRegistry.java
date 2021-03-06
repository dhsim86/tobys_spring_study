package ch07.springbook.sql.registry;

import java.util.Map;

public interface UpdatableSqlRegistry extends SqlRegistry {
	void updateSql(String key, String sql) throws SqlUpdateFailureException;
	void updateSql(Map<String, String> sqlMap) throws SqlUpdateFailureException;
}
