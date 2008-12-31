/*
 * GeneralSystemtestDoubleAriphmetics.java
 *
 *
 * Owner: Ruslan Shevchenko
 *
 * Copyright (c) 2004-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termwaretests.systems;

import junit.framework.TestCase;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

/**
 * Test ariphmetis.
 * @author Ruslan Shevchenko
 */
public class GeneralSystemtestDoubleAriphmetics extends TestCase
{
    
   
    protected void setUp() throws Exception
    {
        TermWare.getInstance().init();
        testSystem_ = new TermSystem(new FirstTopStrategy(),new DefaultFacts());
        TermWare.addGeneralTransformers(testSystem_);
        testSystem_.addRule("A($x,$y)->$x+$y" );
        testSystem_.addRule("A1 ->  A(2.0,3.1415926)" );
        testSystem_.addRule("B($x,$y) -> $x * $y");
        
    }

    public void testPlusA1() throws Exception
    {
        Term t = TermWare.getInstance().getTermFactory().createAtom("A1");
        t=testSystem_.reduce(t);        
        assertTrue("reduce A1 must be number, instead it "+TermHelper.termToString(t),t.isNumber());
        assertTrue("reduce A1 must be double",t.isDouble());
        assertTrue("value of A1 :"+t.getDouble(),Math.abs(5.1415926-t.getDouble()) < 1e-10);
    }
    
    private TermSystem testSystem_;
    
}
