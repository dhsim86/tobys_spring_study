package ch01.springbook.user;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch01.springbook.user.dao.UserDao;
import ch01.springbook.user.domain.Level;
import ch01.springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {

	@Autowired
	UserService userService;

	@Autowired
	UserDao userDao;

	List<User> userList;

	@Before
	public void setUp() {

		userList = Arrays.asList(
			new User("test01", "test", "no1", Level.BASIC, 49, 0),
			new User("test02", "test", "no2", Level.BASIC, 50, 0),
			new User("test03", "test", "no3", Level.SILVER, 60, 29),
			new User("test04", "test", "no3", Level.SILVER, 60, 30),
			new User("test05", "test", "no3", Level.GOLD, 100, 100)
		);
	}

	@Test
	public void bean() {
		assertThat(userService, is(notNullValue()));
	}

	@Test
	public void upgradeLevels() {

		userDao.deleteAll();

		for (User user : userList) {
			userDao.add(user);
		}

		userService.upgradeLevels();

		checkLevel(userList.get(0), Level.BASIC);
		checkLevel(userList.get(1), Level.SILVER);
		checkLevel(userList.get(2), Level.SILVER);
		checkLevel(userList.get(3), Level.GOLD);
		checkLevel(userList.get(4), Level.GOLD);
	}

	@Test
	public void add() {

		userDao.deleteAll();

		User userWithLevel = userList.get(4);
		User userWithoutLevel = userList.get(0);
		userWithLevel.setLevel(null);

		userService.add(userWithLevel);
		userService.add(userWithoutLevel);

		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
	}

	private void checkLevel(User user, Level expectedLevel) {
		User userUpdate = userDao.get(user.getId());
		assertThat(userUpdate.getLevel(), is(expectedLevel));
	}
}
