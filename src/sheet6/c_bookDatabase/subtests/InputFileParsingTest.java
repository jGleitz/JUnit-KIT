package sheet6.c_bookDatabase.subtests;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import test.TestObject;
import test.TestObject.SystemExitStatus;

/**
 * Asserts correct parsing of the input file. The tested class should print error messages for bad files and meanwhile
 * read in good files without any output.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 * @since 31.01.2015
 */
public class InputFileParsingTest extends BookDatabaseSubTest {
	/**
	 * Tests the program's behaviour for a bad formed input file. Asserts that the program prints an error message for:
	 * <ul>
	 * <li>an empty file
	 * <li>a file with general bad syntax
	 * <li>a file with a malformed attribute name
	 * </ul>
	 */
	@Test
	public void testBadFile() {
		TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);

		// empty file
		line = "";
		errorTest("quit", "0.3", getFile(line));

		// random text
		line = "Just some random text";
		errorTest("quit", "0.3", getFile(line));

		// no value
		line = "creator=";
		errorTest("quit", "0.3", getFile(line));

		// false attribute name
		line = "creater=Max_Mustermann";
		errorTest("quit", "0.3", getFile(line));

		// comma at the end
		line = "creator=Max_Mustermann,";
		errorTest("quit", "0.3", getFile(line));

		// space in between
		line = "creator=Max_Mustermann, title=Musterbuch";
		errorTest("quit", "0.3", getFile(line));

		// Duplicate attribute keyword title
		line = "title=java,creator=reussner,title=java,year=2005";
		errorTest("quit", "0.3", getFile(line));

		// Duplicate attribute keyword year
		line = "title=java,year=2010,creator=reussner,year=2005";
		errorTest("quit", "0.3", getFile(line));

		// Duplicate attribute keyword creator
		line = "title=java,creator=reussner,creator=mustermann,year=2005";
		errorTest("quit", "0.3", getFile(line));

		// maybe a regex will fail here
		line = "creator=reussner,year=2014,tl=test";
		errorTest("quit", "0.3", getFile(line));
	}

	/**
	 * Tests the program's behaviour for a bad formed input file. Asserts that the program prints an error message for:
	 * <ul>
	 * <li>a malformed attribute value
	 * </ul>
	 */
	@Test
	public void testWrongInputFileAttributeValues() {
		TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);

		List<String> correctValues = new LinkedList<>();
		List<String> falseValues = new LinkedList<>();
		correctValues.add("creator=Max_Mustermann");
		correctValues.add("year=2010");
		correctValues.add("title=Musterbuch");

		// bad character in creator
		line = "creator=Max_Mus$termann";
		errorTest("quit", "0.3", getFile(line));
		falseValues.add(line);

		// bad character in title
		line = "title=Must&erbuch";
		errorTest("quit", "0.3", getFile(line));
		falseValues.add(line);

		// year is not a number
		line = "year=irgendetwas";
		errorTest("quit", "0.3", getFile(line));
		falseValues.add(line);

		// year is a number, but not in range
		line = "year=2016";
		errorTest("quit", "0.3", getFile(line));
		falseValues.add(line);

		// year is a number, but not in range
		line = "year=-1";
		errorTest("quit", "0.3", getFile(line));
		falseValues.add(line);

		// year is a number, is in range, but not an Integer.
		line = "year=2014.2";
		errorTest("quit", "0.3", getFile(line));
		falseValues.add(line);

		// test combinations of good and bad strings
		List<String> goodOnes;
		for (String bad : falseValues) {
			goodOnes = new LinkedList<>();
			for (String good : correctValues) {
				if (!good.contains(bad.split("=")[0])) {
					goodOnes.add(good);
				}
			}
			errorTest("quit", "0.3", getFile(goodOnes.get(0) + "," + bad));
			errorTest("quit", "0.3", getFile(goodOnes.get(1) + "," + bad));
			errorTest("quit", "0.3", getFile(goodOnes.get(0) + "," + bad + "," + goodOnes.get(1)));
		}
	}

	/**
	 * Tests the program's behaviour for well formed input files. Asserts that:
	 * <ul>
	 * <li>the program reads in the input file without printing an error message
	 * </ul>
	 */
	@Test
	public void testCorrectFiles() {
		List<String> correctLines = new LinkedList<>();

		// just creator
		line = "creator=Mustermann";
		correctLines.add(line);
		oneLineTest("quit", "", "0.5", getFile(correctLines));

		// title
		line = "title=Muster-buch";
		correctLines.add(line);
		oneLineTest("quit", "", "0.5", getFile(correctLines));

		// year
		line = "year=100";
		correctLines.add(line);
		oneLineTest("quit", "", "0.5", getFile(correctLines));

		// complete line
		line = "title=a-b-c,year=4";
		correctLines.add(line);
		oneLineTest("quit", "", "0.5", getFile(correctLines));
		
		// complete line
		line = "title=AND,creator=OR";
		correctLines.add(line);
		oneLineTest("quit", "", "0.5", getFile(correctLines));

		// incomplete line
		line = "title=Java_ist_auch_eine_Insel,creator=GalileoComputing";
		correctLines.add(line);
		oneLineTest("quit", "", "0.5", getFile(correctLines));

		// random case
		oneLineTest("quit", "", "0.5", getFile(shuffledCase(correctLines)));
		oneLineTest("quit", "", "0.5", getFile(shuffledCase(correctLines)));
		oneLineTest("quit", "", "0.5", getFile(shuffledCase(correctLines)));
	}

	private List<String> shuffledCase(List<String> lines) {
		List<String> shuffled = new LinkedList<>();
		for (String line : lines) {
			shuffled.add(shuffleCaseLine(line));
		}
		return shuffled;
	}

	private String shuffleCaseLine(String line) {
		StringBuilder builder = new StringBuilder();
		Random random = new Random();
		for (char c : line.toCharArray()) {
			builder.append((random.nextInt(2) != 0) ? Character.toUpperCase(c) : Character.toLowerCase(c));
		}
		return builder.toString();
	}

}
