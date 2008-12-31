/*
 * GeneralSystemListTest.java
 *
 *
 * Copyright (c) 2004-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termwaretests.systems;

import junit.framework.TestCase;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;

/**
 *
 * @author Ruslan Shevchenko
 */
public class GeneralSystemListTest extends TestCase
{
    
    protected void setUp() throws Exception
    {
       TermWare.getInstance().init();
       system_=TermWare.getInstance().resolveSystem("general");
       system_.addRule("len([$x:$y])->1+len($y)");
       system_.addRule("len([])->0");
       system_.addRule("car([$x:$y])->$x");
       system_.addRule("car([])->[]");
       system_.addRule("cdr([$x:$y])->$y");
       system_.addRule("cdr([])->[]");
    }
    
    public void testEmptyListLen() throws Exception
    {
        Term emptyList = TermWare.getInstance().getTermFactory().createParsedTerm("[]");
        Term lenList = TermWare.getInstance().getTermFactory().createTerm("len",emptyList);
        Term r = system_.reduce(lenList);
        assertTrue(r.getAsInt(TermWare.getInstance())==0);
    }
    
    public void testListLen1() throws Exception
    {
        Term list = TermWare.getInstance().getTermFactory().createParsedTerm("[x]");
        Term lenList = TermWare.getInstance().getTermFactory().createTerm("len",list);
        Term r = system_.reduce(lenList);
        assertTrue(r.getAsInt(TermWare.getInstance())==1);        
    }

     public void testListLen2() throws Exception
    {
        Term list = TermWare.getInstance().getTermFactory().createParsedTerm("[x,y]");
        Term lenList = TermWare.getInstance().getTermFactory().createTerm("len",list);
        Term r = system_.reduce(lenList);
        assertTrue(r.getAsInt(TermWare.getInstance())==2);        
    }

     public void testListLen3() throws Exception
    {
        Term list = TermWare.getInstance().getTermFactory().createParsedTerm("[x,y,z]");
        Term lenList = TermWare.getInstance().getTermFactory().createTerm("len",list);
        Term r = system_.reduce(lenList);
        assertTrue(r.getAsInt(TermWare.getInstance())==3);        
    }

     public void testListLen4() throws Exception
    {
        Term list = TermWare.getInstance().getTermFactory().createParsedTerm("[x,y,z]");
        Term lenList = TermWare.getInstance().getTermFactory().createTerm("len",list);
        Term r = system_.reduce(lenList);
        assertTrue(r.getAsInt(TermWare.getInstance())==3);        
    }

     public void testListCar1() throws Exception
    {
        Term list = TermWare.getInstance().getTermFactory().createParsedTerm("[x]");
        Term carList = TermWare.getInstance().getTermFactory().createTerm("car",list);
        Term r = system_.reduce(carList);
        assertTrue(r.isAtom() && r.getName().equals("x"));        
    }
     
     public void testListCar2() throws Exception
    {
        Term list = TermWare.getInstance().getTermFactory().createParsedTerm("[x,y]");
        Term carList = TermWare.getInstance().getTermFactory().createTerm("car",list);
        Term r = system_.reduce(carList);
        assertTrue(r.isAtom() && r.getName().equals("x"));        
    }
     
     public void testListCar3() throws Exception
    {
        Term list = TermWare.getInstance().getTermFactory().createParsedTerm("[x,y,z]");
        Term carList = TermWare.getInstance().getTermFactory().createTerm("car",list);
        Term r = system_.reduce(carList);
        assertTrue(r.isAtom() && r.getName().equals("x"));        
    }
    
     public void testListCdr0() throws Exception
    {
        Term list = TermWare.getInstance().getTermFactory().createParsedTerm("[]");
        Term cdrList = TermWare.getInstance().getTermFactory().createTerm("cdr",list);
        Term r = system_.reduce(cdrList);
        assertTrue(r.isNil());        
    }
     
     public void testListCdr1() throws Exception
    {
        Term list = TermWare.getInstance().getTermFactory().createParsedTerm("[x]");
        Term cdrList = TermWare.getInstance().getTermFactory().createTerm("cdr",list);
        Term r = system_.reduce(cdrList);
        assertTrue(r.isNil());        
    }

    public void testListCdr2() throws Exception
    {
        Term list = TermWare.getInstance().getTermFactory().createParsedTerm("[x,y]");
        Term cdrList = TermWare.getInstance().getTermFactory().createTerm("cdr",list);
        Term r = system_.reduce(cdrList);
        assertTrue(r.isComplexTerm() && r.getName().equals("cons"));        
    }

    public void testListCdr3() throws Exception
    {
        Term list = TermWare.getInstance().getTermFactory().createParsedTerm("[x,y,z]");
        Term cdrList = TermWare.getInstance().getTermFactory().createTerm("cdr",list);
        Term r = system_.reduce(cdrList);
        assertTrue(r.isComplexTerm() && r.getName().equals("cons"));       
        Term lenList = TermWare.getInstance().getTermFactory().createTerm("len",r);
        r=system_.reduce(lenList);
        assertTrue(r.getAsInt(TermWare.getInstance())==2);
    }

     
    private TermSystem system_;
}
