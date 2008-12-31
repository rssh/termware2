/*
 * BTStrategyTest.java
 *
 * Copyright (c) 2004-2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termwaretests.strategies;

import junit.framework.Assert;
import junit.framework.TestCase;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.IFacts;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.strategies.BTStrategy;

/**
 *Test BT strategy.
 * @author Ruslan Shevchenko
 */
public class BTStrategyTest extends TestCase
{

    public void  testBTStrategy() throws Exception
    {
        BTStrategy strategy = new BTStrategy();
        IFacts facts = new DefaultFacts();
        TermSystem ts = new TermSystem(strategy,facts,TermWare.getInstance());
        ts.addRule("B($x)->$x");
       // ts.setDebugEntity("All");
       // ts.setDebugMode(true);
        
        Term t = TermWare.getInstance().getTermFactory().createParsedTerm("A(B(C(q)))");
        String checkName=t.getSubtermAt(0).getName();
        Assert.assertTrue("B must be first subterm",checkName.equals("B"));
        t=ts.reduce(t);
        checkName=t.getSubtermAt(0).getName();
        Assert.assertFalse("B must be erased",checkName.equals("B"));
    }
    
}
