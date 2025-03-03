package jez04.structure.test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.Description;

public class HasMethod extends StructureMatcher<Class<?>> {

	private String methodNameRegexp;
	private Class<?> returnType;
	private boolean caseSensitive = true;
	private boolean useRegExp = false;
	private Boolean abstractTag = null;
	private Boolean finalTag = null;
	private int count = 1;
	private List<Class<?>> params;

	public HasMethod(String methodNameRegexp, Class<?> returnType, Class<?>... params) {
		this.methodNameRegexp = methodNameRegexp;
		this.returnType = returnType;
		this.params = List.of(params);
	}

	public HasMethod caseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		return this;
	}

	public HasMethod abstractTag(Boolean abstractTag) {
		this.abstractTag = abstractTag;
		return this;
	}

	public HasMethod finalTag(Boolean finalTag) {
		this.finalTag = finalTag;
		return this;
	}

	public HasMethod count(int count) {
		this.count = count;
		return this;
	}

	public HasMethod useRegExp(boolean useRegExp) {
		this.useRegExp = useRegExp;
		return this;
	}

	@Override
	public boolean matches(Object actual) {
		if (actual instanceof Class c) {
			List<Method> methods = Arrays.asList(c.getDeclaredMethods());
			Stream<Method> streamOfMethods;
			if (useRegExp) {
				streamOfMethods = methods.stream().filter(m -> caseSensitive ? m.getName().matches(methodNameRegexp)
						: m.getName().toLowerCase().matches(methodNameRegexp.toLowerCase()));

			} else {
				streamOfMethods = methods.stream().filter(m -> caseSensitive ? m.getName().endsWith(methodNameRegexp)
						: m.getName().toLowerCase().endsWith(methodNameRegexp.toLowerCase()));
			}
			streamOfMethods = streamOfMethods
					.filter(m -> returnType != null ? returnType.equals(m.getReturnType()) : true)
					.filter(m -> finalTag != null ? Modifier.isAbstract(m.getModifiers()) == abstractTag.booleanValue()
							: true)
					.filter(m -> abstractTag != null ? Modifier.isFinal(m.getModifiers()) == finalTag.booleanValue()
							: true);
			long co = streamOfMethods.count(); 
			return co >= count;
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		params.stream().map(Class::getName).collect(Collectors.joining(", "));
		description.appendText(
				String.format("Class should have method name (regexp) of type %s %s %s with params types %s",
						returnType, methodNameRegexp, caseSensitive ? "" : "ignore case", ""));
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
