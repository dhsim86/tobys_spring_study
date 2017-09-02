package ch01.springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import ch01.springbook.user.domain.User;
import ch03.springbook.user.dao.DeleteAllStatement;
import ch03.springbook.user.dao.StatementStrategy;

public class UserDao {

	private ConnectionMaker connectionMaker;
	private DataSource dataSource;

	public UserDao() {

	}

	public UserDao(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setConnectionMaker(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
	}

	public void add(User user) throws ClassNotFoundException, SQLException {
		Connection c = dataSource.getConnection();

		PreparedStatement ps = c.prepareStatement(
			"INSERT INTO users(id, name, password) VALUES(?, ?, ?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());

		ps.executeUpdate();

		ps.close();
		c.close();
	}

	public User get(String id) throws ClassNotFoundException, SQLException {
		Connection c = dataSource.getConnection();

		PreparedStatement ps = c.prepareStatement(
			"SELECT * FROM users WHERE id = ?");
		ps.setString(1, id);

		ResultSet rs = ps.executeQuery();

		User user = null;

		if (rs.next()) {
			user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
		}

		rs.close();
		ps.close();
		c.close();

		if (user == null) {
			throw new EmptyResultDataAccessException(1);
		}

		return user;
	}

	public void deleteAll() throws SQLException {

		StatementStrategy st = new DeleteAllStatement();
		jdbcContextWithStatementStrategy(st);
	}

	public int getCount() throws SQLException {
		Connection c = dataSource.getConnection();

		PreparedStatement ps = c.prepareStatement("select count(*) from users");

		ResultSet rs = ps.executeQuery();
		rs.next();

		int count = rs.getInt(1);

		rs.close();
		ps.close();
		c.close();

		return count;
	}

	private PreparedStatement makeStatement(Connection c) throws SQLException {
		PreparedStatement ps;
		ps = c.prepareStatement("delete from users");
		return ps;
	}

	public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {

		Connection c = null;
		PreparedStatement ps = null;

		try {

			c = dataSource.getConnection();
			ps = stmt.makePreparedStatement(c);
			ps.executeUpdate();

		} catch (SQLException e) {
			throw e;

		} finally {
			if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
			if (c != null) { try { c.close(); } catch (SQLException e) {} }
		}
	}
}
