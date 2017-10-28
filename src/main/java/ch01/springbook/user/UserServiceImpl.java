package ch01.springbook.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch01.springbook.user.dao.UserDao;
import ch01.springbook.user.domain.Level;
import ch01.springbook.user.domain.User;

@Service("userService")
public class UserServiceImpl implements UserService {

	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;

	@Autowired
	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void add(User user) {
		if (user.getLevel() == null) {
			user.setLevel(Level.BASIC);
		}

		userDao.add(user);
	}

	public void deleteAll() {
		userDao.deleteAll();
	}

	public User get(String id) {
		return userDao.get(id);
	}

	public List<User> getAll() {
		return userDao.getAll();
	}

	public void update(User user) {
		userDao.update(user);
	}

	public void upgradeLevels() {

		List<User> userList = userDao.getAll();

		for (User user : userList) {

			if (canUpgradeLevel(user) == true) {
				upgradeLevel(user);
			}
		}
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
