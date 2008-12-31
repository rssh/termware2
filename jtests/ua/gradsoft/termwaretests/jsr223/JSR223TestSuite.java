/*
 * JSR223TestSuite.java
 *
 * Created on July 19, 2007, 5:45 PM
 */

package ua.gradsoft.termwaretests.jsr223;

import junit.framework.TestSuite;

/**
 *
 * @author rssh
 */
public class JSR223TestSuite extends TestSuite
{
    
    /**
     * Creates a new instance of JSR223TestSuite
     */
    public JSR223TestSuite() {
        addTest(new TestSuite(JSR223FirstTestCase.class));   
        addTest(new TestSuite(JSR223SecondTestCase.class));
    }
    
    public void testMakeAntHappy() {}
    
    public static void main(String[] args)
    {
          junit.textui.TestRunner.run(new JSR223TestSuite());
    }
        
    
}
