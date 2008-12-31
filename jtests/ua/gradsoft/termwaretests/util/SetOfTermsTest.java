/*
 * SetOfTermsTest.java
 *
 * Created 30, 06, 2005, 12:35
 * 
 * Owner: Ruslan Shevchenko
 *
 * Description:
 *
 * History:
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termwaretests.util;

import junit.framework.TestCase;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.set.SetOfTerms;

/**
 * Test functionality of class SetOfTerms
 * @author Ruslan Shevchenko
 */
public class SetOfTermsTest extends TestCase
{
    
    /** Creates a new instance of SetOfTermsTest */
    public SetOfTermsTest(String name) {
        super(name);
    }
    
    public void setUp() 
    {
      TermWare.getInstance().init();  
    }
    
    public void testNewIsEmpty() throws Exception
    {
      SetOfTerms x=new SetOfTerms();
      assertTrue(x.getSize()==0);
    }
    
    public void testInsertDifferentAtoms() throws TermWareException
    {
      Term a1=TermWare.getInstance().getTermFactory().createAtom("a1");
      Term a2=TermWare.getInstance().getTermFactory().createAtom("a2");
      SetOfTerms x=new SetOfTerms();
      x.insert(a1);
      x.insert(a2);
      assertTrue(x.getSize()==2);
      x=new SetOfTerms();
      x.insert(a2);
      x.insert(a1);
      assertTrue(x.getSize()==2);
    }
    
    public void testInsertSameAtoms() throws TermWareException
    {
      Term a1=TermWare.getInstance().getTermFactory().createAtom("a1");
      Term a2=TermWare.getInstance().getTermFactory().createAtom("a2");
      SetOfTerms x=new SetOfTerms();
      x.insert(a1);
      x.insert(a2);
      x.insert(a2);
      assertTrue(x.getSize()==2);
      x.insert(a1);
      assertTrue(x.getSize()==2);
    }
    
    
}
