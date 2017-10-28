package test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ch01.springbook.user.UserService;
import ch01.springbook.user.UserServiceTest;
import ch06.springbook.factorybean.Message;
import ch06.springbook.factorybean.MessageFactoryBean;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "ch01")
public class TestAppContext {

	@Bean
	public UserService testUserService() {
		return new UserServiceTest.TestUserService();
	}

	@Bean
	public Message message() throws Exception {
		MessageFactoryBean messageFactoryBean = new MessageFactoryBean();
		messageFactoryBean.setText("Factory Bean");

		return messageFactoryBean.getObject();
	}
}
