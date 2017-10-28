package test;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mysql.jdbc.Driver;

import ch01.springbook.user.UserService;
import ch01.springbook.user.UserServiceTest;
import ch01.springbook.user.dao.UserDao;
import ch06.springbook.factorybean.Message;
import ch06.springbook.factorybean.MessageFactoryBean;
import ch07.springbook.sql.OxmSqlService;
import ch07.springbook.sql.SqlService;
import ch07.springbook.sql.registry.EmbeddedDbSqlRegistry;
import ch07.springbook.sql.registry.SqlRegistry;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "ch01")
public class TestApplicationContext {

	@Autowired
	private UserDao userDao;

	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/tobystudy?useUnicode=true&amp;characterEncoding=UTF-8&amp;useSSL=true&amp;verifyServerCertificate=false");
		dataSource.setUsername("study");
		dataSource.setPassword("study");

		return dataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource());
		return transactionManager;
	}

	@Bean
	public UserService testUserService() {
		UserServiceTest.TestUserService testUserService = new UserServiceTest.TestUserService();
		testUserService.setUserDao(userDao);
		return testUserService;
	}

	@Bean
	public SqlService sqlService() {
		OxmSqlService oxmSqlService = new OxmSqlService();
		oxmSqlService.setUnmarshaller(unmarshaller());
		oxmSqlService.setSqlRegistry(sqlRegistry());
		return oxmSqlService;
	}

	@Bean
	public Unmarshaller unmarshaller() {
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setContextPath("ch07.springbook.sql.reader.jaxb");
		return jaxb2Marshaller;
	}

	@Bean
	public SqlRegistry sqlRegistry() {
		EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
		embeddedDbSqlRegistry.setDataSource(embeddedDatabase());
		return embeddedDbSqlRegistry;
	}

	@Bean
	public DataSource embeddedDatabase() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL)
											.addScript("classpath:sql/embedded/schema.sql")
											.build();
	}

	@Bean
	public Message message() throws Exception {
		MessageFactoryBean messageFactoryBean = new MessageFactoryBean();
		messageFactoryBean.setText("Factory Bean");

		return messageFactoryBean.getObject();
	}
}
