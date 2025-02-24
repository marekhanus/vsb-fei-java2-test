package jez04.structure.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.hamcrest.Description;

public class ResourceContains extends StructureMatcher<String> {
	private String regexp;
	private boolean caseInsensitive;
	private boolean srcFound;
	private int count = 1;

	public ResourceContains(String regexp, boolean caseInsensitive) {
		this.regexp = regexp;
		this.caseInsensitive = caseInsensitive;
	}

	public ResourceContains count(int count) {
		this.count = count;
		return this;
	}

	@Override
	public boolean matches(Object actual) {
		srcFound = true;
		List<Path> foundResources = new LinkedList<>();
		Pattern p;
		if (caseInsensitive) {
			p = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
		} else {
			p = Pattern.compile(regexp);
		}
		try {
			URL myClassUrl = StructureHelper.class.getResource(this.getClass().getSimpleName() + ".class");
			Path classRoot = Paths.get(myClassUrl.toURI());
			while (!"test-classes".equals(classRoot.getFileName().toString())
					&& !"classes".equals(classRoot.getFileName().toString())) {
				classRoot = classRoot.getParent();
			}
			Path resourcesRoot = classRoot.getParent().getParent().resolve(Paths.get("src", "main", "resources"));
			System.out.println("resources root: " + resourcesRoot);
			Files.walkFileTree(resourcesRoot, new FileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (p.matcher(file.getFileName().toString()).matches()) {
						foundResources.add(file);
					}
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
			return foundResources.size() >= count;
		} catch (URISyntaxException | IOException e) {
			srcFound = false;
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(String.format("Source code of class shoud contains regexp '%s'%s", regexp,
				caseInsensitive ? " in case insensitive mode" : ""));
	}

	@Override
	public void describeMismatch(Object item, Description description) {
		if (srcFound) {
			description
					.appendText(String.format("source code of class %s do not contains substring that match reg exp"));
		} else {
			description.appendText(String.format("source code of class %s was not found"));
		}
	}

}
