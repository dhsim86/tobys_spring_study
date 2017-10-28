package ch07.springbook.sql.registry;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {

	private JdbcTemplate jdbcTemplate;
	private TransactionTemplate transactionTemplate;

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
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
	public void updateSql(final Map<String, String> sqlMap) throws SqlUpdateFailureException {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
				for (Map.Entry<String, String> entry : sqlMap.entrySet()) {
					updateSql(entry.getKey(), entry.getValue());
				}
			}
		});
	}
}
