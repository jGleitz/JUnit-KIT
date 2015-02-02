package test;

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

			@Override
			public boolean matches(final Object testObject) {
				String testString = (String) testObject;
				boolean contains = true;
				int length = 0;
				for (int i = 0; i < substrings.length && contains; i++) {
					String sub = substrings[i];
					length += sub.length();
					int index = testString.indexOf(sub) + sub.length();
					contains = (index >= sub.length());
					if (contains) {
						boolean isAtEnd = (index == testString.length());
						boolean nextIsDivider = (!isAtEnd && testString.substring(index, index + 1).equals(divider));
						if (nextIsDivider) {
							testString = testString.substring(0, index) + testString.substring(index + 1);
						}
						contains = (isAtEnd || nextIsDivider);
					}
				}
				return contains && (testString.length() == length);
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("A String containing exactly ").appendValueList("", ", ", "", substrings);
			}

			@Override
			public void describeMismatch(final Object item, final Description description) {
				description.appendText("was ").appendValue((String) item);
			}

		};
	}

}
