package ch01.springbook.user;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch01.springbook.user.dao.UserDao;
import ch01.springbook.user.domain.Level;
import ch01.springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private UserDao userDao;

	@Autowired
	private DataSource dataSource;

	private User user1;
	private User user2;
	private User user3;

	@Before
	public void setUp() {
		user1 = new User("test01", "test", "no1", Level.BASIC, 1, 0, "test@mail.com");
		user2 = new User("test02", "test", "no2", Level.SILVER, 55, 10, "test2@mail.com");
		user3 = new User("test03", "test", "no3", Level.GOLD, 100, 40, "test3@mail.com");
	}

	private void checkSameUser(User user1, User user2) {
		assertThat(user2.getName(), is(user1.getName()));
		assertThat(user2.getPassword(), is(user1.getPassword()));
		assertThat(user2.getLevel(), is(user1.getLevel()));
		assertThat(user2.getLogin(), is(user1.getLogin()));
		assertThat(user2.getRecommend(), is(user1.getRecommend()));
		assertThat(user2.getEmail(), is(user1.getEmail()));
	}

	@Test
	public void addAndGet() {
		// Delete Test
		userDao.deleteAll();
		assertThat(userDao.getCount(), is(0));

		userDao.add(user1);
		userDao.add(user2);
		assertThat(userDao.getCount(), is(2));

		User userget1 = userDao.get(user1.getId());
		checkSameUser(userget1, user1);

		User userget2 = userDao.get(user2.getId());
		checkSameUser(userget2, user2);
	}

	@Test
	public void getAll() {

		userDao.deleteAll();

		userDao.add(user1);
		userDao.add(user2);
		userDao.add(user3);

		List<User> userList = userDao.getAll();
		assertThat(userList.size(), is(3));
	}

	@Test
	public void count() {
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
	public void getUserFailure() {
		userDao.deleteAll();
		assertThat(userDao.getCount(), is(0));

		userDao.get("unknown");
	}

	@Test(expected = DuplicateKeyException.class)
	public void duplicateKey() {
		userDao.deleteAll();

		userDao.add(user1);
		userDao.add(user1);
	}

	@Test
	public void update() {
		userDao.deleteAll();

		userDao.add(user1);
		userDao.add(user2);

		user1.setName("test011");
		user1.setPassword("testmod");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		user1.setEmail("test011@mail.com");

		userDao.update(user1);

		User userget1 = userDao.get(user1.getId());
		checkSameUser(user1, userget1);

		User userget2 = userDao.get(user2.getId());
		checkSameUser(user2, userget2);
	}
}
