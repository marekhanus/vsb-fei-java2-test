package jez04.structure.test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.hamcrest.Description;

public class HasProperty extends StructureMatcher<Class<?>> {

	private String propertyNameRegexp;
	private Class<?> type;
	private Predicate<Type> genericTypeFilter;
	private Predicate<Class<?>> typeFilter;
	private Boolean array = false;
	private boolean caseSensitive;
	private Class<?> annotation = null;
	private int count = 1;

	public HasProperty(String propertyNameRegexp, Class<?> type, Boolean array) {
		this(propertyNameRegexp, type, array, true);
	}

	public HasProperty(String propertyNameRegexp, Class<?> type, Boolean array, boolean caseSensitive) {
		this.propertyNameRegexp = propertyNameRegexp;
		this.type = type;
		this.array = array;
		this.caseSensitive = caseSensitive;
	}

	public HasProperty annotation(Class<?> annotation) {
		this.annotation = annotation;
		return this;
	}

	public HasProperty typeFilter(Predicate<Class<?>> typeFilter) {
		this.typeFilter = typeFilter;
		return this;
	}

	public HasProperty genericTypeFilter(Predicate<Type> genericTypeFilter) {
		this.genericTypeFilter = genericTypeFilter;
		return this;
	}

	public HasProperty count(int count) {
		this.count = count;
		return this;
	}

	@Override
	public boolean matches(Object actual) {
		if (actual instanceof Class c) {
			Stream<?> streamOfResults;
			List<Field> fields = Arrays.asList(c.getDeclaredFields());
			Stream<Field> streamOfFields = fields.stream()
					.filter(f -> caseSensitive ? f.getName().matches(propertyNameRegexp)
							: f.getName().toLowerCase().matches(propertyNameRegexp.toLowerCase()))
					.filter(f -> type != null ? f.getType().equals(type) : true)
					.filter(f -> array != null ? f.getType().isAnnotation() == array.booleanValue() : true)
					.filter(f -> genericTypeFilter != null ? genericTypeFilter.test(f.getGenericType()) : true)
					.filter(f -> typeFilter != null ? typeFilter.test(f.getType()) : true);
			streamOfResults = streamOfFields;
			if (annotation != null) {
				streamOfResults = streamOfFields.flatMap(f -> Arrays.asList(f.getAnnotations()).stream())
						.map(a -> a.annotationType()).filter(a -> a.equals(annotation));
			}
			long actualCount = streamOfResults.count();
			return this.count <= actualCount;
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(String.format("Class should have field of type %s%s with name match regexp '%s'%s", type,
				array != null && array ? "[]" : "", propertyNameRegexp, caseSensitive ? "" : "ignore case"));
	}

	@Override
	public void describeMismatch(Object item, Description description) {
		if (item instanceof Class c) {
			description.appendValueList("none of", ", ", "match", c.getDeclaredFields());
		} else {
			description.appendText("mismatched item is not class type");
		}
	}
}
