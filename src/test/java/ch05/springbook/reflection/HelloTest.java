package ch05.springbook.reflection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import java.lang.reflect.Proxy;

public class HelloTest {

	@Test
	public void targetTest() {

		Hello hello = new HelloTarget();
		assertThat(hello.sayHello("Toby"), is("Hello Toby"));
		assertThat(hello.sayHi("Toby"), is("Hi Toby"));
		assertThat(hello.sayThankYou("Toby"), is("Thank You Toby"));
	}

	@Test
	public void simpleProxyTest() {
		Hello proxyHello = new HelloUppercase(new HelloTarget());
		assertThat(proxyHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxyHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxyHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
	}

	@Test
	public void dynamicProxyTest() {
		Hello proxyHello =
			(Hello)Proxy.newProxyInstance(getClass().getClassLoader(),
										  new Class[] { Hello.class },
										  new UppercaseHandler(new HelloTarget()));
		assertThat(proxyHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxyHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxyHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
	}
}
