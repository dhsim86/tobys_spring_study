package ch01.springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ch01.springbook.user.domain.Level;
import ch01.springbook.user.domain.User;
import ch07.springbook.sql.SqlService;

public class UserDaoJdbc implements UserDao {

	@Autowired
	private SqlService sqlService;

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

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}

	public void add(User user) {
		jdbcTemplate.update(sqlService.getSql("userAdd"),
							 user.getId(), user.getName(), user.getPassword(),
							 user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
	}

	public User get(String id) {
		return jdbcTemplate.queryForObject(sqlService.getSql("userGet"), new Object[] {id}, this.userMapper);
	}

	public List<User> getAll() {
		return jdbcTemplate.query(sqlService.getSql("userGetAll"), this.userMapper);
	}

	public void deleteAll() {
		jdbcTemplate.update(sqlService.getSql("userDeleteAll"));
	}

	public int getCount() {
/*
		return this.jdbcTemplate.query(
			new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					return connection.prepareStatement(sqlService.getSql("userGetCount"));
				}
			},
			new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					resultSet.next();
					return resultSet.getInt(1);
				}
			});*/

		return jdbcTemplate.queryForObject(sqlService.getSql("userGetCount"), Integer.class);
	}

	public void update(User user) {
		jdbcTemplate.update(sqlService.getSql("userUpdate"),
			user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(),
			user.getRecommend(), user.getEmail(), user.getId());
	}
}
