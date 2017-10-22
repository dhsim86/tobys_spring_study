package ch07.springbook.sql;

import javax.annotation.PostConstruct;

import ch07.springbook.sql.reader.SqlReader;
import ch07.springbook.sql.registry.SqlNotFoundException;
import ch07.springbook.sql.registry.SqlRegistry;

public class BaseSqlService implements SqlService {
	protected SqlReader sqlReader;
	protected SqlRegistry sqlRegistry;

	public void setSqlReader(SqlReader sqlReader) {
		this.sqlReader = sqlReader;
	}

	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}

	@PostConstruct
	public void loadSql() {
		sqlReader.read(sqlRegistry);
	}

	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		try {
			return sqlRegistry.findSql(key);
		} catch (SqlNotFoundException e) {
			throw new SqlRetrievalFailureException(e);
		}
	}
}
