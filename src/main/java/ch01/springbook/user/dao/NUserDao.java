package ch01.springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NUserDao extends UserDao {

	@Override
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection(
			"jdbc:mysql://localhost/tobystudy?useUnicode=true&characterEncoding=UTF-8&useSSL=true&verifyServerCertificate=false",
			"study",
			"study");
		return c;
	}
}
