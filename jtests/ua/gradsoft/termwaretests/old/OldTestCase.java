/*
 * OldTestCase.java
 *
 */

package ua.gradsoft.termwaretests.old;

import junit.framework.*;

/**
 *TestCase for old tests.
 * @author Ruslan Shevchenko
 */
public class OldTestCase extends TestCase
{
    
    /** Creates a new instance of OldTestCase */
    public OldTestCase() {
    }
    
    public void testAllOld()
    {
        AllTests allTests=new AllTests();
        allTests.setInJUnit(true);
        boolean result = allTests.runAll(new String[0]);
        assertEquals("none",allTests.getFailedTest());
        assertEquals(result,true);
    }
    
}
