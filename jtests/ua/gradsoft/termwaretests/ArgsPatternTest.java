/*
 * ArgsPatternTest.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termwaretests;

import junit.framework.TestCase;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.Substitution;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.strategies.FirstTopStrategy;
import ua.gradsoft.termware.transformers.general.PlusTransformer;

/**
 *
 * @author Ruslan Shevchenko
 */
public class ArgsPatternTest extends TestCase
{
         
    public ArgsPatternTest(String testName) {
        super(testName);
    }
    
    protected void setUp()
    {
        TermWare.getInstance().init();
    }
    
    public void testArgsPatternUnifyRightFun() throws Exception
    {
        Term f = TermWare.getInstance().getTermFactory().createAtom("f");
        Term x = TermWare.getInstance().getTermFactory().createX(1);
        Term afx = TermWare.getInstance().getTermFactory().createArgsPatternTerm(f,x);
        
        Term ff3 = TermWare.getInstance().getTermFactory().createParsedTerm("f(x1,x2,x3)");
        
        Substitution s = new Substitution();
        boolean b = afx.freeUnify(ff3,s);
        assertTrue("f..($x) must be unified with f(x1,x2,x3)",b);
        Term xv=s.get(1);
        assertTrue("x must be filled",xv!=null);
        assertEquals("xv must be cons","cons",xv.getName());
        assertEquals("xv.getSubtermAt(0) must be x1","x1",xv.getSubtermAt(0).getName());
        Term xv1=xv.getSubtermAt(1);
        assertEquals("xv1 must be cons","cons",xv1.getName());
        assertEquals("xv1.getSubtermAt(0) must be x2","x2",xv1.getSubtermAt(0).getName());
        Term xv2=xv1.getSubtermAt(1);
        assertEquals("xv2 must be cons","cons",xv2.getName());
        assertEquals("xv2.getSubtermAt(0) must be x3","x3",xv2.getSubtermAt(0).getName());
        assertTrue("xv2.getSubtermAt(1) must be NIL",xv2.getSubtermAt(1).isNil());
        
        Term ff1 = TermWare.getInstance().getTermFactory().createParsedTerm("f(x1)");
        s = new Substitution();
        
        
    }
    
    public void testReduce1() throws Exception
    {
        TermSystem system = new TermSystem(new FirstTopStrategy(),new DefaultFacts());        
        system.addNormalizer("plus",PlusTransformer.INSTANCE);
        system.addRule("P..($x) -> S($x)");
        system.addRule("S([$x:$y]) -> $x+S($y)");
        system.addRule("S([]) -> 0 ");
       // system.setDebugMode(true);
       // system.setDebugEntity("All");
        Term[] x = new Term[5];
        x[0]=TermWare.getInstance().getTermFactory().createInt(1);
        x[1]=TermWare.getInstance().getTermFactory().createInt(2);
        x[2]=TermWare.getInstance().getTermFactory().createInt(3);
        x[3]=TermWare.getInstance().getTermFactory().createInt(4);
        x[4]=TermWare.getInstance().getTermFactory().createInt(5);        
        Term t=TermWare.getInstance().getTermFactory().createTerm("P",x);
        t=system.reduce(t);
        assertTrue("result term must be int",t.isInt());
        assertEquals("sum must be 15",15,t.getInt());
    }

    public void testReduce2() throws Exception
    {
        TermSystem system = new TermSystem(new FirstTopStrategy(),new DefaultFacts());        
        system.addNormalizer("plus",PlusTransformer.INSTANCE);
        system.addRule("f..($x) -> c(g,$x)");
        system.addRule("c($x,$y) -> $x..($y)");        
        //system.setDebugMode(true);
        //system.setDebugEntity("All");
        Term t=TermWare.getInstance().getTermFactory().createParsedTerm("f(1,2,3,4,5)");
        t=system.reduce(t);
        assertEquals("result term must be g","g",t.getName());        
        assertEquals("result term arity",5,t.getArity());        
    }
    
}
