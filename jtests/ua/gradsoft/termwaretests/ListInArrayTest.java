/*
 * ListInArrayTest.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termwaretests;

import junit.framework.TestCase;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.ListInArrayTerm;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.strategies.FirstTopStrategy;
import ua.gradsoft.termware.transformers.general.PlusTransformer;

/**
 *
 * @author Ruslan Shevchenko
 */
public class ListInArrayTest extends TestCase
{
    
     
    public ListInArrayTest(String testName) {
        super(testName);
    }
    
    protected void setUp()
    {
        TermWare.getInstance().init();
    }
    
    public void testListReduce1() throws Exception
    {
        TermSystem system = new TermSystem(new FirstTopStrategy(),new DefaultFacts());        
        system.addNormalizer("plus",PlusTransformer.INSTANCE);
        system.addRule("S([$x:$y]) -> $x+S($y)");
        system.addRule("S([]) -> 0 ");
        //system.setDebugMode(true);
        //system.setDebugEntity("All");
        Term[] x = new Term[5];
        x[0]=TermWare.getInstance().getTermFactory().createInt(1);
        x[1]=TermWare.getInstance().getTermFactory().createInt(2);
        x[2]=TermWare.getInstance().getTermFactory().createInt(3);
        x[3]=TermWare.getInstance().getTermFactory().createInt(4);
        x[4]=TermWare.getInstance().getTermFactory().createInt(5);
        Term t = TermWare.getInstance().getTermFactory().createListInArray(x,0);
        t=TermWare.getInstance().getTermFactory().createTerm("S",t);
        t=system.reduce(t);
        assertTrue("result term must be int",t.isInt());
        assertEquals("sum must be 15",15,t.getInt());
    }
    
}
