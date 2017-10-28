package ch07.springbook.sql.registry;

import java.util.HashMap;
import java.util.Map;

public class HashMapSqlRegistry implements SqlRegistry {

	private Map<String, String> sqlMap = new HashMap<>();

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
}
