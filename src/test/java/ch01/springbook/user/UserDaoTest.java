package ch01.springbook.user;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import ch01.springbook.user.dao.UserDao;
import ch01.springbook.user.domain.User;

public class UserDaoTest {

	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException {
		ApplicationContext applicationContext =
			new GenericXmlApplicationContext("applicationContext.xml");
		UserDao dao = applicationContext.getBean("userDao", UserDao.class);

		// Delete Test
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		User user = new User();
		user.setId("whiteship");
		user.setName("심동호");
		user.setPassword("1234");

		dao.add(user);
		assertThat(dao.getCount(), is(1));

		User user2 = dao.get(user.getId());

		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));
	}
}
