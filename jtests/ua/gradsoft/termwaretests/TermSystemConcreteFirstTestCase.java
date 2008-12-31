/*
 * TermSystemConcreteFirstTestCase.java
 *
 * Created on June 26, 2007, 9:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.termwaretests;

import junit.framework.TestCase;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

/**
 *
 * @author rssh
 */
public class TermSystemConcreteFirstTestCase extends TestCase
{        
        
    protected void setUp() throws Exception
    {
      TermWare.getInstance().init();
      termSystem_=new TermSystem(new FirstTopStrategy(),new DefaultFacts());

    }
        
    public void testSameDifferent() throws Exception
    {
      termSystem_.addRule("p($x,$x) -> same");
      termSystem_.addRule("p($x,$y) -> different");
       
      Term t1 = TermWare.getInstance().getTermFactory().createParsedTerm("p(a,a)");
      
      Term t2 = termSystem_.reduce(t1);
      
      assertEquals("must be same","same",t2.getName());
      
      t1 = TermWare.getInstance().getTermFactory().createParsedTerm("p(a,b)");
      t2 = termSystem_.reduce(t1);
      assertEquals("must be different","different",t2.getName());
        
    }

    public void testDifferentSame() throws Exception
    {
      termSystem_.addRule("p($x,$x) -> same");
      termSystem_.addRule("p($x,$y) -> different");
       
      Term t1 = TermWare.getInstance().getTermFactory().createParsedTerm("p(a,a)");
      
      Term t2 = termSystem_.reduce(t1);
      
      assertEquals("must be same","same",t2.getName());

      t1 = TermWare.getInstance().getTermFactory().createParsedTerm("p(a,b)");
      t2 = termSystem_.reduce(t1);
      assertEquals("must be different","different",t2.getName());
            
    }
    
    public void testConcreteS1() throws Exception
    {
       termSystem_.addRule("p(q($x),$y) -> A");
       termSystem_.addRule("p(s($x),$y) -> B");
       termSystem_.addRule("p($x,$y) -> G");
       
       Term tA = TermWare.getInstance().getTermFactory().createParsedTerm("p(q(s(l)),q(A))");
       Term tB = TermWare.getInstance().getTermFactory().createParsedTerm("p(s(l),q(A))");
       Term tG = TermWare.getInstance().getTermFactory().createParsedTerm("p(1,q(A))");
       
       Term tAr = termSystem_.reduce(tA);
       assertEquals("must be A","A",tAr.getName());
       Term tBr = termSystem_.reduce(tB);
       assertEquals("must be B","B",tBr.getName());
       Term tGr = termSystem_.reduce(tG);
       assertEquals("must be G","G",tGr.getName());
       
    }

    private TermSystem termSystem_;


}
