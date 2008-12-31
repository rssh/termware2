/*
 * ListSystemTest.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termwaretests.systems;

import junit.framework.TestCase;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author Ruslan Shevchenko
 */
public class ListSystemTest extends TestCase
{
    
    protected void setUp()
    { TermWare.getInstance().init(); }
    
    
    public void testResolution() throws TermWareException
    {
      TermSystem listSystem=TermWare.getInstance().resolveSystem("List");  
      assertTrue(listSystem!=null);
    }

    /**
     * append(NIL,NIL) -> NIL
     */
    public void testAppend1() throws TermWareException
    {
        TermSystem listSystem=TermWare.getInstance().resolveSystem("List");  
        Term l = TermWare.getInstance().getTermFactory().createNil();
        Term e = TermWare.getInstance().getTermFactory().createNil();
        Term t = TermWare.getInstance().getTermFactory().createTerm("append",e,l);
        t=listSystem.reduce(t);
        assertTrue(t.isNil());
    }

    /**
     * append(NIL,q) -> cons(q,NIL)
     */
    public void testAppend2() throws TermWareException
    {
        TermSystem listSystem=TermWare.getInstance().resolveSystem("List");  
        Term l = TermWare.getInstance().getTermFactory().createNil();
        Term e = TermWare.getInstance().getTermFactory().createAtom("q");
        Term t = TermWare.getInstance().getTermFactory().createTerm("append",e,l);
        t=listSystem.reduce(t);
        assertTrue(t.getName().equals("cons"));
        Term q=t.getSubtermAt(0);
        assertTrue(q.getName().equals("q"));
        Term n=t.getSubtermAt(1);
        assertTrue(n.isNil());
    }
    
    /**
     * append(cons(a,NIL),q) -> cons(a,cons(q,NIL))
     */
    public void testAppend3() throws TermWareException
    {
        TermSystem listSystem=TermWare.getInstance().resolveSystem("List");  
        Term l = TermWare.getInstance().getTermFactory().createParsedTerm("cons(a,NIL)");
        Term e = TermWare.getInstance().getTermFactory().createAtom("q");
        Term t = TermWare.getInstance().getTermFactory().createTerm("append",e,l);
        t=listSystem.reduce(t);
        assertTrue(t.getName().equals("cons"));
        Term a=t.getSubtermAt(0);
        assertTrue(a.getName().equals("a"));
        t=t.getSubtermAt(1);
        assertTrue(t.getName().equals("cons"));
        Term q=t.getSubtermAt(0);
        assertTrue(q.getName().equals("q"));
        Term n=t.getSubtermAt(1);
        assertTrue(n.isNil());
    }

    /**
     * append(q,q)  must be unchanged
     */
    public void testAppend4() throws TermWareException
    {
        TermSystem listSystem=TermWare.getInstance().resolveSystem("List");  
        Term l = TermWare.getInstance().getTermFactory().createAtom("q");
        Term e = TermWare.getInstance().getTermFactory().createAtom("q");
        Term t = TermWare.getInstance().getTermFactory().createTerm("append",e,l);
        t=listSystem.reduce(t);
        assertTrue(t.getName().equals("append"));
    }

    public void testLength1() throws TermWareException
    {
        TermSystem listSystem=TermWare.getInstance().resolveSystem("List");  
        Term l = TermWare.getInstance().getTermFactory().createNil();
        Term t = TermWare.getInstance().getTermFactory().createTerm("length",l);
        t=listSystem.reduce(t);
        assertTrue(t.getInt()==0);
    }
    
}
