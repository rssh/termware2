 /*
 * JECheckFacts.java

 */

package ua.gradsoft.termwaredemos.jsa;

import ua.gradsoft.termware.*;

/**
 *Facts database for JECheckFacts.
 * @author  Ruslan Shevchenko
 */
public class JECheckFacts extends DefaultFacts {
    
    private int nProblems = 0;
    
    /** Creates a new instance of JECheckFacts */
    public JECheckFacts() throws TermWareException
    {
    }
    
    public void foundProblem(TransformationContext ctx, String message) throws TermWareException
    {
     getEnv().getOutput().println("problem:"+message);
     ++nProblems;
     //throw new AssertException("qqq");
    }
   
    
    public int getNProblems()
    {
     return nProblems;
    }
    
}
