package ch01.springbook.user.dao;

import ch01.springbook.user.domain.User;

import java.sql.SQLException;

public class UserDaoTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		ConnectionMaker connectionMaker = new SimpleConnectionMaker();

		UserDao dao = new UserDao(connectionMaker);

		User user = new User();
		user.setId("whiteship");
		user.setName("심동호");
		user.setPassword("1234");

		dao.add(user);

		System.out.println(user.getId() + " registered success.");

		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());

		System.out.println(user2.getId() + " retrieve success.");
	}
}
