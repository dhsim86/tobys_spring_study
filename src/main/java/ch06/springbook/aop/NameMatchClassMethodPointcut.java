package ch06.springbook.aop;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.util.PatternMatchUtils;

public class NameMatchClassMethodPointcut extends NameMatchMethodPointcut {

	public void setMappedClassName(String mappedClassName) {
		setClassFilter(new SimpleClassFilter(mappedClassName));
	}

	static class SimpleClassFilter implements ClassFilter {

		private String mappedName;

		private SimpleClassFilter(String mappedName) {
			this.mappedName = mappedName;
		}

		public boolean matches(Class<?> clazz) {
			return PatternMatchUtils.simpleMatch(mappedName, clazz.getSimpleName());
		}
	}
}
