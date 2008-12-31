/*
 * GeneralSystemLetTest.java
 *
 */

package ua.gradsoft.termwaretests.systems;

import ua.gradsoft.termware.*;
import junit.framework.*;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

/**
 *
 * @author Ruslan Shevchenko
 */
public class GeneralSystemLetTest extends TestCase
{
    
          private TermSystem local_;
    
    protected void setUp() throws TermWareException
    { 
        TermWare.getInstance().init();
        local_=new TermSystem(new FirstTopStrategy(),new DefaultFacts(),TermWare.getInstance());        
        TermWare.addGeneralTransformers(local_);
    }
    
    protected void tearDown()
    {
        local_=null;
    }
    
    public void  testLet1() throws TermWareException
    {
        local_.apply("let x->y");
        Term t=TermWare.getInstance().getTermFactory().createAtom("x");
        t=local_.reduce(t);
        assertEquals("y",t.getName());
    }
  
    
    public void  testLet2() throws TermWareException
    {
        local_.apply("let q($x)->f($x)");
        Term t0=TermWare.getInstance().getTermFactory().createAtom("xx");
        Term t1=TermWare.getInstance().getTermFactory().createTerm("q",t0);
        Term t=local_.reduce(t1);
        assertEquals("f",t.getName());
    }
     
   
    public void  testLet3() throws TermWareException
    {
        local_.apply("let qq($x) [ $x==1 ] ->f($x)");
        Term t0=TermWare.getInstance().getTermFactory().createInt(1);
        Term t1=TermWare.getInstance().getTermFactory().createTerm("qq",t0);
        Term t=local_.reduce(t1);
        assertEquals("f",t.getName());
        t0=TermWare.getInstance().getTermFactory().createAtom("x");
        t1=TermWare.getInstance().getTermFactory().createTerm("qq",t0);
        t=local_.reduce(t1);
        assertEquals("qq",t.getName());
    }
    
     
    
    
}
