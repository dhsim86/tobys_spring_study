package ch10.springbook._00;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class ApplicationContextTest {

    @Test
    public void RegisterBeanTest() {
        StaticApplicationContext sc = new StaticApplicationContext();
        sc.registerSingleton("hello1", Hello.class);

        Hello hello1 = sc.getBean("hello1", Hello.class);

        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        sc.registerBeanDefinition("hello2", helloDef);

        assertThat(hello1, is(notNullValue()));

        Hello hello2 = sc.getBean("hello2", Hello.class);
        assertThat(hello2.sayHello(), is("Hello Spring"));
        assertThat(hello1, is(not(hello2)));
        assertThat(sc.getBeanFactory().getBeanDefinitionCount(), is(2));
    }

}
