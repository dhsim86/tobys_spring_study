package ch01.springbook.user;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import ch01.springbook.user.domain.User;

public class UserServiceTx implements UserService {

	private PlatformTransactionManager transactionManager;
	private UserService userService;

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void add(User user) {
		userService.add(user);
	}

	public void upgradeLevels() {

		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

		try {
			userService.upgradeLevels();
			transactionManager.commit(status);

		} catch (RuntimeException e) {
			transactionManager.rollback(status);
			throw e;
		}
	}
}
