package ch07.springbook.sql.registry;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {

	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void registerSql(String key, String sql) {
		jdbcTemplate.update("INSERT INTO SQLMAP(KEY_, SQL_) values(?, ?)", key, sql);
	}

	@Override
	public String findSql(String key) throws SqlNotFoundException {
		try {
			return jdbcTemplate.queryForObject("SELECT SQL_ FROM SQLMAP WHERE KEY_ = ?",
											   String.class, key);
		} catch (EmptyResultDataAccessException e) {
			throw new SqlNotFoundException("Can not find appropriate sql statement, key: " + key);
		}
	}

	@Override
	public void updateSql(String key, String sql) throws SqlUpdateFailureException {
		int count = jdbcTemplate.update("UPDATE SQLMAP SET SQL_ = ? WHERE KEY_= ?", sql, key);

		if (count == 0) {
			throw new SqlUpdateFailureException("Sql not found, key: " + key);
		}
	}

	@Override
	public void updateSql(Map<String, String> sqlMap) throws SqlUpdateFailureException {
		for (Map.Entry<String, String> entry : sqlMap.entrySet()) {
			updateSql(entry.getKey(), entry.getValue());
		}
	}
}
