package test;

import java.util.Arrays;
import java.util.Iterator;
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
			private String wrongPlacedDivider = null;
			private boolean hadTooMuch;

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
						notContained.remove(sub);
						boolean isAtEnd = (index == testString.length());
						boolean nextIsDivider = (!isAtEnd && testString.substring(index, index + 1).equals(divider));
						nextIsDivider = nextIsDivider && testString.length() > index + 1;
						if (nextIsDivider) {
							testString = testString.substring(0, index) + testString.substring(index + 1);
						} else {
							wrongPlacedDivider = sub;
						}
						contains = (isAtEnd || nextIsDivider);
					}
				}
				if (!contains) {
					Iterator<String> iterator = notContained.iterator();
					while (iterator.hasNext()) {
						String sub = iterator.next();
						if (testString.contains(sub)) {
							iterator.remove();
						}
					}
				}
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
				description.appendText("was ").appendValue((String) item);
				if (!notContained.isEmpty()) {
					description.appendText("\nmissing these items: ").appendValueList("", ", ", "", notContained);
				} else if (hadTooMuch) {
					description.appendText("\nhaving too many characters");
				} else if (wrongPlacedDivider != null) {
					description.appendText("\nhaving a misplaced divider after ").appendValue(wrongPlacedDivider);
				}
			}

		};
	}
}
