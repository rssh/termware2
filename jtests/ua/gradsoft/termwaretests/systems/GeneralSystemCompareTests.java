/*
 * GeneralSystemCompareTests.java
 *
 */

package ua.gradsoft.termwaretests.systems;

import java.math.BigDecimal;
import junit.framework.TestCase;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;


/**
 *test for compare (less, less_eq, greater, greater_eq)
 * @author Ruslan Shevchenko
 */
public class GeneralSystemCompareTests extends TestCase {
    
       private TermSystem general_;
    
    protected void setUp() throws TermWareException
    { 
        TermWare.getInstance().init();
        general_=TermWare.getInstance().resolveSystem("general");
    }
    
    protected void tearDown()
    {
        general_=null;
    }
 
    public void testLessBigDecimalBigDecimal1()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createBigDecimal(BigDecimal.valueOf(1));
      Term snd=TermWare.getInstance().getTermFactory().createBigDecimal(BigDecimal.valueOf(2));
      Term lessTerm=TermWare.getInstance().getTermFactory().createTerm("less",frs,snd);
      
      Term t=general_.reduce(lessTerm);
      assertTrue(t.getBoolean());
      
    }
    
    public void testLessInt1Int1()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createInt(1);
      Term snd=TermWare.getInstance().getTermFactory().createInt(1);
      Term lessTerm=TermWare.getInstance().getTermFactory().createTerm("less",frs,snd);
      
      Term t=general_.reduce(lessTerm);
      
      assertFalse(t.getBoolean());      
    }
    

    public void testLessEqInt1Int1()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createInt(1);
      Term snd=TermWare.getInstance().getTermFactory().createInt(1);
      Term lessEqTerm=TermWare.getInstance().getTermFactory().createTerm("less_eq",frs,snd);
      
      Term t=general_.reduce(lessEqTerm);
      
      assertTrue(t.getBoolean());      
    }

    public void testLessEqInt1Int2()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createInt(1);
      Term snd=TermWare.getInstance().getTermFactory().createInt(2);
      Term lessEqTerm=TermWare.getInstance().getTermFactory().createTerm("less_eq",frs,snd);
      
      Term t=general_.reduce(lessEqTerm);
      
      assertTrue(t.getBoolean());      
    }
 
    
    public void testGreaterStringAStringB()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createString("A");
      Term snd=TermWare.getInstance().getTermFactory().createString("B");
      Term greaterTerm=TermWare.getInstance().getTermFactory().createTerm("greater",frs,snd);
      
      Term t=general_.reduce(greaterTerm);
      
      assertFalse(t.getBoolean());      
    }

    public void testGreaterStringBStringA()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createString("B");
      Term snd=TermWare.getInstance().getTermFactory().createString("A");
      Term greaterTerm=TermWare.getInstance().getTermFactory().createTerm("greater",frs,snd);
      
      Term t=general_.reduce(greaterTerm);
      
      assertTrue(t.getBoolean());      
    }
 
    public void testGreaterStringAStringA()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createString("A");
      Term snd=TermWare.getInstance().getTermFactory().createString("A");
      Term greaterTerm=TermWare.getInstance().getTermFactory().createTerm("greater",frs,snd);
      
      Term t=general_.reduce(greaterTerm);
      
      assertFalse(t.getBoolean());      
    }

    public void testGreaterEqStringAStringA()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createString("A");
      Term snd=TermWare.getInstance().getTermFactory().createString("A");
      Term greaterTerm=TermWare.getInstance().getTermFactory().createTerm("greater_eq",frs,snd);
      
      Term t=general_.reduce(greaterTerm);
      
      assertTrue(t.getBoolean());      
    }

    
    
}
