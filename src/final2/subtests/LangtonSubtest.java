package final2.subtests;

import java.util.List;

import org.hamcrest.Matcher;

import test.Input;
import test.InteractiveConsoleTest;
import test.runs.LineRun;
import test.runs.NoOutputRun;
import test.runs.Run;

/**
 * Base class for implementing subtest for the programming lecture's second final task. Should contain some convenience
 * methods and fields.
 * 
 * @author Joshua Gleitze
 * @author Martin Loeper
 * @author Christian Hilden
 */
public abstract class LangtonSubtest extends InteractiveConsoleTest {

	/**
	 * The first input file provided on the task sheet (page 6)
	 */
	protected static final String[] TASK_SHEET_INPUT_FILE_1 = new String[] {
			"000",
			"000",
			"0F0"
	};

	/**
	 * The second input file provided on the task sheet (page 7).
	 */
	protected static final String[] TASK_SHEET_INPUT_FILE_2 = new String[] {
			"1*0",
			"3e4",
			"12*"
	};

	/**
	 * The first board.txt Praktomat test file. ("Fundamental tests with ordinary ant")
	 */
	protected static final String[] PUBLIC_PRAKTOMAT_TEST_FILE_1 = new String[] {
			"0000",
			"0*00",
			"0A00",
			"0000"
	};

	/**
	 * The second board.txt Praktomat test file. ("Sporty ant movement")
	 */
	protected static final String[] PUBLIC_PRAKTOMAT_TEST_FILE_2 = new String[] {
			"0000",
			"0*00",
			"0I00",
			"0000"
	};

	/**
	 * The third board.txt Praktomat test file. ("Lazy ant movement")
	 */
	protected static final String[] PUBLIC_PRAKTOMAT_TEST_FILE_3 = new String[] {
			"0000",
			"0*00",
			"0R00",
			"0000"
	};

	/**
	 * The fourth board.txt Praktomat test file. ("Ordinary ant movement ")
	 */
	protected static final String[] PUBLIC_PRAKTOMAT_TEST_FILE_4 = new String[] {
			"0000",
			"0000",
			"A000",
			"0000"
	};

	/**
	 * A test file containing all types of ants, colours and cells. Let's take a look at what we expect to happen:
	 * <p>
	 * We start with this board:
	 * 
	 * 
	 * <pre>
	 * print|colour|direction
	 * 
	 * abir | 0000 | ↓↓↓↓
	 * 0041 | 0041 |     
	 * 0*00 | 0*00 |     
	 * cdjs | 0000 | ↑↑↑↑
	 * </pre>
	 * 
	 * We then run {@code move 1}. This should first move the standard ants. The athletic ants are next. {@code speedup}
	 * is {@code 4}, {@code rule} is {@code 90-90-315-90-270} so they perform four steps. Last, the lazy ants move. We
	 * get the last figure as first result:
	 * 
	 * <pre>
	 * 00ir | 0000 |   ↓↓ 
	 * ab41 | 3341 | ←←  
	 * c*00 | 3*00 | →   
	 * 0djs | 0300 |  →↑↑
	 * ------------------
	 * 000r | 0000 |    ↓ 
	 * ab42 | 3342 | ←←   
	 * c*i3 | 3*33 | → ↑  
	 * 0d0s | 0300 |  → ↑ 
	 * ------------------
	 * 0000 | 0000 |     
	 * ab4r | 3341 | ←← ↘
	 * c*is | 3*30 | → ↑→
	 * 0d00 | 0300 |  →
	 * </pre>
	 * 
	 * Next {@code move 1}:
	 * 
	 * <pre>
	 * 0000 | 0000 |    
	 * b34r | 0341 | ↑  ↘  
	 * c*is | 0*30 | ↓ ↑→
	 * 03d0 | 0330 |   ↓ 
	 * ------------------
	 * 03i0 | 0330 |   ↓      
	 * b04r | 0041 | ↑  ↘ 
	 * c*3s | 0*30 | ↓  → 
	 * 03d0 | 0330 |   ↓  
	 * ------------------
	 * 03i0 | 0330 |   ↓  
	 * b04r | 0041 | ↑  ↘ 
	 * c*3s | 0*30 | ↓  → 
	 * 03d0 | 0330 |   ↓
	 * </pre>
	 * 
	 * Another {@code move 1}:
	 * 
	 * <pre>
	 * b3i0 | 3330 | → ↓  
	 * 004r | 0041 |    ↘ 
	 * 0*3s | 0*30 |    → 
	 * c330 | 3330 | ←    
	 * ------------------
	 * b30i | 3303 | →  ↓  
	 * 004r | 0041 |    ↘ 
	 * 0*3s | 0*30 |    → 
	 * c330 | 3330 | ←    
	 * ------------------
	 * b30i | 3303 | →  ↓  
	 * 004r | 0041 |    ↘ 
	 * 0*3s | 0*30 |    → 
	 * c330 | 3330 | ←
	 * </pre>
	 * 
	 * Another {@code move 1}:
	 * 
	 * <pre>
	 * 3b0i | 3003 |  ↓ ↓
	 * 004r | 0041 |    ↘
	 * 0*3s | 0*30 |    →
	 * 3330 | 3330 |     
	 * ------------------
	 * 3b30 | 3030 |  ↓    
	 * 004r | 0041 |    ↘  
	 * 0*3s | 0*30 |    →  
	 * 3330 | 3330 |       
	 * ------------------
	 * 3b30 | 3030 |  ↓   
	 * 004r | 0041 |    ↘ 
	 * 0*3s | 0*30 |    → 
	 * 3330 | 3330 |
	 * </pre>
	 * 
	 * Another {@code move 1}:
	 * 
	 * <pre>
	 * 3030 | 3030 |      
	 * 0b4r | 0341 |  ← ↘ 
	 * 0*3s | 0*30 |    → 
	 * 3330 | 3330 |
	 * ------------------
	 * 3030 | 3030 |      
	 * 0b4r | 0341 |  ← ↘ 
	 * 0*3s | 0*30 |    → 
	 * 3330 | 3330 |      
	 * ------------------
	 * 3030 | 3030 |      
	 * 0b41 | 0341 |  ←    
	 * 0*30 | 0*30 |       
	 * 3330 | 3330 |
	 * </pre>
	 */
	protected static final String[] ALL_TYPES_BOARD = new String[] {
			"abir",
			"0041",
			"0*00",
			"CDJS"
	};

	/**
	 * The expected pitches for {@link #ALL_TYPES_BOARD} for the first five moves, as described for
	 * {@link #ALL_TYPES_BOARD}.
	 */
	protected static final String[][] ALL_TYPES_PITCHES = new String[][] {
			{
					"0000",
					"ab4r",
					"c*is",
					"0d00"
			},
			{
					"03i0",
					"b04r",
					"c*3s",
					"03d0"
			},
			{
					"b30i",
					"004r",
					"0*3s",
					"c330"
			},
			{
					"3b30",
					"004r",
					"0*3s",
					"3330"
			},
			{
					"3030",
					"0b41",
					"0*30",
					"3330"
			}
	};

	/**
	 * The rule that produces the behaviour specified for {@link #ALL_TYPES_BOARD}.
	 */
	protected static final String ALL_TYPES_RULE = "rule=90-90-315-90-270";
	/**
	 * The speedup that produces the behaviour specified for {@link #ALL_TYPES_BOARD}.
	 */
	protected static final String ALL_TYPES_SPEEDUP = "speedup=4";

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.InteractiveConsoleTest#consoleMessage(java.lang.String[], java.lang.String[])
	 */
	@Override
	protected String consoleMessage(String[] commands, String[] commandLineArguments) {
		String result = "";
		String fileMessage = (commandLineArguments.length > 0) ? Input.fileMessage(commandLineArguments[0]) : "";
		result += "We ran a session on your interactive console" + fileMessage + " running the commands \n\n"
				+ joinAsNumberedLines(commands) + "\n\nbut got unexpected output:\n";
		return result;
	}

	/**
	 * Checks if the given expected pitch matches the program output.
	 * 
	 * @param pExpectedPitch
	 *            the lines of the expected pitch
	 * @return the run which checks the pitch
	 */
	protected Run checkPitch(String[] pExpectedPitch) {
		List<Matcher<String>> matchers = joinAsIsMatchers(pExpectedPitch);
		return new LineRun("print", matchers);
	}

	/**
	 * Returns the given pitch description in lowercase as expected by the print command.
	 * 
	 * @param pPitchLines
	 *            the pitch description
	 * @return lowercase pitch description
	 */
	protected String[] pitchToLowercase(String[] pPitchLines) {
		String[] out = new String[pPitchLines.length];
		for (int i = 0; i < pPitchLines.length; i++) {
			out[i] = pPitchLines[i].toLowerCase();
		}
		return out;
	}

	/**
	 * @param count
	 *            How many moves the tested class shall perform.
	 * @return A run for {@code move count}.
	 */
	protected Run move(int count) {
		return new NoOutputRun("move " + count);
	}
}
