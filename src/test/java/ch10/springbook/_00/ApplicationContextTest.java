package ch10.springbook._00;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
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

    @Test
    public void registerBeanWithDependency() {
        StaticApplicationContext sc = new StaticApplicationContext();

        sc.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));

        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        helloDef.getPropertyValues().addPropertyValue("printer", new RuntimeBeanReference("printer"));

        sc.registerBeanDefinition("hello", helloDef);

        Hello hello = sc.getBean("hello", Hello.class);
        hello.print();

        assertThat(sc.getBean("printer").toString(), is("Hello Spring"));
    }

    @Test
    public void genericApplicationContextTest() {
        GenericApplicationContext ac = new GenericApplicationContext();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ac);

        reader.loadBeanDefinitions("ch10/bean-definition-reader-test.xml");

        ac.refresh();

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
    }

}
