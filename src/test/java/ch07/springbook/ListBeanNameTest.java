package ch07.springbook;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.AppContext;

import test.TestAppContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppContext.class, TestAppContext.class})
public class ListBeanNameTest {

	@Autowired
	private DefaultListableBeanFactory beanFactory;

	@Test
	public void beanListTest() {
		for (String beanName : beanFactory.getBeanDefinitionNames()) {
			System.out.println(beanName + " \t" + beanFactory.getBean(beanName).getClass().getName());
		}
	}
}
