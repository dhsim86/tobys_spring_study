package ch10.springbook._00;

import org.junit.Test;
import org.springframework.context.support.StaticApplicationContext;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class ApplicationContextTest {

    @Test
    public void RegisterBeanTest() {
        StaticApplicationContext sc = new StaticApplicationContext();
        sc.registerSingleton("hello1", Hello.class);

        Hello hello1 = sc.getBean("hello1", Hello.class);

        assertThat(hello1, is(notNullValue()));
    }

}
