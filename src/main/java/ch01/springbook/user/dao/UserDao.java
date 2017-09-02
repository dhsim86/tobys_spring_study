package ch01.springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import ch01.springbook.user.domain.User;

public class UserDao {

	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;

	public UserDao() {

	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.dataSource = dataSource;
	}

	public void add(User user) throws ClassNotFoundException, SQLException {
		this.jdbcTemplate.update("INSERT INTO users(id, name, password) VALUES(?, ?, ?)",
			user.getId(), user.getName(), user.getPassword());
	}

	public User get(String id) throws ClassNotFoundException, SQLException {

		return this.jdbcTemplate.queryForObject(
			"SELECT * FROM users WHERE id = ?",
			new Object[] {id},
			new RowMapper<User>() {
				@Override
				public User mapRow(ResultSet resultSet, int i) throws SQLException {

					User user = new User();
					user.setId(resultSet.getString("id"));
					user.setName(resultSet.getString("name"));
					user.setPassword(resultSet.getString("password"));

					return user;
				}
			}
		);
	}

	public void deleteAll() throws SQLException {
		this.jdbcTemplate.update("delete from users");
	}

	public int getCount() throws SQLException {

		return this.jdbcTemplate.query(
			new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					return connection.prepareStatement("select count(*) from users");
				}
			},
			new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					resultSet.next();
					return resultSet.getInt(1);
				}
			}
		);

		// this.jdbcTemplate.queryForInt("select count(*) from users");
	}
}
