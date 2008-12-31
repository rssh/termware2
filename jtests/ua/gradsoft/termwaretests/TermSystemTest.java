/*
 * TermSystemTest.java
 *
 */

package ua.gradsoft.termwaretests;

import junit.framework.*;
import ua.gradsoft.termware.*;
import ua.gradsoft.termware.strategies.*;

/**
 *
 * @author Ruslan Shevchenko
 */
public class TermSystemTest extends TestCase
{
    
    private TermSystem ts1_;
    private TermSystem ts2_;
    private TermSystem ruleWithStop_;
    
    protected void setUp() 
    {
      try {  
        TermWare.getInstance().init();  
        
        ts1_=new TermSystem(new FirstTopStrategy(),new DefaultFacts());
        ts1_.addRule("x -> y");
        ts1_.addRule("OTHERWISE($x)->z [ setCurrentStopFlag(true) ] ");
        //ts1_.addRule("OTHERWISE($x)->z ");
        
        ts2_=new TermSystem(new FirstTopStrategy(),new DefaultFacts());
        ts2_.addRule("P(x)->z");
        
        ruleWithStop_=new TermSystem(new FirstTopStrategy(),new DefaultFacts());
        ruleWithStop_.addRule("x->y [ setCurrentStopFlag(true) ]");
        ruleWithStop_.addRule("y->z");
        
      }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }
    }
    
    TermFactory getTermFactory()
    { return TermWare.getInstance().getTermFactory(); }
    
    /**
     * check, than strategy hasOtherwise after inserting OTHERWISE ters,
     */
    public void testHasOtherwise() throws TermWareException
    {
      assertTrue(ts1_.getStrategy().hasOtherwise());
    }
    
    /**
     * check, that otherwise rule is really work.
     */
    public void testOtherwiseIsWork() throws TermWareException
    {
      Term t=getTermFactory().createAtom("q"); // q is not x
     // ts1_.setDebugMode(true);
     // ts1_.setDebugEntity("All");
      t=ts1_.reduce(t);
      //System.err.print("t is ");
      //t.print(System.err);
      //System.err.println();
      assertTrue(t.isAtom());
      assertEquals("z",t.getName());
    }
    
    public void testSimpleSubstitutionOfComplexTerm() throws TermWareException
    {
      Term t=getTermFactory().createTerm("P",getTermFactory().createAtom("x"));
      t=ts2_.reduce(t);
      assertTrue(t.isAtom());
      assertEquals(t.getName(),"z");
    }
    
    public void testRuleWithStop() throws TermWareException
    {
      Term t=getTermFactory().createAtom("x");
      t=ruleWithStop_.reduce(t);
      assertTrue(t.isAtom());
      assertEquals("y",t.getName());
    }
    
    public void testTrueReduce() throws TermWareException
    {
        TermSystem system=new TermSystem(new FirstTopStrategy(),
                                         new DefaultFacts(), 
                                         TermWare.getInstance()
                                         );
        system.addRule("p($x)->true");
       // system.setLoggingMode(true);
       // system.setLoggedEntity("All");
        Term t=system.reduce(getTermFactory().createTerm("p",new JTerm(system)));
        //t.println(System.err);
        assertTrue(t.isBoolean());
        assertTrue(t.getBoolean());
    }

    
}
