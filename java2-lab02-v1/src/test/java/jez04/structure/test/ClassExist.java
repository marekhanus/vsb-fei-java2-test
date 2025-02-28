package jez04.structure.test;

import org.hamcrest.Description;

public class ClassExist extends StructureMatcher<String> {

	private String className;
	private boolean useRegExp;
	private boolean caseSensitive;

	public ClassExist(String className) {
		this(className, true, false);
	}

	public ClassExist(String className, boolean caseSensitive, boolean useRegExp) {
		this.className = className;
		this.useRegExp = useRegExp;
		this.caseSensitive = caseSensitive;
	}

	@Override
	public boolean matches(Object actual) {
		if (useRegExp) {
			return structureHelper.getAllClasses().stream().anyMatch(
					c -> caseSensitive ? c.matches(className) : c.toLowerCase().matches(className.toLowerCase()));
		} else {
			return structureHelper.getAllClasses().stream().anyMatch(
					c -> caseSensitive ? c.endsWith(className) : c.toLowerCase().endsWith(className.toLowerCase()));
		}

	}

	@Override
	public void describeTo(Description description) {
		description.appendText(String.format("class/interface with name '%s' comparsion params(%s %s)  exists", className,
				caseSensitive ? "" : "no case sensitive", useRegExp ? "using regexp" : ""));
	}

	@Override
	public void describeMismatch(Object item, Description description) {
		description.appendValueList("no class match from:\n      ", "\n      ", "", structureHelper.getAllClasses());
	}

}
