package final2.subtests;

import org.junit.Test;

import test.Input;
import test.runs.Run;

/**
 * Checks the {@code move} command.
 * 
 * @author Joshua Gleitze
 */
public class MoveTest extends LangtonSubtest {

	/**
	 * Asserts that {@code move} works on a very simple, easy to understand example: The rule is set to
	 * {@code 90-90-90-90-90} and we have only one ant on this 2x2 board:
	 * 
	 * <code>
	 * <pre>
	 * 0a
	 * 00
	 * </pre>
	 * </code>
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
	 * Asserts that {@code move} works on a test file with all ant and cell types. Let's take a look at what we expect
	 * to happen:
	 * <p>
	 * We start with this board: <code>
	 * <pre>
	 * print|colour|direction
	 * 
	 * abir | 0000 | ↓↓↓↓
	 * 0041 | 0041 |     
	 * 0*00 | 0*00 |     
	 * CDJS | 0000 | ↑↑↑↑
	 * </pre>
	 * </code> We then run {@code move 1}. This should first move the standard ants. The athletic ants are next.
	 * {@code speedup} is {@code 4}, {@code rule} is {@code 90-90-315-90-270} so they perform four steps. Last, the lazy
	 * ants move. We get the last figure as first result: <code>
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
	 * </code> Next {@code move 1}:
	 * 
	 * <pre>
	 * <code>
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
	 * </code>
	 * </pre>
	 * 
	 * Another {@code move 1}: <code>
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
	 * </code>
	 * 
	 * Another {@code move 1}: <code>
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
	 * </code>
	 * 
	 * Another {@code move 1}: <code>
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
}
