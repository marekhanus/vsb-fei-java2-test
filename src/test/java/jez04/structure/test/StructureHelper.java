package jez04.structure.test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.Assertions;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

class StructureHelper {

	private static StructureHelper singeltonInstance;

	public static StructureHelper getInstance() {
		if (singeltonInstance == null) {
			singeltonInstance = new StructureHelper();
		}
		return singeltonInstance;
	}

	Set<String> allClasses = getNameOfAllClasses();

	private StructureHelper() {
		/* hide public one */
	}

	public Set<String> getAllClasses() {
		return allClasses;
	}

	public void isInterface(Class<?> c) {
		assertTrue(c.isInterface(), c.getName() + " have to be interface.");
	}

	public void classExist(String name) {
		classExist(name, true);
	}

	public void classExist(String name, boolean caseSensitive) {
		assertTrue(
				allClasses.stream()
						.anyMatch(c -> caseSensitive ? c.endsWith(name) : c.toLowerCase().endsWith(name.toLowerCase())),
				"Class/Interface " + name + " not found");
	}

	public void classExistRegexp(String name) {
		classExistRegexp(name, true);
	}

	public void classExistRegexp(String name, boolean caseSensitive) {
		assertTrue(
				allClasses.stream()
						.anyMatch(c -> caseSensitive ? c.matches(name) : c.toLowerCase().matches(name.toLowerCase())),
				"Class/Interface " + name + " not found");
	}

	public Class<?> getClassDirectly(String name) {
		return loadClass(name, name);
	}

	public Class<?> getClassRegexp(String name) {
		return getClassRegexp(name, true);
	}

	public Class<?> getClassRegexp(String name, boolean caseSensitive) {
		String className = allClasses.stream()
				.filter(c -> caseSensitive ? c.matches(name) : c.toLowerCase().matches(name.toLowerCase())).findAny()
				.orElse(null);
		if (className == null) {
			Assertions.fail("Class " + name + " not found.");
		}
		return loadClass(name, className);
	}

	public Class<?> getClass(String name) {
		return getClass(name, true);
	}

	public Class<?> getClass(String name, boolean caseSensitive) {
		String className = allClasses.stream()
				.filter(c -> caseSensitive ? c.endsWith(name) : c.toLowerCase().endsWith(name.toLowerCase())).findAny()
				.orElse(null);
		if (className == null) {
			Assertions.fail("Class " + name + " not found.");
		}
		return loadClass(name, className);
	}

	private Class<?> loadClass(String name, String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (PrintStream ps = new PrintStream(baos, true)) {
				e.printStackTrace(ps);
			} catch (Exception e2) {
				Assertions.fail(e2.getMessage());
			}
			String stackTrace = baos.toString();
			Assertions.fail("Class " + name + " not found.\n" + stackTrace);
			return null;
		}
	}

	public org.hamcrest.Matcher<Class<?>> hasProperty(String propertyNameRegexp, Class<?> type, boolean array) {
		return new HasProperty(propertyNameRegexp, type, array);
	}

	public void hasProperty(Class<?> classDef, String propertyNameRegexp, Class<?> type, boolean array) {
		hasProperty(classDef, propertyNameRegexp, type, array, true);
	}

	public void hasProperty(Class<?> classDef, String propertyNameRegexp, Class<?> type, boolean array,
			boolean caseSensitive) {
		assertTrue(hasPropertyB(classDef, propertyNameRegexp, type, array, caseSensitive),
				"No field " + propertyNameRegexp + " of type " + type.getName() + " (is array " + array + ") in class "
						+ classDef.getName());
	}

	public boolean hasPropertyB(Class<?> classDef, String propertyNameRegexp, Class<?> type, boolean array,
			boolean caseSensitive) {
		List<Field> fields = Arrays.asList(classDef.getDeclaredFields());
		return fields.stream().anyMatch(f -> {
			if (caseSensitive ? f.getName().matches(propertyNameRegexp)
					: f.getName().toLowerCase().matches(propertyNameRegexp.toLowerCase())) {
				if (array) {
					return f.getType().isArray() && f.getType().getComponentType().equals(type);
				} else {
					return f.getType().equals(type);
				}
			}
			return false;
		});
	}

	public void hasPropertyWithAnnotation(Class<?> classDef, String propertyNameRegexp, Class<?> annotation) {
		hasPropertyWithAnnotation(classDef, propertyNameRegexp, annotation, true);
	}

	public void hasPropertyWithAnnotation(Class<?> classDef, String propertyNameRegexp, Class<?> annotation,
			boolean caseSensitive) {
		List<Field> fields = Arrays.asList(classDef.getDeclaredFields());
		assertTrue(
				fields.stream()
						.filter(f -> caseSensitive ? f.getName().matches(propertyNameRegexp)
								: f.getName().toLowerCase().matches(propertyNameRegexp.toLowerCase()))
						.flatMap(f -> Arrays.asList(f.getAnnotations()).stream()).map(a -> a.annotationType())
						.anyMatch(a -> a.equals(annotation)),
				"No field " + propertyNameRegexp + " with annotation " + annotation.getName() + " in class "
						+ classDef.getName());
	}

	public void hasMethod(Class<?> interfaceDef, String methodName, Class<?> returnType) {
		hasMethod(interfaceDef, methodName, returnType, true);
	}

	public void hasMethod(Class<?> interfaceDef, String methodName, Class<?> returnType, boolean caseSensitive) {
		List<Method> methods = Arrays.asList(interfaceDef.getDeclaredMethods());
		assertTrue(methods.stream().anyMatch(m -> m.getName().contains(methodName)), "No method " + methodName);
		assertTrue(
				methods.stream()
						.filter(m -> caseSensitive ? m.getName().matches(methodName)
								: m.getName().toLowerCase().matches(methodName.toLowerCase()))
						.anyMatch(m -> m.getReturnType().equals(returnType)),
				"Method " + methodName + " not return " + returnType.getName());
	}

	public void hasMethod(Class<?> interfaceDef, String methodName, Class<?> returnType, Class<?>... params) {
		hasMethod(interfaceDef, methodName, true, returnType, params);
	}

	public void hasMethod(Class<?> interfaceDef, String methodName, boolean caseSensitive, Class<?> returnType,
			Class<?>... params) {
		List<Method> methods = Arrays.asList(interfaceDef.getDeclaredMethods());
		assertTrue(
				methods.stream()
						.anyMatch(m -> caseSensitive ? m.getName().matches(methodName)
								: m.getName().toLowerCase().matches(methodName.toLowerCase())),
				"No method " + methodName);
		assertTrue(
				methods.stream()
						.filter(m -> caseSensitive ? m.getName().matches(methodName)
								: m.getName().toLowerCase().matches(methodName.toLowerCase()))
						.filter(m -> m.getReturnType().equals(returnType))
						.anyMatch(m -> Arrays.asList(m.getParameterTypes()).containsAll(Arrays.asList(params))),
				"Method " + methodName + " has no all parrams:"
						+ Arrays.asList(params).stream().map(Class::getName).collect(Collectors.joining(", ")));
	}

	public Method getMethod(Class<?> interfaceDef, String methodName, Class<?> returnType, Class<?>... params) {
		return getMethod(interfaceDef, methodName, true, returnType, params);
	}

	public Method getMethod(Class<?> interfaceDef, String methodName, boolean caseSensitive, Class<?> returnType,
			Class<?>... params) {
		List<Method> methods = Arrays.asList(interfaceDef.getDeclaredMethods());
		List<Method> foundMethods = methods.stream()
				.filter(m -> caseSensitive ? m.getName().matches(methodName)
						: m.getName().toLowerCase().matches(methodName.toLowerCase()))
				.filter(m -> m.getReturnType().equals(returnType))
				.filter(m -> Arrays.asList(m.getParameterTypes()).containsAll(Arrays.asList(params))).toList();
		if (foundMethods.isEmpty()) {
			fail("No method " + methodName + " found");
		}
		if (foundMethods.size() > 1) {
			fail("More then one method " + methodName + " found");
		}
		return foundMethods.get(0);
	}

	public long countMethodRegexp(Class<?> interfaceDef, String methodNameRegexp) {
		return countMethodRegexp(interfaceDef, methodNameRegexp, true);
	}

	public long countMethodRegexp(Class<?> interfaceDef, String methodNameRegexp, boolean caseSensitive) {
		List<Method> methods = Arrays.asList(interfaceDef.getDeclaredMethods());
		return methods.stream().filter(m -> caseSensitive ? m.getName().matches(methodNameRegexp)
				: m.getName().toLowerCase().matches(methodNameRegexp.toLowerCase())).count();
	}

	public long countMethodReference(Class<?> interfaceDef) throws URISyntaxException, IOException {
		Pattern p = Pattern.compile("::");
		Matcher m = p.matcher(getSourceCode(interfaceDef));
		return m.results().count();
	}

	public long countMethodReferenceOn(Class<?> interfaceDef, String to) {
		try {
			Pattern p = Pattern.compile(to + "::");
			Matcher m = p.matcher(getSourceCode(interfaceDef));
			return m.results().count();
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public long countClassesRegexp(String classNameRegexp) {
		return countClassesRegexp(classNameRegexp, true);
	}

	public long countClassesRegexp(String classNameRegexp, boolean caseSensitive) {
		return getNameOfAllClasses().stream().filter(className -> caseSensitive ? className.matches(classNameRegexp)
				: className.toLowerCase().matches(classNameRegexp.toLowerCase())).count();
	}

	public void hasConstructor(Class<?> classDef, Class<?>... params) {
		getConstructor(classDef, params);
	}

	public Constructor<?> getConstructor(Class<?> classDef, Class<?>... params) {
		List<Constructor<?>> constructors = Arrays.asList(classDef.getConstructors());
		List<Constructor<?>> foundConstructors = constructors.stream()
				.filter(m -> m.getParameterCount() == params.length)
				.filter(m -> Arrays.asList(m.getParameterTypes()).containsAll(Arrays.asList(params))).toList();
		if (foundConstructors.isEmpty()) {
			fail("No constructor found with parameters: "
					+ Arrays.asList(params).stream().map(Class::getName).collect(Collectors.joining(", ")));
		}
		if (foundConstructors.size() > 1) {
			fail("More then one constructor found with parameters: "
					+ Arrays.asList(params).stream().map(Class::getName).collect(Collectors.joining(", ")));
		}
		return foundConstructors.get(0);
	}

	public void hasMethodRegexp(Class<?> interfaceDef, String methodNameRegexp, Class<?> returnType,
			Class<?>... params) {
		hasMethodRegexp(interfaceDef, methodNameRegexp, true, returnType, params);
	}

	public void hasMethodRegexp(Class<?> interfaceDef, String methodNameRegexp, boolean caseSensitive,
			Class<?> returnType, Class<?>... params) {
		List<Method> methods = Arrays.asList(interfaceDef.getDeclaredMethods());
		assertTrue(
				methods.stream()
						.anyMatch(m -> caseSensitive ? m.getName().matches(methodNameRegexp)
								: m.getName().toLowerCase().matches(methodNameRegexp.toLowerCase())),
				"No method " + methodNameRegexp);
		assertTrue(
				methods.stream()
						.filter(m -> caseSensitive ? m.getName().matches(methodNameRegexp)
								: m.getName().toLowerCase().matches(methodNameRegexp.toLowerCase()))
						.filter(m -> m.getReturnType().equals(returnType))
						.anyMatch(m -> Arrays.asList(m.getParameterTypes()).containsAll(Arrays.asList(params))),
				"Method " + methodNameRegexp + " has no all parrams:"
						+ Arrays.asList(params).stream().map(Class::getName).collect(Collectors.joining(", ")));
	}

	public boolean hasMethodRegexpTest(Class<?> interfaceDef, String methodNameRegexp, boolean caseSensitive,
			Class<?> returnType, Class<?>... params) {
		return hasMethodRegexpTest(interfaceDef, methodNameRegexp, caseSensitive, returnType, List.of(params));
	}

	public boolean hasMethodRegexpTest(Class<?> interfaceDef, String methodNameRegexp, boolean caseSensitive,
			Class<?> returnType, List<Class<?>> params) {
		List<Method> methods = Arrays.asList(interfaceDef.getDeclaredMethods());
		if (!methods.stream().anyMatch(m -> caseSensitive ? m.getName().matches(methodNameRegexp)
				: m.getName().toLowerCase().matches(methodNameRegexp.toLowerCase()))) {
			return false;
		}
		return methods.stream()
				.filter(m -> caseSensitive ? m.getName().matches(methodNameRegexp)
						: m.getName().toLowerCase().matches(methodNameRegexp.toLowerCase()))
				.filter(m -> m.getReturnType().equals(returnType))
				.anyMatch(m -> Arrays.asList(m.getParameterTypes()).containsAll(params));
	}

	public long countMethodRegexp(Class<?> interfaceDef, String methodNameRegexp, Class<?> returnType,
			Class<?>... params) {
		return countMethodRegexp(interfaceDef, methodNameRegexp, true, returnType, params);
	}

	public long countMethodRegexp(Class<?> interfaceDef, String methodNameRegexp, boolean caseSensitive,
			Class<?> returnType, Class<?>... params) {
		List<Method> methods = Arrays.asList(interfaceDef.getDeclaredMethods());
		assertTrue(
				methods.stream()
						.anyMatch(m -> caseSensitive ? m.getName().matches(methodNameRegexp)
								: m.getName().toLowerCase().matches(methodNameRegexp.toLowerCase())),
				"No method " + methodNameRegexp);
		return methods.stream()
				.filter(m -> caseSensitive ? m.getName().matches(methodNameRegexp)
						: m.getName().toLowerCase().matches(methodNameRegexp.toLowerCase()))
				.filter(m -> m.getReturnType().equals(returnType))
				.filter(m -> Arrays.asList(m.getParameterTypes()).containsAll(Arrays.asList(params))).count();
	}

	public boolean hasMethodTest(Class<?> interfaceDef, boolean finalTag, boolean abstractTag, String methodName,
			boolean caseSensitive, Class<?> returnType, Class<?>... params) {
		return hasMethodTest(interfaceDef, finalTag, abstractTag, methodName, caseSensitive, returnType, List.of(params));
	}
	public boolean hasMethodTest(Class<?> interfaceDef, boolean finalTag, boolean abstractTag, String methodName,
			boolean caseSensitive, Class<?> returnType, List<Class<?>> params) {
		List<Method> methods = Arrays.asList(interfaceDef.getDeclaredMethods());
		if (!methods.stream().anyMatch(m -> caseSensitive ? m.getName().matches(methodName)
				: m.getName().toLowerCase().matches(methodName.toLowerCase()))) {
			return false;
		}
		return methods.stream()
				.filter(m -> caseSensitive ? m.getName().matches(methodName)
						: m.getName().toLowerCase().matches(methodName.toLowerCase()))
				.filter(m -> m.getReturnType().equals(returnType)
						&& (Modifier.isAbstract(m.getModifiers()) == abstractTag)
						&& (Modifier.isFinal(m.getModifiers()) == finalTag))
				.anyMatch(m -> Arrays.asList(m.getParameterTypes()).containsAll(params));
	}

	public void hasMethod(Class<?> interfaceDef, boolean finalTag, boolean abstractTag, String methodName,
			Class<?> returnType, Class<?>... params) {
		hasMethod(interfaceDef, finalTag, abstractTag, methodName, true, returnType, params);
	}

	public void hasMethod(Class<?> interfaceDef, boolean finalTag, boolean abstractTag, String methodName,
			boolean caseSensitive, Class<?> returnType, Class<?>... params) {
		List<Method> methods = Arrays.asList(interfaceDef.getDeclaredMethods());
		assertTrue(
				methods.stream()
						.anyMatch(m -> caseSensitive ? m.getName().matches(methodName)
								: m.getName().toLowerCase().matches(methodName.toLowerCase())),
				"No method " + methodName);
		assertTrue(
				methods.stream()
						.filter(m -> caseSensitive ? m.getName().matches(methodName)
								: m.getName().toLowerCase().matches(methodName.toLowerCase()))
						.filter(m -> m.getReturnType().equals(returnType)
								&& (Modifier.isAbstract(m.getModifiers()) == abstractTag)
								&& (Modifier.isFinal(m.getModifiers()) == finalTag))
						.anyMatch(m -> Arrays.asList(m.getParameterTypes()).containsAll(Arrays.asList(params))),
				"Method " + methodName + " has no all params:"
						+ Arrays.asList(params).stream().map(Class::getName).collect(Collectors.joining(", ")));
	}

	public boolean isDescendatOf(Class<?> clazz, String interfaceName) {
		return getClass(interfaceName).isAssignableFrom(clazz);
	}

	public void hasImplements(Class<?> clazz, String... interfaceNames) {
		List<Class<?>> interfaces = new ArrayList<>();
		Arrays.asList(interfaceNames).stream().map(name -> getClass(name)).forEach(c -> interfaces.add(c));
		assertTrue(Arrays.asList(clazz.getInterfaces()).containsAll(interfaces), "Class not implements all interfaces:"
				+ interfaces.stream().map(Class::getName).collect(Collectors.joining(", ")));
	}

	public void hasExtends(Class<?> clazz, String parentName) {
		Class<?> parent = getClass(parentName);
		assertTrue(clazz.getSuperclass().equals(parent),
				"Class " + clazz.getName() + " not extends class " + parentName);
	}

	public void hasExtends(Class<?> clazz, Class<?> parent) {
		assertTrue(clazz.getSuperclass().equals(parent),
				"Class " + clazz.getName() + " not extends class " + parent.getCanonicalName());
	}

	public void hasMethod(Class<?> interfaceDef, String methodName) {
		hasMethod(interfaceDef, methodName, true);
	}

	public void hasMethod(Class<?> interfaceDef, String methodName, boolean caseSensitive) {
		List<Method> methods = Arrays.asList(interfaceDef.getMethods());
		assertTrue(
				methods.stream()
						.anyMatch(m -> caseSensitive ? m.getName().matches(methodName)
								: m.getName().toLowerCase().matches(methodName.toLowerCase())),
				"No method " + methodName);
	}

	public String getSourceCode(Class<?> clazz) throws URISyntaxException, IOException {
		URL myClassUrl = StructureHelper.class.getResource(this.getClass().getSimpleName() + ".class");
		Path classRoot = Paths.get(myClassUrl.toURI());
		while (!"test-classes".equals(classRoot.getFileName().toString())
				&& !"classes".equals(classRoot.getFileName().toString())) {
			classRoot = classRoot.getParent();
		}
		Path srcRoot = classRoot.getParent().getParent().resolve(Paths.get("src", "main", "java"));
		System.out.println("class root: " + classRoot);
		Path srcPath = srcRoot.resolve(clazz.getCanonicalName().replace(".", File.separator) + ".java");
		return Files.readString(srcPath);
	}

	public Set<String> getNameOfAllClasses() {
		Set<String> allClassesName = new TreeSet<>();
		dynamicalyFoundSomeClass(allClassesName);
//		allClassesName.addAll(List.of("cz.vsb.fei.lab.App", "lab.Routines", "lab.App", "lab.DrawingThread"));
		for (String className : allClassesName) {
			try {
				Class.forName(className);
				break;
			} catch (ClassNotFoundException e) {
				System.out.println(String.format("Class '%s' cannot be loaded: %s", className, e.getMessage()));
			}
		}
		for (Package p : Package.getPackages()) {
			if (p.getName().startsWith("java.") || p.getName().startsWith("com.") || p.getName().startsWith("jdk.")
					|| p.getName().startsWith("javafx.") || p.getName().startsWith("org.")
					|| p.getName().startsWith("sun.") || p.getName().startsWith("javax.")
					|| p.getName().startsWith("javassist")) {
				continue;
			}
//			System.out.println(p.getName());
			Configuration conf = new ConfigurationBuilder().addScanners(Scanners.SubTypes.filterResultsBy(pc -> true))
					.forPackages(p.getName());
			Reflections reflections = new Reflections(conf);
			allClassesName.addAll(reflections.getAll(Scanners.SubTypes.filterResultsBy(c -> {
//				System.out.println(">>> " + c);
				return true;
			})));
		}
		return allClassesName;
	}

	private static final List<String> dirsToSkip = List.of("jez04", "META-INF");
	private static final List<String> filesToSkip = List.of("module-info.class");

	public void dynamicalyFoundSomeClass(Set<String> allClassesName) {
		URL myClassUrl = StructureHelper.class.getResource(this.getClass().getSimpleName() + ".class");
		try {
			Path classRoot = Paths.get(myClassUrl.toURI());
			while (!"test-classes".equals(classRoot.getFileName().toString())
					&& !"classes".equals(classRoot.getFileName().toString())) {
				classRoot = classRoot.getParent();
			}
			if ("test-classes".equals(classRoot.getFileName().toString())) {
				classRoot = classRoot.getParent().resolve("classes");
			}
//			System.out.println("class root: " + classRoot);
			final Path classRootFinal = classRoot;
			Files.walkFileTree(classRoot, new FileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					if (dirsToSkip.contains(dir.getFileName().toString())) {
						return FileVisitResult.SKIP_SUBTREE;
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (filesToSkip.contains(file.getFileName().toString())) {
						return FileVisitResult.CONTINUE;
					}
					if (!file.getFileName().toString().endsWith(".class")) {
						return FileVisitResult.CONTINUE;
					}
					if (file.getFileName().toString().contains("$")) {
						return FileVisitResult.CONTINUE;
					}
					String foundClassName = classRootFinal.relativize(file).toString();
					foundClassName = foundClassName.substring(0, foundClassName.length() - 6)
							.replace(File.separatorChar, '.');
					addClassAndAllRef(allClassesName, foundClassName);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	private void addClassAndAllRef(Set<String> allClassesName, String foundClassName) {
		allClassesName.add(foundClassName);
		try {
			Class<?> foundClass = Class.forName(foundClassName);
			List.of(foundClass.getInterfaces()).stream().map(Class::getCanonicalName).forEach(allClassesName::add);
			List.of(foundClass.getDeclaredClasses()).stream().map(Class::getCanonicalName).forEach(allClassesName::add);
			List.of(foundClass.getDeclaredFields()).stream().map(Field::getType)
					.map(clazz -> clazz.isArray() ? clazz.componentType() : clazz)
					.filter(Predicate.not(Class::isPrimitive)).map(Class::getCanonicalName)
					.forEach(allClassesName::add);
			List.of(foundClass.getDeclaredMethods()).stream().map(Method::getReturnType)
					.map(clazz -> clazz.isArray() ? clazz.componentType() : clazz)
					.filter(Predicate.not(Class::isPrimitive)).map(Class::getCanonicalName)
					.forEach(allClassesName::add);
			List.of(foundClass.getDeclaredMethods()).stream().flatMap(m -> List.of(m.getParameters()).stream())
					.map(Parameter::getType).map(clazz -> clazz.isArray() ? clazz.componentType() : clazz)
					.filter(Predicate.not(Class::isPrimitive)).map(Class::getCanonicalName)
					.forEach(allClassesName::add);
			List.of(foundClass.getDeclaredMethods()).stream().flatMap(m -> List.of(m.getExceptionTypes()).stream())
					.map(clazz -> clazz.isArray() ? clazz.componentType() : clazz)
					.filter(Predicate.not(Class::isPrimitive)).map(Class::getCanonicalName)
					.forEach(allClassesName::add);
			if (foundClass.getSuperclass() != null) {
				allClassesName.add(foundClass.getSuperclass().getCanonicalName());
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
