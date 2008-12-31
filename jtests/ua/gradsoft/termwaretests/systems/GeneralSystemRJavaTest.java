/*
 * GeneralSystemRJavaTest.java
 *
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termwaretests.systems;

import junit.framework.TestCase;
import ua.gradsoft.termware.IFacts;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.strategies.FirstTopStrategy;
import ua.gradsoft.termwaretests.systems.helpers.Customer;

/**
 *
 * @author Ruslan Shevchenko
 */
public class GeneralSystemRJavaTest extends TestCase
{
    
    /** Creates a new instance of GeneralSystemRJavaTest */
    public GeneralSystemRJavaTest() {
    }
    
    protected void setUp() throws Exception
    {
        TermWare.getInstance().init();
        IFacts facts = new GeneralSystemRFacts();
        testSystem_ = new TermSystem(new FirstTopStrategy(),facts);
        testSystem_.addRule("A [ getIntValue() == 1 ] -> A1" );
        testSystem_.addRule("B [ getIntValue() == 2 ] -> B1" );
        testSystem_.addRule("C -> $x [ assign($x,getIntValue()) ]" );
        testSystem_.addRule("D($x) -> $y [ assign($y,createCustomer($x)) ]");
        testSystem_.addRule("E -> e1($y) && e2($y) [ assign($y,createCustomer(q)) ]");        
        testSystem_.addRule("e1($y) -> p($y) [ $y.setName(e1) ]");        
        testSystem_.addRule("e2($y) -> p($y) [ $y.setCreditLimit(100) ]");        
        testSystem_.addRule("p($y) && p($y) ->  $y ");        
    }
    
    public void testIntFactsMethod() throws TermWareException
    {
        Term aTerm = TermWare.getInstance().getTermFactory().createAtom("A");
        Term r = testSystem_.reduce(aTerm);
        assertEquals("rule A test is incorrect:","A1",r.getName());
        Term bTerm = TermWare.getInstance().getTermFactory().createAtom("B");        
        r = testSystem_.reduce(bTerm);
        assertEquals("rule B test is incorrect:","B",r.getName());
        Term cTerm = TermWare.getInstance().getTermFactory().createAtom("C");        
        r = testSystem_.reduce(cTerm);
        assertEquals("rule C test is incorrect:","1",r.getName());
    }
    
    public void testObjectInFactsMethod() throws TermWareException
    {
        Term dTerm = TermWare.getInstance().getTermFactory().createTerm("D","aaa");
        Term r = testSystem_.reduce(dTerm);
        assertTrue("rule D must return jobject", r.isJavaObject());        
    }
    
    public void testModifyObjectInFacts() throws TermWareException
    {
        Term eTerm = TermWare.getInstance().getTermFactory().createAtom("E");
        Term r = testSystem_.reduce(eTerm);
        assertTrue("rule E must return jobject term", r.isJavaObject());
        assertTrue("rule E must return Customer", r.getJavaObject() instanceof Customer);
        Customer cr = (Customer)r.getJavaObject();
        assertTrue("invalid name",cr.getName().equals("e1"));
        assertTrue("invalid credit limit",cr.getCreditLimit()==100);
    }
    
    private TermSystem testSystem_;
}
