package ch01.springbook.user;

import ch01.springbook.user.domain.User;

public interface UserService {

	void add(User user);
	void upgradeLevels();
}
