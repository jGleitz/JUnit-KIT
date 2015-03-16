package test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Contains Matchers that are not provided {@code #org.hamcrest.CoreMatchers} but needed by a test.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 * @since 02.02.2015
 */
public class KitMatchers {

	/**
	 * Creates a matcher that matches if the examined String contains exactly the elements in the specified String
	 * Array, separated exactly by {@code divider}.
	 * 
	 * @param substrings
	 *            the substrings that the returned matcher will expect to be contained by any examined string
	 * @param divider
	 *            the divider String that the returned matcher will expect the Strings of {@code substrings} to be
	 *            divided by in any examined string
	 */
	public static Matcher<String> containsExactlyDividedBy(final String[] substrings, final String divider) {
		return new BaseMatcher<String>() {
			private List<String> notContained = new LinkedList<>(Arrays.asList(substrings));
			private boolean hadTooMuch;

			@Override
			public boolean matches(final Object testObject) {
				String testString = (String) testObject;
				boolean contains = true;
				int length = 0;
				// we don't break at a mismatch as we want to have the notContained list ready
				for (String sub : substrings) {
					length += sub.length();
					int innerIndex = testString.indexOf(divider + sub + divider);
					int lastItemIndex = (testString.length() - sub.length() - divider.length());

					boolean found = ((innerIndex > 0) && (innerIndex < lastItemIndex)); // as inner
					found = found || (testString.indexOf(sub + divider) == 0); // at start
					found = found || (testString.indexOf(divider + sub) == lastItemIndex); // at end

					if (found) {
						notContained.remove(sub);
					}

					contains = contains && found;
				}

				length += (substrings.length - 1) * divider.length();
				hadTooMuch = contains && (testString.length() != length);
				return contains && !hadTooMuch;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("A String containing exactly ").appendValueList("", ", ", "", substrings)
						.appendText(" divided exactly by ").appendValue(divider);
			}

			@Override
			public void describeMismatch(final Object item, final Description description) {
				description.appendText("was ").appendValue(item);
				if (!notContained.isEmpty()) {
					description.appendText("\nnot containing these items correctly: ").appendValueList("", ", ", "",
						notContained);
				}
				if (hadTooMuch) {
					description.appendText("\nhaving to many characters");
				}
			}

		};
	}

	/**
	 * Creates a matcher that matches if the examined Integer suits the needs implied by {@code systemExitStatus}.
	 * 
	 * @param systemExitStatus
	 *            The exit status any examined Integer is expected to match
	 * @param mandatory
	 *            Whether it was mandatory for the tested class to call {@code System.exit} the way defined by
	 *            {@code SystemExitStatus}.
	 */
	public static Matcher<Integer> suits(final SystemExitStatus systemExitStatus, final boolean mandatory) {
		return new BaseMatcher<Integer>() {

			@Override
			public boolean matches(final Object testObject) {
				Integer status = (Integer) testObject;
				return systemExitStatus.matches(status, mandatory);
			}

			@Override
			public void describeTo(Description description) {
				description.appendText(systemExitStatus.toString());
			}

			@Override
			public void describeMismatch(final Object item, final Description description) {
				if (item == null) {
					description.appendText("was no call to System.exit at all");
				} else {
					description.appendText("was System.exit(" + item + ")");
				}
			}
		};
	}

	/**
	 * Creates a matcher that matches if the examined String array contains exactly as much elements as specified by
	 * {@code count}. This matcher is meant to assert a tested class' output. It provides a appropriate error message.
	 * 
	 * @param count
	 *            How many elements the examined array shall have.
	 * @param outputDescription
	 *            A description of what was counted here (e.g. lines, calls to a function…). The {@code i}th element of
	 *            the array corresponds to {@code i} instances of the counted. If {@code n} array elements are provided,
	 *            {@code n-1} will be selected for all {@code i≥n-1} <br>
	 *            Example: <code>new String[]{"lines", "line", "lines"}</code>
	 */
	public static Matcher<String[]> hasExcactlyThatMuch(final int count, final String[] outputDescription) {
		return hasExcactlyThatMuch(count, outputDescription, null);
	}

	/**
	 * Creates a matcher that matches if the examined String array contains exactly as much elements as specified by
	 * {@code count}. This matcher is meant to assert a tested class' output. It provides a appropriate error message.
	 * 
	 * @param count
	 *            How many elements the examined array shall have.
	 * @param outputDescription
	 *            A description of what was counted here (e.g. lines, calls to a function…). The {@code i}th element of
	 *            the array corresponds to {@code i} instances of the counted. If {@code n} array elements are provided,
	 *            {@code n-1} will be selected for all {@code i≥n-1} <br>
	 *            Example: <code>new String[]{"lines", "line", "lines"}</code>
	 * @param expectedOverride
	 *            If not {@code null}, this String will used to describe the expected output instead of a generated
	 *            message based on {@code outputDescription}. The actual count will still be generated.
	 */
	public static Matcher<String[]> hasExcactlyThatMuch(final int count, final String[] outputDescription,
			final String expectedOverride) {
		return new BaseMatcher<String[]>() {
			private String[] providedLines;

			private String description(int count) {
				return outputDescription[Math.min(count, outputDescription.length - 1)];
			}

			@Override
			public boolean matches(final Object testObject) {
				providedLines = (String[]) testObject;
				return providedLines.length == count;
			}

			@Override
			public void describeTo(Description description) {
				if (expectedOverride != null) {
					description.appendText(expectedOverride);
				} else {
					description.appendValue(count).appendText(" ").appendText(description(count));
				}
			}

			@Override
			public void describeMismatch(final Object item, final Description description) {
				description.appendText("found ").appendValue(providedLines.length).appendText(" ")
						.appendText(description(providedLines.length));
			}

		};
	}

	public static Matcher<String> anyOfRemaining(final List<Matcher<String>> matchers) {
		return new BaseMatcher<String>() {

			@Override
			public boolean matches(Object item) {
				for (Matcher<String> matcher : matchers) {
					if (matcher.matches(item)) {
						matchers.remove(matcher);
						return true;
					}
				}
				return false;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("any of the following: ");
				description.appendList("<", ">, <", ">", matchers);
			}

			@Override
			public void describeMismatch(final Object item, final Description description) {
				description.appendText("matched none of the above: ").appendValue(item);
			}

		};
	}

	public static List<Matcher<String>> inAnyOrder(List<Matcher<String>> expectedOutput) {
		List<Matcher<String>> result = new LinkedList<>();
		for (int i = 0; i < expectedOutput.size(); i++) {
			result.add(KitMatchers.anyOfRemaining(expectedOutput));
		}
		return result;
	}

}
