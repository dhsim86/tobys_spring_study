package ch01.springbook.user.dao;

import ch01.springbook.user.domain.User;

import java.sql.*;

public class UserDao {

	public void add(User user) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection(
			"jdbc:mysql://localhost/tobystudy?useUnicode=true&characterEncoding=UTF-8&useSSL=true&verifyServerCertificate=false",
			"study",
			"study");

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
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection(
			"jdbc:mysql://localhost/tobystudy?useUnicode=true&characterEncoding=UTF-8&useSSL=true&verifyServerCertificate=false",
			"study",
			"study");

		PreparedStatement ps = c.prepareStatement(
			"SELECT * FROM users WHERE id = ?");
		ps.setString(1, id);

		ResultSet rs = ps.executeQuery();
		rs.next();

		User user = new User();
		user.setId(rs.getString("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));

		rs.close();
		ps.close();
		c.close();

		return user;
	}
}
