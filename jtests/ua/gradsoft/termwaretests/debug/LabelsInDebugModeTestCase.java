/*
 * LabelsInDebugModeTestCase.java
 *
 */

package ua.gradsoft.termwaretests.debug;

import junit.framework.TestCase;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termwaretests.demos.labels.LabelsTestCase;

/**
 *This testcase rerun labels in debug mode.
 * @author rssh
 */
public class LabelsInDebugModeTestCase extends TestCase
{
    
    protected void setUp() throws TermWareException
    {
       TermWare.setInDebug(true);
       TermWare.getInstance().init();
       TermSystem ts = TermWare.getInstance().getOrCreateDomain("examples").resolveSystem("LabelText");
       // set new facts, to reset references.
       ts.setFacts(new ua.gradsoft.termwaretests.demos.labels.Facts());
    }
    
    protected void tearDown()
    {
        TermWare.setInDebug(false);
    }

    public void testDebugLabelsExample() throws TermWareException
    {
        String input = "@ref{one} @label{two} @ref{one} qqq\n"+
                       "aaa @label{one} @ref{two}";
        
        String output = LabelsTestCase.transformText(input);
        
        //System.out.println("output="+output);
        
        assertTrue(output.contains("[2]"));
        
    }
    
    
}
