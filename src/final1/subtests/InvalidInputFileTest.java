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
            "os contained-in os"
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

        input = new String[] {
                "C (id=2) predecessor-of A (id=3)",
                "A (id=3) predecessor-of C (id=4)"
        };
        exitTest("quit", Input.getFile(input));
    }
    
    @Test 
    public void circleTest() {
        String[][] relations = new String[][] {
                {"contains", "contained-in"},
                {"successor-of", "predecessor-of"},
                {"has-part", "part-of"}
        };
        
        for (String[] relation : relations) {
            for (int i = 0; i < 2; i++) {
                input = new String[] {
                    "A " + relation[i] + " A"
                };
                exitTest("quit", Input.getFile(input));
                
                input = new String[] {
                        "A (id=1) " + relation[i] + " B (id=2)",
                        "B (id=2) " + relation[i] + " C (id=3)",
                        "C (id=3) " + relation[i] + " D (id=4)",
                        "D (id=4) " + relation[i] + " E (id=5)",
                        "E (id=5) " + relation[i] + " F (id=6)",
                        "F (id=6) " + relation[i] + " G (id=7)",
                        "G (id=7) " + relation[i] + " H (id=8)",
                        "H (id=8) " + relation[i] + " A (id=1)"
                };
                exitTest("quit", Input.getFile(input));
                
                // different order
                input = new String[] {
                        "A (id=1) " + relation[i] + " B (id=2)",
                        "B (id=2) " + relation[i] + " C (id=3)",
                        "C (id=3) " + relation[i] + " D (id=4)",
                        "H (id=8) " + relation[i] + " A (id=1)",
                        "D (id=4) " + relation[i] + " E (id=5)",
                        "E (id=5) " + relation[i] + " F (id=6)",
                        "F (id=6) " + relation[i] + " G (id=7)",
                        "G (id=7) " + relation[i] + " H (id=8)"
                };
                exitTest("quit", Input.getFile(input));
                
                input = new String[] {
                        "A (id=1) " + relation[i] + " B (id=2)",
                        "B (id=2) " + relation[i] + " C (id=3)",
                        "C (id=3) " + relation[i] + " D (id=4)",
                        "D (id=4) " + relation[i] + " E (id=5)",
                        "E (id=5) " + relation[i] + " F (id=6)",
                        "F (id=6) " + relation[i] + " G (id=7)",
                        "G (id=7) " + relation[i] + " H (id=8)",
                        "H (id=8) " + relation[i % 2] + " A (id=1)"
                };
                exitTest("quit", Input.getFile(input));
                
                // different order
                input = new String[] {
                        "A (id=1) " + relation[i] + " B (id=2)",
                        "B (id=2) " + relation[i] + " C (id=3)",
                        "C (id=3) " + relation[i] + " D (id=4)",
                        "H (id=8) " + relation[i % 2] + " A (id=1)",
                        "D (id=4) " + relation[i] + " E (id=5)",
                        "E (id=5) " + relation[i] + " F (id=6)",
                        "F (id=6) " + relation[i] + " G (id=7)",
                        "G (id=7) " + relation[i] + " H (id=8)"
                };
                exitTest("quit", Input.getFile(input));
            }
        }
    }
    
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
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "",
                "CentOS5 ( id= 105) contained-in operatingSystem",
                "centOS6 ( id = 106) contained-in OperatingSystem",
                "operatingSystem contains centos7 ( id = 107 )",
                "operatingsystem contained-in Software",
                "CentOS7 (id=107) successor-of centos6(id=106)",
                "CentOS5 (id=105) predecessor-of centos6(id=106)"
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "CentOS5 ( id= 105) contained-in operatingSystem",
                "centOS6 ( id = 106) contained-in OperatingSystem",
                "operatingSystem contains centos7 ( id = 107 )",
                "operatingsystem contained-in Software",
                "CentOS7 (id=107) successor-of centos6(id=106)",
                "CentOS5 (id=105) predecessor-of centos6(id=106)",
                ""
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                ""
        };
        exitTest("quit", Input.getFile(input));
    }
    
    @Test
    public void caseSensitivityTest() {
        input = new String[] {
                "operatingSystem Contains centos7 ( id = 107 )"
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "operatingSystem contains centos7 ( iD = 107 )"
        };
        exitTest("quit", Input.getFile(input));
    }
    
    @Test
    public void invalidArgumentCountTest() {
        input = new String[] {
                "operatingSystem con tains centos7 (id = 107 )"
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "operatingSystem contains centos7 (id=107) part-of centos8 (id=108)"
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "operatingSystem contains"
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "contains centos7 ( id = 107 )"
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "operatingSystem test"
        };
        exitTest("quit", Input.getFile(input));
    }
    
    @Test
    public void invalidSymbolTest() {
        input = new String[] {
                "operatingSystem contains centos7 (i d = 107 )"
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "operäitingSystem contains centos7 ( id = 107 )"
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "operaitingSystem contains centös7 ( id = 107 )"
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "operatingSystem contains cento-s7 ( id = 107 )"
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "operating-System contains centos7 ( id = 107 )"
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "operating System contains centos7 ( id = 107 )"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
                "operatingSystem contains cent os7 ( id = 107 )"
        };
        exitTest("quit", Input.getFile(input));
    }
    
}
