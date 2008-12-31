/*
 * SubstitutionTestCase.java
 *
 */

package ua.gradsoft.termwaretests;

import junit.framework.*;
import ua.gradsoft.termware.*;

/**
 *
 * @author Ruslan Shevchenko
 */
public class SubstitutionTestCase  extends TestCase
{
    protected void setUp()
    {
      TermWare.getInstance().init();  
    }
    
    public void testPut() throws TermWareException
    {   
      TermFactory termFactory=TermWare.getInstance().getTermFactory();  
      Term x=termFactory.createParsedTerm("p($x)");
      Substitution s = new Substitution();
      s.put(x.getSubtermAt(0), termFactory.createInt(4));
      Term x1=x.subst(s);
      assertEquals(4,x1.getSubtermAt(0).getInt());
    }
    
    public void testNotEmpty() throws TermWareException
    {
      TermFactory termFactory=TermWare.getInstance().getTermFactory();    
      Substitution s = new Substitution();
      s.put(termFactory.createX(0),termFactory.createAtom("qqq"));
      assertFalse(s.isEmpty());
    }
    
    
}
