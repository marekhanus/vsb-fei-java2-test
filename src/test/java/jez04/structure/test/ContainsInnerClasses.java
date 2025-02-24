package jez04.structure.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.Description;

public class ContainsInnerClasses extends StructureMatcher<Class<?>> {

	private String methodNameRegexp;
	private boolean caseSensitive = true;
	private boolean useRegExp = false;
	private int count = 1;
	private List<Class<?>> params;

	public ContainsInnerClasses(String methodNameRegexp) {
		this.methodNameRegexp = methodNameRegexp;
	}

	public ContainsInnerClasses caseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		return this;
	}

	public ContainsInnerClasses count(int count) {
		this.count = count;
		return this;
	}

	public ContainsInnerClasses useRegExp(boolean useRegExp) {
		this.useRegExp = useRegExp;
		return this;
	}

	@Override
	public boolean matches(Object actual) {
		if (actual instanceof Class c) {
			long lamdaCount = structureHelper.countMethodRegexp(c, "lambda\\$.*");
			long innerClassCount = structureHelper.countClassesRegexp(c.getCanonicalName()+"\\$.*");
			long methodRefCount = 0;
			try {
				methodRefCount = structureHelper.countMethodReference(c);
			} catch (URISyntaxException  | IOException e) {
				System.out.println("Cannot count method references");
				e.printStackTrace();
			}
			return lamdaCount + innerClassCount+methodRefCount >= count;
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		params.stream().map(Class::getName).collect(Collectors.joining(", "));
		description.appendText(
				String.format("Class should have inner classses or lambdas name (regexp) of type %s %s %s with params types %s",
						methodNameRegexp, caseSensitive ? "" : "ignore case", ""));
	}

	@Override
	public void describeMismatch(Object item, Description description) {
		if (item instanceof Class c) {
			description.appendValueList("no method match from:\n      ", "\n      ", "", c.getDeclaredMethods());
		} else {
			description.appendText("mismatched item is not class type");
		}
	}
}
