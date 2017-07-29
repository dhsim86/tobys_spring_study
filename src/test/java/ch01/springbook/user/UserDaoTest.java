package ch01.springbook.user;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import ch01.springbook.user.dao.UserDao;
import ch01.springbook.user.domain.User;

public class UserDaoTest {

	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException {
		ApplicationContext applicationContext =
			new GenericXmlApplicationContext("applicationContext.xml");
		UserDao dao = applicationContext.getBean("userDao", UserDao.class);

		User user1 = new User("whiteship", "test", "no1");
		User user2 = new User("blackship", "test", "no2");

		// Delete Test
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));

		User userget1 = dao.get(user1.getId());
		assertThat(userget1.getName(), is(user1.getName()));
		assertThat(userget1.getPassword(), is(user1.getPassword()));

		User userget2 = dao.get(user2.getId());
		assertThat(userget2.getName(), is(user2.getName()));
		assertThat(userget2.getPassword(), is(user2.getPassword()));
	}

	@Test
	public void count() throws SQLException, ClassNotFoundException {
		ApplicationContext applicationContext =
			new GenericXmlApplicationContext("applicationContext.xml");
		UserDao dao = applicationContext.getBean("userDao", UserDao.class);

		User user1 = new User("test01", "test", "no1");
		User user2 = new User("test02", "test", "no2");
		User user3 = new User("test03", "test", "no3");

		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		dao.add(user1);
		assertThat(dao.getCount(), is(1));

		dao.add(user2);
		assertThat(dao.getCount(), is(2));

		dao.add(user3);
		assertThat(dao.getCount(), is(3));
	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException, ClassNotFoundException {
		ApplicationContext applicationContext =
			new GenericXmlApplicationContext("applicationContext.xml");
		UserDao dao = applicationContext.getBean("userDao", UserDao.class);

		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		dao.get("unknown");
	}
}
