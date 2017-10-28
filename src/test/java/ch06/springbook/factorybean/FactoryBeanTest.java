package ch06.springbook.factorybean;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import test.TestApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationContext.class)
public class FactoryBeanTest {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void getMessageFromFactoryBeanTest() {
		Object message = applicationContext.getBean("message");
		assertThat(Message.class.isInstance(message), is(true));
		assertThat(((Message)message).getText(), is("Factory Bean"));
	}

	@Test
	public void getFactoryBeanTest() {
		Object factory = applicationContext.getBean("&message");
		assertThat(MessageFactoryBean.class.isInstance(factory), is(true));
	}
}
