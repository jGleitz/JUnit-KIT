package final1.subtests;

import static org.junit.Assert.fail;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;

/**
 * Starts the program with several invalid input files without performing any actions. Checks if the program outputs
 * error messages.
 * 
 * @author Joshua Gleitze
 * @author Martin Loeper
 * @author Roman Langrehr
 * @version 1.1
 */
public class InvalidInputFileTest extends RecommendationSubtest {
	private String[] input;

	public InvalidInputFileTest() {
		setExpectedSystemStatus(SystemExitStatus.EXACTLY.status(1));
	}

	@Test
	public void incomplete() {
		fail("This test is still in the development state and therefore incomplete!");
	}

	/**
	 * Asserts that the program detects syntactical errors
	 */
	@Test
	public void syntaxErrorsTest() {
		input = new String[] {
			"CentOS5 id= 12) contains operatingsystems"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"CentOS5 (id= 12) likes operatingsystems"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"CentOS5 id=12 contains operatingsystems"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"CentOS5(id=12) contains operatingsystems"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"CentOS5 (id=12)contains operatingsystems"
		};
		errorTest("quit", Input.getFile(input));
	}

	/**
	 * Tests invalid product ids.
	 */
	@Test
	public void invalidProductIds() {
		input = new String[] {
			"CentOS5 (id=1a2) contains operatingsystems"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"CentOS5 (id=-1) contains operatingsystems"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"CentOS5 (id=1 2) contains operatingsystems"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"CentOS5 (id=efj) contains operatingsystems"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"CentOS5 (id=999999999999999999999999999999999999999999999999999999999999999999999999999999999) contains operatingsystems"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"CentOS5 (id=999999999999999999999999999999999999999999999999999999999999999999999999999999999) contained-in operatingsystems"
		};
		errorTest("quit", Input.getFile(input));

		// test too big number after the product has been mentioned with a valid ID
		input = new String[] {
				"CentOS5 (id=2) contained-in operatingsystems",
				"CentOS5 (id=999999999999999999999999999999999999999999999999999999999999999999999999999999999) part-of somethingelse(id=3)"
		};
		errorTest("quit", Input.getFile(input));
	}

	/**
	 * Asserts that the tested class prints error messages for relations that get passed the wrong shop element type.
	 */
	@Test
	public void worngRelationArgumentTest() {
		input = new String[] {
			"CentOS5 (id= 12) contains operatingsystems"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"operatingsystems part-of CentOS5 (id = 10)"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"operatingsystems contained-in CentOS5 (id = 10)"
		};
		errorTest("quit", Input.getFile(input));
	}

	/**
	 * Asserts that the tested class prints an error message for input files that do not directly match product IDs and
	 * names.
	 */
	@Test
	public void nameIDMissmatchTest() {
		input = new String[] {
				"CentOS5 (id = 5) contained-in operatingsystems",
				"CentOS5 (id = 6) contained-in Cent"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
				"CentOS5 (id = 5) contained-in operatingsystems",
				"CentOS5Alt (id = 5) contained-in Cent"
		};
		errorTest("quit", Input.getFile(input));
	}

	/**
	 * Asserts that the tested class prints an error message if the same name is used for two shop elements.
	 */
	@Test
	public void nameTypeMissmatchTest() {
		input = new String[] {
				"CentOS5 (id = 5) contained-in operatingsystems",
				"CentOS5 contained-in operatingsystems"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
				"CentOS5 (id = 5) contained-in operatingsystems",
				"Centos5 contained-in operatingsystems"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
				"CentOS5 contained-in operatingsystems",
				"CentOS5 (id = 5) contained-in operatingsystems"
		};
		errorTest("quit", Input.getFile(input));
	}

	/**
	 * Asserts that the tested class detects and prints an error message for input files that form circles in the
	 * constructed graph. This method tests with short input files but covers different syntactical constructs,
	 * including lines relating shop elements with themselves.
	 */
	@Test
	public void basicCircleTest() {
		input = new String[] {
			"operatingsystems contained-in operatingsystems"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"os (id=1) part-of os (id=1)"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"os contained-in os"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
				"writer (id=1) part-of office (id=2)",
				"office (id=2) part-of officeSuite (id=3)",
				"officeSuite (id=3) part-of WorkTools (id=4)",
				"worktools (id=4) part-of writer (id=1)"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
				"writer contained-in office",
				"office contained-in writer",
				"officeSuite contained-in worktools",
				"writer contains worktools"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
				"C (id=2) predecessor-of A (id=3)",
				"A (id=3) predecessor-of C (id=4)"
		};
		errorTest("quit", Input.getFile(input));
	}

	/**
	 * Asserts that the tested class detetects and prints an error message for input files that form circles in the
	 * constructed graph. This method tests with a longer input file and all relations.
	 */
	@Test
	public void circleTest() {
		for (int r = 0; r < 6; r++) {
			input = new String[] {
				relation(r, 1, 1)
			};
			errorTest("quit", Input.getFile(input));

			input = new String[] {
					relation(r, 1, 2),
					relation(r, 2, 3),
					relation(r, 3, 4),
					relation(r, 4, 5),
					relation(r, 5, 6),
					relation(r, 6, 7),
					relation(r, 7, 8),
					relation(r, 8, 1)
			};
			errorTest("quit", Input.getFile(input));

			// different order
			input = new String[] {
					relation(r, 1, 2),
					relation(r, 2, 3),
					relation(r, 3, 4),
					relation(r, 8, 1),
					relation(r, 4, 5),
					relation(r, 5, 6),
					relation(r, 6, 7),
					relation(r, 7, 8),
			};
			errorTest("quit", Input.getFile(input));

			input = new String[] {
					relation(r, 1, 2),
					relation(r, 2, 3),
					relation(r, 3, 4),
					relation(r, 4, 5),
					relation(r, 5, 6),
					relation(r, 6, 7),
					relation(r, 7, 8),
					reverse(r, 1, 8)
			};
			errorTest("quit", Input.getFile(input));

			// different order
			input = new String[] {
					relation(r, 4, 5),
					relation(r, 1, 2),
					relation(r, 2, 3),
					relation(r, 7, 8),
					relation(r, 3, 4),
					reverse(r, 1, 8),
					relation(r, 5, 6),
					relation(r, 6, 7),
			};
			errorTest("quit", Input.getFile(input));
		}
	}

	/**
	 * Asserts that the tested class prints an error message for empty lines, which are syntactical errors.
	 */
	@Test
	public void emptyLinesTest() {
		input = new String[] {
				"CentOS5 ( id= 105) contained-in operatingSystem",
				"centOS6 ( id = 106) contained-in OperatingSystem",
				"operatingSystem contains centos7 ( id = 107 )",
				"operatingsystem contained-in Software",
				"",
				"CentOS7 (id=107) successor-of centos6(id=106)",
				"CentOS5 (id=105) predecessor-of centos6(id=106)"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
				"",
				"CentOS5 ( id= 105) contained-in operatingSystem",
				"centOS6 ( id = 106) contained-in OperatingSystem",
				"operatingSystem contains centos7 ( id = 107 )",
				"operatingsystem contained-in Software",
				"CentOS7 (id=107) successor-of centos6(id=106)",
				"CentOS5 (id=105) predecessor-of centos6(id=106)"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
				"CentOS5 ( id= 105) contained-in operatingSystem",
				"centOS6 ( id = 106) contained-in OperatingSystem",
				"operatingSystem contains centos7 ( id = 107 )",
				"operatingsystem contained-in Software",
				"CentOS7 (id=107) successor-of centos6(id=106)",
				"CentOS5 (id=105) predecessor-of centos6(id=106)",
				""
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			""
		};
		errorTest("quit", Input.getFile(input));

		// totally empty input file is not valid: "Die Datenbestand-Datei besteht aus einer oder mehreren Zeilen."
		input = new String[] {};
		errorTest("quit", Input.getFile(input));
	}

	/**
	 * Asserts that the tested class checks for the right case in the input file.
	 */
	@Test
	public void caseSensitivityTest() {
		input = new String[] {
			"operatingSystem Contains centos7 ( id = 107 )"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			" centos6 ( id = 106 ) predecessor-Of centos7 ( id = 107 )"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"operatingSystem contains centos7 ( iD = 107 )"
		};
		errorTest("quit", Input.getFile(input));
	}

	/**
	 * Asserts that the tested class outputs an error message if an input line contains more parts than allowed.
	 */
	@Test
	public void invalidArgumentCountTest() {
		input = new String[] {
			"operatingSystem con tains centos7 (id = 107 )"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"operatingSystem contains centos7 (id=107) part-of centos8 (id=108)"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"operatingSystem contains"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"contains centos7 ( id = 107 )"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"operatingSystem test"
		};
		errorTest("quit", Input.getFile(input));
	}

	/**
	 * Asserts that the tested class detects illegal characters in the input file.
	 */
	@Test
	public void invalidSymbolTest() {
		input = new String[] {
			"operatingSystem contains centos7 (i d = 107 )"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"operäitingSystem contains centos7 ( id = 107 )"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"operaitingSystem contains centös7 ( id = 107 )"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"operatingSystem contains cento-s7 ( id = 107 )"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"operating-System contains centos7 ( id = 107 )"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"operating System contains centos7 ( id = 107 )"
		};
		errorTest("quit", Input.getFile(input));

		input = new String[] {
			"operatingSystem contains cent os7 ( id = 107 )"
		};
		errorTest("quit", Input.getFile(input));
	}

}
