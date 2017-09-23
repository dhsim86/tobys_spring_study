package ch01.springbook.user;

import java.util.List;

import ch01.springbook.user.domain.User;

public interface UserService {

	void add(User user);
	User get(String id);
	List<User> getAll();
	void deleteAll();
	void update(User user);

	void upgradeLevels();
}
