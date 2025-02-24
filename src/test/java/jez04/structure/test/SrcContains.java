package jez04.structure.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import org.hamcrest.Description;

public class SrcContains extends StructureMatcher<Class<?>> {
	private String regexp;
	private boolean caseInsensitive;
	private boolean srcFound;

	public SrcContains(String regexp, boolean caseInsensitive) {
		this.regexp = regexp;
		this.caseInsensitive = caseInsensitive;
	}

	@Override
	public boolean matches(Object actual) {
		srcFound = true;
		if (actual instanceof Class c) {
			Pattern p = Pattern.compile(regexp);
			if (caseInsensitive) {
				p = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
			}
			try {
				return p.matcher(structureHelper.getSourceCode(c)).find();
			} catch (URISyntaxException | IOException e) {
				srcFound = false;
				e.printStackTrace();
				return false;
			}
		}
		return false;
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
					.appendText(String.format("source code of class %s do not contains substring that match reg exp", item));
		} else {
			description.appendText(String.format("source code of class %s was not found"));
		}
	}

}
