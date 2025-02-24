package jez04.structure.test;

import org.hamcrest.Description;

public class IsInterface extends StructureMatcher<Class<?>> {

	public IsInterface() {
	}

	@Override
	public boolean matches(Object actual) {
		if (actual instanceof Class c) {
			return c.isInterface();
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(String.format("value should be interface"));
	}

}
