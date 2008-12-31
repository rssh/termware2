/*
 * GeneralSystemApplyTest.java
 *
 */

package ua.gradsoft.termwaretests.systems;

import junit.framework.*;
import ua.gradsoft.termware.JTerm;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.strategies.FirstTopStrategy;
import ua.gradsoft.termwaretests.systems.helpers.Customer;

/**
 *Tests for apply transformers.
 * @author Ruslan Shevchenko
 */
public class GeneralSystemApplyTest extends TestCase
{
    
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
 
 
    public void testApplyMP() throws TermWareException
    {
        TermSystem mpSystem=new TermSystem(new FirstTopStrategy(),TermWare.getInstance().getFacts("default"),TermWare.getInstance());
        mpSystem.addRule("x->y");
        mpSystem.addRule("y->z");
        TermWare.getInstance().addSystem("mp", mpSystem);
        
        Term t=TermWare.getInstance().getTermFactory().createParsedTerm("mp.x");
        
        t=general_.reduce(t);
        
        assertTrue("t.isAtom",t.isAtom());
        
        assertEquals("z",t.getName());
        
    }
    
    
    public void testApplyJava() throws TermWareException
    {
        TermSystem jCheckSystem = new TermSystem(new FirstTopStrategy(),TermWare.getInstance().getFacts("default"), TermWare.getInstance());
        TermWare.addGeneralTransformers(jCheckSystem);
       // jCheckSystem.setDebugMode(true);
       // jCheckSystem.setDebugEntity("All");
        jCheckSystem.addRule("@class(\"ua.gradsoft.termwaretests.systems.helpers.Customer\",$x) [ $x.getCreditLimit() > 500 ] -> BIG_CREDIT_LIMIT ");
        jCheckSystem.addRule("@class(\"ua.gradsoft.termwaretests.systems.helpers.Customer\",$x) [ $x.getName() == \"scott\" ] -> SCOTT [$x.setCreditLimit(400)] ");
        Customer bigCustomer = new Customer("big");
        bigCustomer.setCreditLimit(2000);
        Term t = jCheckSystem.reduce(new JTerm(bigCustomer));
        assertTrue(t.isAtom());
        assertTrue(t.getName().equals("BIG_CREDIT_LIMIT"));
        Customer scott = new Customer("scott");
        t=jCheckSystem.reduce(new JTerm(scott));
        assertTrue(t.isAtom());
        assertTrue(t.getName().equals("SCOTT"));
        assertTrue(scott.getCreditLimit()==400);
    }
     
    public void testApplyRule1() throws Exception
    {
        Term t = TermWare.getInstance().getTermFactory().createParsedTerm("(f->q).f");
        t = general_.reduce(t);
        assertTrue(t.isAtom());
        assertTrue(t.getName().equals("q"));
    }
    
    public void testApplyRule2() throws Exception
    {
        Term t = TermWare.getInstance().getTermFactory().createParsedTerm("(f->q).q");
        t = general_.reduce(t);
        assertEquals("apply",t.getName());
    }
    
    public void testApplyConditionalRule1() throws Exception
    {
       Term t = TermWare.getInstance().getTermFactory().createParsedTerm("(f($x)[$x==1]->q).f(1)");
       t = general_.reduce(t);
       assertTrue(t.isAtom());
       assertTrue(t.getName().equals("q"));       
    }    

    public void testApplyConditionalRule2() throws Exception
    {
       Term t = TermWare.getInstance().getTermFactory().createParsedTerm("(f($x)[$x==1]->q).f(2)");
       t = general_.reduce(t);
       assertEquals("apply",t.getName());
    }    
    
    
}
