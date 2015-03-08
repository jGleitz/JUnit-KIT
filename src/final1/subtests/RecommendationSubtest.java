package final1.subtests;

import test.InteractiveConsoleTest;

/**
 * Base class for implementing subtest for the programming lecture's first final task. Contains some convenience methods
 * and fields.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public abstract class RecommendationSubtest extends InteractiveConsoleTest {
    protected String[] TASK_SHEET_INPUT_FILE = new String[] {
            "CentOS5 ( id= 105) contained-in operatingSystem",
            "centOS6 ( id = 106) contained-in OperatingSystem",
            "operatingSystem contains centos7 ( id = 107 )",
            "operatingsystem contained-in Software",
            "CentOS7 (id=107) successor-of centos6(id=106)",
            "CentOS5 (id=105) predecessor-of centos6(id=106)",
            "writer (id=201) contained-in officesuite",
            "calc (id=202) contained-in officesuite",
            "impress (id=203) contained-in officesuite",
            "officesuite contained-in software",
            "LibreOffice (id=200) contained-in officesuite",
            "writer (id=201) part-of LibreOffice (id=200)",
            "calc (id=202) part-of libreoffice (id=200)",
            "libreoffice (id=200) has-part impress (id=203)"
    };
    
    protected String[] SYNTHAX_ERROR_1 = new String[] {
            "CentOS5 id= 12) contains operatingsystems"
    };
    
    protected String[] SYNTHAX_ERROR_2 = new String[] {
            "CentOS5 (id= 12) likes operatingsystems"
    };
    
    protected String[] SEMANTIC_ERROR_1 = new String[] {
            "CentOS5 (id= 12) contains operatingsystems"
    };
    
    protected String[] SEMANTIC_ERROR_2 = new String[] {
            "operatingsystems part-of CentOS5 (id = 10)"
    };
    
    protected String[] SEMANTIC_ERROR_3 = new String[] {
            "operatingsystems contained-in CentOS5 (id = 10)"
    };
    
    protected String[] NAME_ID_MISSMATCH_1 = new String[] {
            "CentOS5 (id = 5) contained-in operatingsystems",
            "CentOS5 (id = 6) contained-in Cent"
    };
    
    protected String[] NAME_ID_MISSMATCH_2 = new String[] {
            "CentOS5 (id = 5) contained-in operatingsystems",
            "CentOS5Alt (id = 5) contained-in Cent"
    };
    
    protected String[] NAME_TYPE_MISSMATCH_1 = new String[] {
            "CentOS5 (id = 5) contained-in operatingsystems",
            "CentOS5 contained-in operatingsystems"
    };
    
    protected String[] NAME_TYPE_MISSMATCH_2 = new String[] {
            "CentOS5 contained-in operatingsystems",
            "CentOS5 (id = 5) contained-in operatingsystems"
    };
    
    protected String[] CIRCLE_1 = new String[] {
            "operatingsystems contained-in operatingsystems"
    };
    
    protected String[] CIRCLE_2 = new String[] {
            "os (id=1) part-of os (id=1)"
    };
    
    protected String[] CIRCLE_3 = new String[] {
            "writer (id=1) part-of office (id=2)",
            "office (id=2) part-of officeSuite (id=3)",
            "officeSuite (id=3) part-of WorkTools (id=4)",
            "worktools (id=4) part-of writer (id=1)"
    };
    
    protected String[] CIRCLE_4 = new String[] {
            "writer contained-in office",
            "office contained-in writer",
            "officeSuite contained-in worktools",
            "writer contains worktools"
    };
}
