/*
 * DebugTestSuite.java
 *
 * Created on July 4, 2007, 11:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.termwaretests.debug;

import junit.framework.TestSuite;

/**
 *
 * @author rssh
 */
public class DebugTestSuite extends TestSuite
{
    
    /** Creates a new instance of DebugTestSuite */
    public DebugTestSuite() {
        addTest(new TestSuite(FileAndLineAccessTestCase.class));
        addTest(new TestSuite(UnificationRuleDebugTestCase.class));
        addTest(new TestSuite(LabelsInDebugModeTestCase.class));
    }
    
    public void testMakeJunitHappy() {}    
    
    public static void main(String[] args)
    {
          junit.textui.TestRunner.run(new DebugTestSuite());
    }
    
    
}
