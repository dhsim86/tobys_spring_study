package ch01.springbook.user.dao;

import java.sql.SQLException;

import ch01.springbook.user.domain.User;

public class UserDaoTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		UserDao dao = new DaoFactory().userDao();

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
