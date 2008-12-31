/*
 * ClassPatternTest.java
 *
 */

package ua.gradsoft.termwaretests;

import junit.framework.TestCase;
import ua.gradsoft.termware.ClassPatternTerm;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.JTerm;
import ua.gradsoft.termware.Substitution;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

/**
 *Test class pattern
 * @author Ruslan Shevchenko
 */
public class ClassPatternTest extends TestCase
{
    
    /** Creates a new instance of ClassPatternTest */
    public ClassPatternTest(String testName) {
        super(testName);
    }
    
    protected void setUp()
    {
        TermWare.getInstance().init();
    }
    
    public void testIsClassUnify0() throws TermWareException
    {
        Term jterm=TermWare.getInstance().getTermFactory().createJTerm(this);
        Term x=TermWare.getInstance().getTermFactory().createX(0);
        Term cp=TermWare.getInstance().getTermFactory().createTerm("@class",this.getClass().getName(),x);        
        assertTrue(cp instanceof ClassPatternTerm);
        Substitution s = new Substitution();
        boolean ur=cp.freeUnify(jterm, s);
        assertTrue(ur);
        Term t = s.get(0);
        assertEquals(jterm,t);
    }
    
    public void testIsNotClassUnify1() throws TermWareException
    {        
        Term x=TermWare.getInstance().getTermFactory().createX(0);
        Term jterm=TermWare.getInstance().getTermFactory().createJTerm(TermWare.getInstance());
        Term cp=TermWare.getInstance().getTermFactory().createTerm("@class",this.getClass().getName(),x);        
        assertTrue(cp instanceof ClassPatternTerm);
        Substitution s = new Substitution();
        boolean ur=cp.freeUnify(jterm, s);
        assertFalse(ur);
    }
    
    public void testIsNotClassUnify2() throws TermWareException
    {        
        Term x=TermWare.getInstance().getTermFactory().createX(0);
        Term it=TermWare.getInstance().getTermFactory().createInt(2);
        Term cp=TermWare.getInstance().getTermFactory().createTerm("@class",this.getClass().getName(),x);        
        assertTrue(cp instanceof ClassPatternTerm);
        Substitution s = new Substitution();
        boolean ur=cp.freeUnify(it, s);
        assertFalse(ur);
    }
    
    public void testClassPatternRule1() throws TermWareException
    {
        TermSystem system=new TermSystem(new FirstTopStrategy(),
                                         new DefaultFacts(), 
                                         TermWare.getInstance()
                                         );
        system.addRule("@class(\"java.lang.Object\",$x)->true");
        //system.setDebugMode(true);
        //system.setDebugEntity("All");
        Term t=system.reduce(new JTerm(system));
        //t.println(System.err);
        assertTrue(t.isBoolean());
        assertTrue(t.getBoolean());
    }
    
    public void testClassPatternRule2() throws TermWareException
    {
        TermSystem system=new TermSystem(new FirstTopStrategy(),
                                         new DefaultFacts(), 
                                         TermWare.getInstance()
                                         );
        system.addRule("@class(\"ua.gradsoft.termware.TermSystem\",$x)->true");
        //system.setDebugMode(true);
        //system.setDebugEntity("All");
        Term t=system.reduce(new JTerm(system));
        //t.println(System.err);
        assertTrue(t.isBoolean());
        assertTrue(t.getBoolean());
    }
   
     public void testClassPatternRule3() throws TermWareException
     {
        TermSystem system=new TermSystem(new FirstTopStrategy(),
                                         new DefaultFacts(), 
                                         TermWare.getInstance()
                                         );
        system.addRule("@class(\"ua.gradsoft.termware.Term\",$x)->true");
        //system.setDebugMode(true);
        //system.setDebugEntity("All");
        Term t=system.reduce(new JTerm(system));
        //t.println(System.err);
        assertFalse(t.isBoolean());        
    }
   
    
}
