package jez04.structure.test;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class AllOfContinue<T> extends BaseMatcher<T> {

	private final List<Matcher<? super T>> matchers;

    @SafeVarargs
	public AllOfContinue(Matcher<? super T> ... matchers) {
        this(Arrays.asList(matchers));
    }

    public AllOfContinue(List<Matcher<? super T>> matchers) {
        this.matchers = matchers;
    }

    @Override
    public boolean matches(Object o) {
        for (Matcher<? super T> matcher : matchers) {
            if (!matcher.matches(o)) {
//                matcher.describeMismatch(o, mismatch);
              return false;
            }
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendList("(", " " + "and" + " ", ")", matchers);
    }

}
