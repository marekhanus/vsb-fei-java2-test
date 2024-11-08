package jez04.structure.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

class StructureHelper {

	Set<String> allClasses = getNameOfAllClasses();

	public void isInterface(Class<?> c) {
		assertTrue(c.isInterface(), c.getName() + " have to be interface.");
	}

	public void classExist(String name) {
		assertTrue(allClasses.stream().anyMatch(c -> c.endsWith(name)), "Class/Interface " + name + " not found");
	}

	public Class<?> getClass(String name) {
		String className = allClasses.stream().filter(c -> c.endsWith(name)).findAny().orElse(null);
		if (className == null) {
			Assertions.fail("Class " + name + " not found.");
		}
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

	public void hasProperty(Class<?> classDef, String propertyNameRegexp, Class<?> type, boolean array) {
		List<Field> fields = Arrays.asList(classDef.getDeclaredFields());
		assertTrue(fields.stream().anyMatch(f -> {
			if (f.getName().matches(propertyNameRegexp)) {
				if (array) {
					return f.getType().isArray() && f.getType().getComponentType().equals(type);
				} else {
					return f.getType().equals(type);
				}
			}
			return false;
		}), "No field " + propertyNameRegexp + " of type " + type.getName() + " (is array " + array + ") in class "
				+ classDef.getName());
	}

	public void hasPropertyWithAnnotation(Class<?> classDef, String propertyNameRegexp, Class<?> annotation) {
		List<Field> fields = Arrays.asList(classDef.getDeclaredFields());
		assertTrue(
				fields.stream().filter(f -> f.getName().matches(propertyNameRegexp))
						.flatMap(f -> Arrays.asList(f.getAnnotations()).stream()).map(a -> a.annotationType())
						.anyMatch(a -> a.equals(annotation)),
				"No field " + propertyNameRegexp + " with annotation " + annotation.getName() + " in class "
						+ classDef.getName());
	}

	public void hasMethod(Class<?> interfaceDef, String methodName, Class<?> returnType) {
		List<Method> methods = Arrays.asList(interfaceDef.getDeclaredMethods());
		assertTrue(methods.stream().anyMatch(m -> m.getName().contains(methodName)), "No method " + methodName);
		assertTrue(
				methods.stream().filter(m -> m.getName().contains(methodName))
						.anyMatch(m -> m.getReturnType().equals(returnType)),
				"Method " + methodName + " not return " + returnType.getName());
	}

	public void hasMethod(Class<?> interfaceDef, String methodName, Class<?> returnType, Class<?>... params) {
		List<Method> methods = Arrays.asList(interfaceDef.getDeclaredMethods());
		assertTrue(methods.stream().anyMatch(m -> m.getName().contains(methodName)), "No method " + methodName);
		assertTrue(
				methods.stream().filter(m -> m.getName().contains(methodName))
						.filter(m -> m.getReturnType().equals(returnType))
						.anyMatch(m -> Arrays.asList(m.getParameterTypes()).containsAll(Arrays.asList(params))),
				"Method " + methodName + " has no all parrams:"
						+ Arrays.asList(params).stream().map(Class::getName).collect(Collectors.joining(", ")));
	}

	public long countMethodRegexp(Class<?> interfaceDef, String methodNameRegexp) {
		List<Method> methods = Arrays.asList(interfaceDef.getDeclaredMethods());
		return methods.stream().filter(m -> m.getName().matches(methodNameRegexp)).count();
	}
	public long countClassesRegexp(String classNameRegexp) {
		return getNameOfAllClasses().stream().filter(className -> className.matches(classNameRegexp)).count();
	}

	public void hasMethodRegexp(Class<?> interfaceDef, String methodNameRegexp, Class<?> returnType,
			Class<?>... params) {
		List<Method> methods = Arrays.asList(interfaceDef.getDeclaredMethods());
		assertTrue(methods.stream().anyMatch(m -> m.getName().matches(methodNameRegexp)),
				"No method " + methodNameRegexp);
		assertTrue(
				methods.stream().filter(
						m -> 
						m.getName().matches(methodNameRegexp))
						.filter(
								m -> 
								m.getReturnType().equals(returnType))
						.anyMatch(m ->
						Arrays.asList(m.getParameterTypes()).containsAll(Arrays.asList(params))),
				"Method " + methodNameRegexp + " has no all parrams:"
						+ Arrays.asList(params).stream().map(Class::getName).collect(Collectors.joining(", ")));
	}

	public void hasMethodRegexp(Class<?> interfaceDef, String methodNameRegexp, Collection<Class<?>> returnTypeOnOf,
			Class<?>... params) {
		List<Method> methods = Arrays.asList(interfaceDef.getDeclaredMethods());
		assertTrue(methods.stream().anyMatch(m -> m.getName().matches(methodNameRegexp)),
				"No method " + methodNameRegexp);
		assertTrue(
				methods.stream().filter(
						m -> 
						m.getName().matches(methodNameRegexp))
						.filter(
								m -> 
								returnTypeOnOf.contains(m.getReturnType()))
						.anyMatch(m ->
						Arrays.asList(m.getParameterTypes()).containsAll(Arrays.asList(params))),
				"Method " + methodNameRegexp + " has no all parrams:"
						+ Arrays.asList(params).stream().map(Class::getName).collect(Collectors.joining(", ")));
	}

	public void hasMethod(Class<?> interfaceDef, boolean finalTag, boolean abstractTag, String methodName,
			Class<?> returnType, Class<?>... params) {
		List<Method> methods = Arrays.asList(interfaceDef.getDeclaredMethods());
		assertTrue(methods.stream().anyMatch(m -> m.getName().contains(methodName)), "No method " + methodName);
		assertTrue(
				methods.stream().filter(m -> m.getName().contains(methodName))
						.filter(m -> m.getReturnType().equals(returnType)
								&& (Modifier.isAbstract(m.getModifiers()) == abstractTag)
								&& (Modifier.isFinal(m.getModifiers()) == finalTag))
						.anyMatch(m -> Arrays.asList(m.getParameterTypes()).containsAll(Arrays.asList(params))),
				"Method " + methodName + " has no all params:"
						+ Arrays.asList(params).stream().map(Class::getName).collect(Collectors.joining(", ")));
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

	public void hasMethod(Class<?> interfaceDef, String methodName) {
		List<Method> methods = Arrays.asList(interfaceDef.getMethods());
		assertTrue(methods.stream().anyMatch(m -> m.getName().contains(methodName)), "No method " + methodName);
	}

	public Set<String> getNameOfAllClasses() {
		List<String> initClassesName = new ArrayList<>();
		dynamicaliFoundSomeClass(initClassesName);
		initClassesName.addAll(List.of("lab.Routines", "lab.App", "lab.DrawingThread"));
		for (String className : initClassesName) {
			try {
				Class.forName(className);
				break;
			} catch (ClassNotFoundException e) {
				System.out.println(String.format("Class '%s' cannot be loaded: %s", className, e.getMessage()));
			}
		}
		Set<String> allClasses = new HashSet<>();
		for (Package p : Package.getPackages()) {
			if (p.getName().startsWith("java.") || p.getName().startsWith("com.") || p.getName().startsWith("jdk.")
					|| p.getName().startsWith("javafx.") || p.getName().startsWith("org.")
					|| p.getName().startsWith("sun.") || p.getName().startsWith("javax.")
					|| p.getName().startsWith("javassist")) {
				continue;
			}
			System.out.println(p.getName());
			Configuration conf = new ConfigurationBuilder().addScanners(Scanners.SubTypes.filterResultsBy(pc -> true))
					.forPackages(p.getName());
			Reflections reflections = new Reflections(conf);
			allClasses.addAll(reflections.getAll(Scanners.SubTypes.filterResultsBy(c -> {
				System.out.println(c);
				return true;
			})));
		}
		for (String string : allClasses) {
			System.out.println(string);
		}
		return allClasses;
	}

	public void dynamicaliFoundSomeClass(List<String> initClassesName) {
		URL myClassUrl = StructureHelper.class.getResource("ClassStructureTest.class");
		myClassUrl.getFile();
		try {
			Path classRoot = Paths.get(myClassUrl.toURI()).getParent().getParent().getParent().getParent();
			if ("test-classes".equals(classRoot.getFileName().toString())) {
				classRoot = classRoot.getParent().resolve("classes");
			}
			System.out.println("class root: " + classRoot);
			final Path classRootFinal = classRoot;
			Files.walkFileTree(classRoot, new FileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					if (List.of("jez04", "META-INF").contains(dir.getFileName().toString())) {
						return FileVisitResult.SKIP_SUBTREE;
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					System.out.println("VISIT: " + file);
					if ("module-info.class".equals(file.getFileName().toString())) {
						return FileVisitResult.CONTINUE;
					}
					if (!file.getFileName().toString().endsWith(".class")) {
						return FileVisitResult.CONTINUE;
					}
					String foundClassName = classRootFinal.relativize(file).toString();
					foundClassName = foundClassName.substring(0, foundClassName.length() - 6)
							.replace(File.separatorChar, '.');
					initClassesName.add(foundClassName);
					return FileVisitResult.TERMINATE;
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
}
