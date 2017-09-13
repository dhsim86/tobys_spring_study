package ch01.springbook.user;

import static ch01.springbook.user.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static ch01.springbook.user.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import ch01.springbook.user.dao.UserDao;
import ch01.springbook.user.domain.Level;
import ch01.springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {

	static class TestUserServiceException extends RuntimeException {

	}

	static class TestUserService extends UserServiceImpl {
		private String id = "test04";

		private TestUserService(String id) {
			this.id = id;
		}

		public TestUserService() {

		}

		protected void upgradeLevel(User user) {
			if (user.getId().equals(this.id)) {
				throw new TestUserServiceException();
			}

			super.upgradeLevel(user);
		}
	}

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private UserService userService;

	@Autowired
	private UserService testUserService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ApplicationContext applicationContext;

	private List<User> userList;

	@Before
	public void setUp() {

		userList = Arrays.asList(
			new User("test01", "test", "no1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0, "no1@mail.com"),
			new User("test02", "test", "no2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "no2@mail.com"),
			new User("test03", "test", "no3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1, "no3@mail.com"),
			new User("test04", "test", "no3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "no4@mail.com"),
			new User("test05", "test", "no3", Level.GOLD, 100, Integer.MAX_VALUE, "no5@mail.com")
		);
	}

	@Test
	public void bean() {
		assertThat(userService, is(notNullValue()));
	}

	@Test
	public void upgradeLevels() throws Exception {

		userDao.deleteAll();

		for (User user : userList) {
			userDao.add(user);
		}

		userService.upgradeLevels();

		checkLevelUpgraded(userList.get(0), false);
		checkLevelUpgraded(userList.get(1), true);
		checkLevelUpgraded(userList.get(2), false);
		checkLevelUpgraded(userList.get(3), true);
		checkLevelUpgraded(userList.get(4), false);
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

	@Test
	public void upgradeAllOrNothing() throws Exception {
		TestUserService testUserService = new TestUserService(userList.get(3).getId());
		testUserService.setUserDao(userDao);

		UserServiceTx txUserService = new UserServiceTx();
		txUserService.setTransactionManager(transactionManager);
		txUserService.setUserService(testUserService);

		userDao.deleteAll();
		for (User user : userList) {
			userDao.add(user);
		}

		try {
			txUserService.upgradeLevels();
			fail("TestUserServiceException expected.");
		} catch (TestUserServiceException e) {

		}

		checkLevelUpgraded(userList.get(1), false);
	}

	@Test
	public void upgradeAllOrNothingUsingDynamicProxy() throws Exception {

		TestUserService testUserService = new TestUserService(userList.get(3).getId());
		testUserService.setUserDao(userDao);

		TransactionHandler txHandler = new TransactionHandler();
		txHandler.setTarget(testUserService);
		txHandler.setTransactionManager(transactionManager);
		txHandler.setPattern("upgradeLevels");

		UserService txUserService = (UserService)Proxy.newProxyInstance(
			getClass().getClassLoader(), new Class[] { UserService.class }, txHandler);

		userDao.deleteAll();
		for (User user : userList) {
			userDao.add(user);
		}

		try {
			txUserService.upgradeLevels();
			fail("TestUserServiceException expected.");
		} catch (TestUserServiceException e) {

		}

		checkLevelUpgraded(userList.get(1), false);
	}

	@Test
	@DirtiesContext
	public void upgradeAllOrNothingUsingDynamicProxyBean() throws Exception {

		userDao.deleteAll();
		for (User user : userList) {
			userDao.add(user);
		}

		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected.");
		} catch (TestUserServiceException e) {

		}

		checkLevelUpgraded(userList.get(1), false);
	}

	@Test
	public void advisorAutoProxyCreatorTest() {
		assertThat(java.lang.reflect.Proxy.isProxyClass(userService.getClass()), is(true));
		assertThat(java.lang.reflect.Proxy.isProxyClass(testUserService.getClass()), is(true));
	}

	private void checkLevelUpgraded(User user, boolean upgraded) {

		User userUpdate = userDao.get(user.getId());

		if (upgraded) {
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		} else {
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}
}
