package jez04.structure.test;

import org.hamcrest.Description;

public class IsDescendatOf extends StructureMatcher<Class<?>> {

	private String className;
	public IsDescendatOf(String className) {
		this.className = className;
	}
	@Override
	public boolean matches(Object actual) {
		if(actual instanceof Class c) {
			return structureHelper.getClass(className).isAssignableFrom(c);
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(String.format("cass shoud be descendant of %s", className));
	}
	
}
