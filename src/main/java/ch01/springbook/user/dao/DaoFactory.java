package ch01.springbook.user.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class DaoFactory {

	@Bean
	public UserDao userDao() {
		UserDao userDao = new UserDao();
		userDao.setDataSource(dataSource());
		return userDao;
	}

	@Bean
	public ConnectionMaker connectionMaker() {
		return new SimpleConnectionMaker();
	}

	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();

		simpleDriverDataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		simpleDriverDataSource.setUrl("jdbc:mysql://localhost/tobystudy?useUnicode=true&characterEncoding=UTF-8&useSSL=true&verifyServerCertificate=false");
		simpleDriverDataSource.setUsername("study");
		simpleDriverDataSource.setPassword("study");

		return simpleDriverDataSource;
	}
}
