package ch07.springbook.sql.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {

	private Map<String, String> sqlMap = new ConcurrentHashMap<>();

	@Override
	public void registerSql(String key, String sql) {
		sqlMap.put(key, sql);
	}

	@Override
	public String findSql(String key) throws SqlNotFoundException {
		String sql = sqlMap.get(key);

		if (sql == null) {
			throw new SqlNotFoundException("Can not find appropriate sql statement, key: " + key);
		}

		return sql;
	}

	@Override
	public void updateSql(String key, String sql) throws SqlUpdateFailureException {

		if (sqlMap.get(key) == null) {
			throw new SqlUpdateFailureException("Sql not found, key: " + key);
		}

		sqlMap.put(key, sql);
	}

	@Override
	public void updateSql(Map<String, String> sqlMap) throws SqlUpdateFailureException {
		for (Map.Entry<String, String> entry : sqlMap.entrySet()) {
			updateSql(entry.getKey(), entry.getValue());
		}
	}
}
