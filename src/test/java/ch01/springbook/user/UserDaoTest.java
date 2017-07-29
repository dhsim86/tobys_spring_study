package ch01.springbook.user;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import ch01.springbook.user.dao.UserDao;
import ch01.springbook.user.domain.User;

public class UserDaoTest {

	private static ApplicationContext applicationContext;

	private UserDao userDao;

	private User user1;
	private User user2;
	private User user3;

	@BeforeClass
	public static void init() {
		applicationContext =
			new GenericXmlApplicationContext("applicationContext.xml");
	}

	@Before
	public void setUp() {
		this.userDao = applicationContext.getBean("userDao", UserDao.class);

		user1 = new User("test01", "test", "no1");
		user2 = new User("test02", "test", "no2");
		user3 = new User("test03", "test", "no3");
	}

	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException {
		// Delete Test
		userDao.deleteAll();
		assertThat(userDao.getCount(), is(0));

		userDao.add(user1);
		userDao.add(user2);
		assertThat(userDao.getCount(), is(2));

		User userget1 = userDao.get(user1.getId());
		assertThat(userget1.getName(), is(user1.getName()));
		assertThat(userget1.getPassword(), is(user1.getPassword()));

		User userget2 = userDao.get(user2.getId());
		assertThat(userget2.getName(), is(user2.getName()));
		assertThat(userget2.getPassword(), is(user2.getPassword()));
	}

	@Test
	public void count() throws SQLException, ClassNotFoundException {
		userDao.deleteAll();
		assertThat(userDao.getCount(), is(0));

		userDao.add(user1);
		assertThat(userDao.getCount(), is(1));

		userDao.add(user2);
		assertThat(userDao.getCount(), is(2));

		userDao.add(user3);
		assertThat(userDao.getCount(), is(3));
	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException, ClassNotFoundException {
		userDao.deleteAll();
		assertThat(userDao.getCount(), is(0));

		userDao.get("unknown");
	}
}
