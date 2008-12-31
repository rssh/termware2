/*
 * UnificationRuleDebugTestCase.java
 *
 */

package ua.gradsoft.termwaretests.debug;

import junit.framework.TestCase;
import ua.gradsoft.termware.*;

/**
 *Testcase for running in debug mode.
 * @author rssh
 */
public class UnificationRuleDebugTestCase extends TestCase
{
    
    protected void setUp()
    {
       TermWare.setInDebug(true);
       TermWare.getInstance().init();
    }
    
    protected void tearDown()
    {
        TermWare.setInDebug(false);
    }
    
    public void testUnification1() throws Exception
    {
       //System.out.println("Start debuger, than press enter"); 
       //System.in.read();
       //String text = System.console().readLine(); 
       TermSystem xyz = TermWare.getInstance().getRoot().getOrCreateSubdomain("examples").resolveSystem("xyz");
       Term input = TermWare.getInstance().getTermFactory().createAtom("x");
       Term output = xyz.reduce(input);
    }
    
 
}
