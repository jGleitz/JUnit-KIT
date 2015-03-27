package final2.subtests;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.runs.Run;

/**
 * Checks the {@code move} command.
 * 
 * @author Joshua Gleitze
 */
public class MoveTest extends LangtonSubtest {

	public MoveTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}

	/**
	 * Asserts that {@code move} works on a very simple, easy to understand example: The rule is set to
	 * {@code 90-90-90-90-90} and we have only one ant on this 2x2 board:
	 * 
	 * <pre>
	 * 0a
	 * 00
	 * </pre>
	 * 
	 * The ant should run through the board clockwise, changing all colours to {@code 3} in the first four steps. In the
	 * next four steps, the ant should run clockwise once again, changing all colours back to {@code 1} and leaving the
	 * board in its initial state.
	 */
	@Test
	public void simpleMoveTest() {
		String[] inputFile = {
				"0a",
				"00"
		};
		runs = new Run[] {
				move(0),
				checkPitch(inputFile),
				move(1),
				checkPitch(new String[] {
						"00",
						"0a"
				}),
				move(1),
				checkPitch(new String[] {
						"00",
						"a3"
				}),
				move(1),
				checkPitch(new String[] {
						"a0",
						"33"
				}),
				move(1),
				checkPitch(new String[] {
						"3a",
						"33"
				}),
				move(4),
				checkPitch(inputFile),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), "rule=90-90-90-90-90");
	}

	/**
	 * Takes a board that repeats in its movement and asserts that the results stay the same for a high move count.
	 */
	@Test
	public void testConsitensy() {
		String[] inputFile = {
				"0a0000",
				"000000",
				"000l00",
				"000000",
				"00000z",
				"000000"
		};
		String[][] standardStates = {
				{
						"0a0000",
						"000000"
				},
				{
						"000000",
						"0a0000"
				},
				{
						"000000",
						"a30000"
				},
				{
						"a00000",
						"330000"
				},
				{
						"3a0000",
						"330000"
				},
				{
						"330000",
						"3a0000"
				},
				{
						"330000",
						"a00000"
				},
				{
						"a30000",
						"000000"
				},

		};
		String[][] athleticStates = {
				{
						"000l00",
						"000000"
				},
				{
						"003l00",
						"003300"
				},
		};
		String[][] lazyStates = {
				{
						"00000z",
						"000000"
				},
				{
						"000000",
						"00000z"
				},
				{
						"000000",
						"0000z3"
				},
				{
						"0000z0",
						"000033"
				},
				{
						"00003z",
						"000033"
				},
				{
						"000033",
						"00003z"
				},
				{
						"000033",
						"0000z0"
				},
				{
						"0000z3",
						"000000"
				},
		};
		List<Run> runList = new LinkedList<>();
		for (int i = 1; i < 80; i++) {
			runList.add(move(1));
			String[] pitch = merge(standardStates[i % 8], athleticStates[i % 2], lazyStates[(((i - 1) / 4) + 1) % 8]);
			runList.add(checkPitch(pitch));
		}
		runList.add(quit());
		sessionTest(runArray(runList), Input.getFile(inputFile), "speedup=4", "rule=90-90-90-90-90");

		runList = new LinkedList<>();
		for (int i = 47; i < 6000; i += 47) {
			runList.add(move(47));
			String[] pitch = merge(standardStates[i % 8], athleticStates[i % 2], lazyStates[(((i - 1) / 4) + 1) % 8]);
			runList.add(checkPitch(pitch));
		}
		runList.add(quit());
		sessionTest(runArray(runList), Input.getFile(inputFile), "speedup=4", "rule=90-90-90-90-90");
	}

	/**
	 * Asserts that {@code move} works on a test file with all ant and cell types. Let's take a look at what we expect
	 * to happen:
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
	 * CDJS | 0000 | ↑↑↑↑
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
	 * 0*3s | 0*00 |    → 
	 * c330 | 3330 | ←    
	 * ------------------
	 * b30i | 3303 | →  ↓  
	 * 004r | 0041 |    ↘ 
	 * 0*3s | 0*00 |    → 
	 * c330 | 3330 | ←    
	 * ------------------
	 * b30i | 3303 | →  ↓  
	 * 004r | 0041 |    ↘ 
	 * 0*3s | 0*00 |    → 
	 * c330 | 3330 | ←
	 * </pre>
	 * 
	 * Another {@code move 1}:
	 * 
	 * <pre>
	 * 3b0i | 3003 |  ↓ ↓
	 * 004r | 0041 |    ↘
	 * 0*3s | 0*00 |    →
	 * 3330 | 3330 |     
	 * ------------------
	 * 3b30 | 3030 |  ↓    
	 * 004r | 0041 |    ↘  
	 * 0*3s | 0*00 |    →  
	 * 3330 | 3330 |       
	 * ------------------
	 * 3b30 | 3030 |  ↓   
	 * 004r | 0041 |    ↘ 
	 * 0*3s | 0*00 |    → 
	 * 3330 | 3330 |
	 * </pre>
	 * 
	 * Another {@code move 1}:
	 * 
	 * <pre>
	 * 3030 | 3030 |      
	 * 0b4r | 0341 |  ← ↘ 
	 * 0*3s | 0*00 |    → 
	 * 3330 | 3330 |
	 * ------------------
	 * 3030 | 3030 |      
	 * 0b4r | 0341 |  ← ↘ 
	 * 0*3s | 0*00 |    → 
	 * 3330 | 3330 |      
	 * ------------------
	 * 3030 | 3030 |      
	 * 0b41 | 0341 |  ←    
	 * 0*30 | 0*00 |       
	 * 3330 | 3330 |
	 * </pre>
	 */
	@Test
	public void allTypesMoveTest() {
		String[] afterFirstRun = new String[] {
				"0000",
				"ab4r",
				"c*is",
				"0d00"
		};
		String[] afterSecondRun = new String[] {
				"03i0",
				"b04r",
				"c*3s",
				"03d0"
		};
		String[] afterThirdRun = new String[] {
				"b30i",
				"004r",
				"0*3s",
				"c330"
		};
		String[] afterFourthRun = new String[] {
				"3b30",
				"004r",
				"0*3s",
				"3330"
		};
		String[] afterFifthRun = new String[] {
				"3030",
				"0b41",
				"0*30",
				"3330"
		};
		runs = new Run[] {
				move(1),
				checkPitch(afterFirstRun),
				move(1),
				checkPitch(afterSecondRun),
				move(1),
				checkPitch(afterThirdRun),
				move(1),
				checkPitch(afterFourthRun),
				move(1),
				checkPitch(afterFifthRun),
				quit()
		};
		sessionTest(runs, Input.getFile(ALL_TYPES_BOARD), "rule=90-90-315-90-270", "speedup=4");
	};

	/**
	 * Asserts that the tested class terminates without any output after all ants have left the field.
	 */
	@Test
	public void terminateAfterAllHaveLeftTest() {
		inputFile = new String[] {
			"a"
		};
		runs = new Run[] {
			move(1)
		};
		sessionTest(runs, Input.getFile(inputFile), "rule=90-90-90-270-90");
		inputFile = new String[] {
				"0a0l0c",
				"303030",
				"030303",
				"303030",
				"030303"
		};
		runs = new Run[] {
				move(1),
				move(1),
				move(1),
				move(1),
				move(1),
				move(1),
				move(1),
				move(1),
				move(1),
		};
		sessionTest(runs, Input.getFile(inputFile), "rule=90-90-90-270-90", "speedup=1");
		runs = new Run[] {
			move(9)
		};
		sessionTest(runs, Input.getFile(inputFile), "rule=90-90-90-270-90", "speedup=1");
		inputFile = new String[] {
				"0i0l0k",
				"303030",
				"030303",
				"303030",
				"030303"
		};
		runs = new Run[] {
			move(1)
		};
		sessionTest(runs, Input.getFile(inputFile), "rule=90-90-90-270-90", "speedup=9");
	}

	/**
	 * Asserts that ants that have left the board are no longer recognized as obstacles.
	 */
	@Test
	public void noZombieAntsTest() {
		inputFile = new String[] {
				"00B400",
				"000C0d",
				"000000"
		};
		runs = new Run[] {
				move(1),
				checkPitch(new String[] {
						"000c00",
						"000000",
						"00000d"
				}),
				move(1),
				checkPitch(new String[] {
						"000400",
						"000000",
						"0000d3"
				}),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), "rule=90-90-90-270-315");
	}

	private static Run[] runArray(List<Run> runs) {
		return runs.toArray(new Run[runs.size()]);
	}

	private static String[] merge(String[]... stringArrays) {
		int size = 0;
		for (String[] a : stringArrays) {
			size += a.length;
		}
		String[] result = new String[size];
		int i = 0;
		for (String[] a : stringArrays) {
			for (int j = 0; j < a.length; j++) {
				result[i] = a[j];
				i++;
			}
		}
		return result;
	}
}
