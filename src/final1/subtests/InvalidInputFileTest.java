package final1.subtests;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.TestObject;

/**
 * Starts the program with several valid input files without performing any actions and checks if the program is able to
 * read in the files.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class InvalidInputFileTest extends RecommendationSubtest {
    private String[] input;

    @Before
    public void defaultSystemExitStatus() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
    }

    @Test
    public void incomplete() {
        fail("This test is still in the development state and therefore incomplete!");
    }

    @Test
    public void syntaxErrorsTest() {
        input = new String[] {
            "CentOS5 id= 12) contains operatingsystems"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
            "CentOS5 (id= 12) likes operatingsystems"
        };
        exitTest("quit", Input.getFile(input));
    }

    @Test
    public void semanticErrorsTest() {
        input = new String[] {
            "CentOS5 (id= 12) contains operatingsystems"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
            "operatingsystems part-of CentOS5 (id = 10)"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
            "operatingsystems contained-in CentOS5 (id = 10)"
        };
        exitTest("quit", Input.getFile(input));
    }

    @Test
    public void nameIDMissmatchTest() {
        input = new String[] {
                "CentOS5 (id = 5) contained-in operatingsystems",
                "CentOS5 (id = 6) contained-in Cent"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
                "CentOS5 (id = 5) contained-in operatingsystems",
                "CentOS5Alt (id = 5) contained-in Cent"
        };
        exitTest("quit", Input.getFile(input));
    }

    @Test
    public void nameTypeMissmatchTest() {
        input = new String[] {
                "CentOS5 (id = 5) contained-in operatingsystems",
                "CentOS5 contained-in operatingsystems"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
                "CentOS5 contained-in operatingsystems",
                "CentOS5 (id = 5) contained-in operatingsystems"
        };
        exitTest("quit", Input.getFile(input));
    }

    @Test
    public void basicCircleTest() {
        input = new String[] {
            "operatingsystems contained-in operatingsystems"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
            "os (id=1) part-of os (id=1)"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
                "writer (id=1) part-of office (id=2)",
                "office (id=2) part-of officeSuite (id=3)",
                "officeSuite (id=3) part-of WorkTools (id=4)",
                "worktools (id=4) part-of writer (id=1)"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
                "writer contained-in office",
                "office contained-in writer",
                "officeSuite contained-in worktools",
                "writer contains worktools"
        };
        exitTest("quit", Input.getFile(input));
    }

    @Test
    public void containsCircleTest() {
        input = new String[] {
            "A contains A"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
                "A contains B",
                "B contains C",
                "C contains D",
                "D contains E",
                "E contains F",
                "F contains G",
                "G contains H",
                "H contains A"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
                "A contains B",
                "B contains C",
                "C contains D",
                "D contains E",
                "E contains F",
                "F contains G",
                "G contains H",
                "A contained-in H"
        };
        exitTest("quit", Input.getFile(input));
    }

    @Test
    public void containedInCircleTest() {
        input = new String[] {
            "A contained-in A"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
                "A contained-in B",
                "B contained-in C",
                "C contained-in D",
                "D contained-in E",
                "E contained-in F",
                "F contained-in G",
                "G contained-in H",
                "H contained-in A"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
                "A contained-in B",
                "B contained-in C",
                "C contained-in D",
                "D contained-in E",
                "E contained-in F",
                "F contained-in G",
                "G contained-in H",
                "A contains H"
        };
        exitTest("quit", Input.getFile(input));
    }

    @Test
    public void successorOfCircleTest() {
        input = new String[] {
            "A (id=1) successor-of A (id=1)"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
                "A (id=1) successor-of B (id=2)",
                "B (id=2) successor-of C (id=3)",
                "C (id=3) successor-of D (id=4)",
                "D (id=4) successor-of E (id=5)",
                "E (id=5) successor-of F (id=6)",
                "F (id=6) successor-of G (id=7)",
                "G (id=7) successor-of H (id=8)",
                "H (id=8) successor-of A (id=1)"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
                "A (id=1) successor-of B (id=2)",
                "B (id=2) successor-of C (id=3)",
                "C (id=3) successor-of D (id=4)",
                "D (id=4) successor-of E (id=5)",
                "E (id=5) successor-of F (id=6)",
                "F (id=6) successor-of G (id=7)",
                "G (id=7) successor-of H (id=8)",
                "A (id=1) predecessor-of H (id=8)"
        };
        exitTest("quit", Input.getFile(input));
    }

    @Test
    public void predecessorOfCircleTest() {
        input = new String[] {
            "A (id=1) predecessor-of A (id=1)"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
                "A (id=1) predecessor-of B (id=2)",
                "B (id=2) predecessor-of C (id=3)",
                "C (id=3) predecessor-of D (id=4)",
                "D (id=4) predecessor-of E (id=5)",
                "E (id=5) predecessor-of F (id=6)",
                "F (id=6) predecessor-of G (id=7)",
                "G (id=7) predecessor-of H (id=8)",
                "H (id=8) predecessor-of A (id=1)"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
                "A (id=1) predecessor-of B (id=2)",
                "B (id=2) predecessor-of C (id=3)",
                "C (id=3) predecessor-of D (id=4)",
                "D (id=4) predecessor-of E (id=5)",
                "E (id=5) predecessor-of F (id=6)",
                "F (id=6) predecessor-of G (id=7)",
                "G (id=7) predecessor-of H (id=8)",
                "A (id=1) successor-of H (id=8)"
        };
        exitTest("quit", Input.getFile(input));
    }

    @Test
    public void hasPartCircleTest() {
        input = new String[] {
            "A (id=1) has-part A (id=1)"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
                "A (id=1) has-part B (id=2)",
                "B (id=2) has-part C (id=3)",
                "C (id=3) has-part D (id=4)",
                "D (id=4) has-part E (id=5)",
                "E (id=5) has-part F (id=6)",
                "F (id=6) has-part G (id=7)",
                "G (id=7) has-part H (id=8)",
                "H (id=8) has-part A (id=1)"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
                "A (id=1) has-part B (id=2)",
                "B (id=2) has-part C (id=3)",
                "C (id=3) has-part D (id=4)",
                "D (id=4) has-part E (id=5)",
                "E (id=5) has-part F (id=6)",
                "F (id=6) has-part G (id=7)",
                "G (id=7) has-part H (id=8)",
                "A (id=1) part-of H (id=8)"
        };
        exitTest("quit", Input.getFile(input));
    }

    @Test
    public void partOfCircleTest() {
        input = new String[] {
            "A (id=1) part-of A (id=1)"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
                "A (id=1) part-of B (id=2)",
                "B (id=2) part-of C (id=3)",
                "C (id=3) part-of D (id=4)",
                "D (id=4) part-of E (id=5)",
                "E (id=5) part-of F (id=6)",
                "F (id=6) part-of G (id=7)",
                "G (id=7) part-of H (id=8)",
                "H (id=8) part-of A (id=1)"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
                "A (id=1) part-of B (id=2)",
                "B (id=2) part-of C (id=3)",
                "C (id=3) part-of D (id=4)",
                "D (id=4) part-of E (id=5)",
                "E (id=5) part-of F (id=6)",
                "F (id=6) part-of G (id=7)",
                "G (id=7) part-of H (id=8)",
                "A (id=1) has-part H (id=8)"
        };
        exitTest("quit", Input.getFile(input));
    }

}
