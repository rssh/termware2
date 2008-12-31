/*
 * FactsReduceTest.java
 *
 * Copyright (c) 2004-2008 GradSoft  Ukraine
 *http://www.gradsoft.ua
 * All Rights Reserved
 */


package ua.gradsoft.termwaretests;

import junit.framework.TestCase;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

/**
 *
 * @author Ruslan Shevchenko
 */
public class FactsReduceTest extends TestCase
{
    
    
    protected void setUp() throws Exception
    {
      TermWare.getInstance().init();
      termSystem_=new TermSystem(new FirstTopStrategy(),new DefaultFacts());
      termSystem_.addRule("P($x,$y,$z) [ match($x,$y) ] -> Q($x,$y,$z) !-> false");
      termSystem_.addRule("Q($x,$y,$z) -> true");
      termSystem_.addRule("match($x,$y) -> len($x)==4 && car($x)==$y");
      termSystem_.addRule("len([])->0");
      termSystem_.addRule("len([$x:$y])->1+len($y)");
      termSystem_.addRule("car([$x:$y])->$x");
      
    }
    
    public void  testFactsReduce1True() throws Exception
    {
      Term xTerm = TermWare.getInstance().getTermFactory().createParsedTerm("[a,b,c,d]");  
      Term yTerm = TermWare.getInstance().getTermFactory().createAtom("a");
      Term zTerm = TermWare.getInstance().getTermFactory().createAtom("z");
      Term pTerm = TermWare.getInstance().getTermFactory().createTerm("P",xTerm,yTerm,zTerm);
      Term r = termSystem_.reduce(pTerm);
      assertTrue(r.isBoolean() && r.getBoolean());
    }
    
    public void  testFactsReduce1False() throws Exception
    {
      Term xTerm = TermWare.getInstance().getTermFactory().createParsedTerm("[a,b]");  
      Term yTerm = TermWare.getInstance().getTermFactory().createAtom("a");
      Term zTerm = TermWare.getInstance().getTermFactory().createAtom("z");
      Term pTerm = TermWare.getInstance().getTermFactory().createTerm("P",xTerm,yTerm,zTerm);
      Term r = termSystem_.reduce(pTerm);
      assertTrue(r.isBoolean() && r.getBoolean()==false);
    }
    
    
    
    private TermSystem termSystem_;
    
}
