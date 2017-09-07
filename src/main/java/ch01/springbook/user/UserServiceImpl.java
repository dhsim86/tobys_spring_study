package ch01.springbook.user;

import java.util.List;

import ch01.springbook.user.dao.UserDao;
import ch01.springbook.user.domain.Level;
import ch01.springbook.user.domain.User;

/**
 * Created by nhnent on 2017. 9. 7..
 */
public class UserServiceImpl implements UserService {

	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void upgradeLevels() {

		List<User> userList = userDao.getAll();

		for (User user : userList) {

			if (canUpgradeLevel(user) == true) {
				upgradeLevel(user);
			}
		}
	}

	public void add(User user) {
		if (user.getLevel() == null) {
			user.setLevel(Level.BASIC);
		}

		userDao.add(user);
	}

	private boolean canUpgradeLevel(User user) {

		Level currentLevel = user.getLevel();

		switch (currentLevel) {
			case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
			case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
			case GOLD: return false;
			default: throw new IllegalArgumentException("Unknown level: " + currentLevel);
		}
	}

	protected void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
	}

}
