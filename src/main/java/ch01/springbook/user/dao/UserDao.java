package ch01.springbook.user.dao;

import java.util.List;

import ch01.springbook.user.domain.User;

public interface UserDao {

	void add(User user);
	User get(String id);
	void deleteAll();
	int getCount();
}
