package test;

import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

/**
 * Contains helper methods to generate input for tests.
 *
 * @author Joshua Gleitze
 * @version 1.0
 * @since 31.01.2015
 */
public class Input {
	private static HashMap<String, String[]> filesMap = new HashMap<>();
	private static HashMap<String[], String> reverseFileMap = new HashMap<>();

	/**
	 * This class is not meant to be instantiated.
	 */
	private Input() {
	}

	/**
	 * Returns a path to a file containing the lines given in {@code lines}. Creates the file if it was not created
	 * before.
	 * 
	 * @param lines
	 *            The lines to print in the file.
	 * @return path to a file containing {@code lines}
	 */
	public static String getFile(String... lines) {
		String fileName;
		if (!Input.reverseFileMap.containsKey(lines)) {
			fileName = UUID.randomUUID().toString() + ".txt";
			File file = new File(fileName);
			BufferedWriter outputWriter = null;
			try {
				outputWriter = new BufferedWriter(new FileWriter(file));
				for (String line : lines) {
					outputWriter.write(line);
					outputWriter.newLine();
				}
				outputWriter.flush();
				outputWriter.close();
				file.deleteOnExit();
			} catch (IOException e) {
				fail("The test was unable to create a test file. That's a shame!");
			}

			Input.filesMap.put(fileName, lines);
			Input.reverseFileMap.put(lines, fileName);
		} else {
			fileName = Input.reverseFileMap.get(lines);
		}
		return fileName;
	}

	/**
	 * Returns a path to a file containing the lines given in {@code lines}. Creates the file if it was not created
	 * before.
	 * 
	 * @param lines
	 *            The lines to print in the file.
	 * @return path to a file containing {@code lines}
	 */
	public static String getFile(Collection<String> lines) {
		String[] linesArray = new String[lines.size()];
		Iterator<String> iterator = lines.iterator();
		for (int i = 0; iterator.hasNext(); i++) {
			linesArray[i] = iterator.next();
		}
		return getFile(linesArray);
	}

	/**
	 * A message giving information about the input file used in a test.
	 * 
	 * @param filePath
	 *            The command line arguments the main method was called with during the test. The file message will read
	 *            the file name in the second argument and output the contents of the file its pointing to.
	 * @return A text representing the input file
	 */
	public static String fileMessage(String filePath) {
		String result = "";
		if (filesMap.containsKey(filePath)) {
			result = "\n with the following input file:\n\n" + arrayToLines(filesMap.get(filePath)) + "\n\n";
		}
		return result;
	}

	public static boolean isFile(String fileName) {
		return filesMap.containsKey(fileName);
	}

	/**
	 * Converts an Array of Strings into a String where each array element is represented as one line.
	 * 
	 * @param lines
	 *            The array to process
	 * @return the array as lines.
	 */
	public static String arrayToLines(String[] lines) {
		String result = "";
		for (String line : lines) {
			if (result != "") {
				result += "\n";
			}
			result += line;
		}
		return result;
	}

}
