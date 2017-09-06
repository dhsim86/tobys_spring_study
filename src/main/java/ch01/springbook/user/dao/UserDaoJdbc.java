package ch01.springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import ch01.springbook.user.domain.Level;
import ch01.springbook.user.domain.User;

public class UserDaoJdbc implements UserDao {

	private JdbcTemplate jdbcTemplate;
	private RowMapper<User> userMapper = new RowMapper<User>() {
		@Override
		public User mapRow(ResultSet resultSet, int i) throws SQLException {

			User user = new User();
			user.setId(resultSet.getString("id"));
			user.setName(resultSet.getString("name"));
			user.setPassword(resultSet.getString("password"));
			user.setLevel(Level.valueOf(resultSet.getInt("level")));
			user.setLogin(resultSet.getInt("login"));
			user.setRecommend(resultSet.getInt("recommend"));
			user.setEmail(resultSet.getString("email"));

			return user;
		}
	};

	public UserDaoJdbc() {

	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void add(User user) {
		this.jdbcTemplate.update("INSERT INTO users(id, name, password, level, login, recommend, email) VALUES(?, ?, ?, ?, ?, ?, ?)",
								 user.getId(), user.getName(), user.getPassword(),
								 user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
	}

	public User get(String id) {

		return this.jdbcTemplate.queryForObject(
			"SELECT * FROM users WHERE id = ?", new Object[] {id}, this.userMapper
											   );
	}

	public List<User> getAll() {
		return this.jdbcTemplate.query("SELECT * FROM users", this.userMapper);
	}

	public void deleteAll() {
		this.jdbcTemplate.update("delete from users");
	}

	public int getCount() {

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

	public void update(User user) {
		this.jdbcTemplate.update(
			"update users set name = ?, password = ?, level = ?, login = ?, " +
			"recommend = ?, email = ? where id = ? ",
			user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(),
			user.getRecommend(), user.getEmail(), user.getId());
	}
}
