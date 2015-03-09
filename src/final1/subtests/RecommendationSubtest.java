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
            "CentOS5 ( id= 105) contained-in operattiingSystem",
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
}
