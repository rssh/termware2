/*
 * SetPatternTest.java
 *
 * Created on: 30, 06, 2005, 12:05
 *
 * UBS application
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termwaretests;

import junit.framework.TestCase;
import ua.gradsoft.termware.AtomTerm;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.Substitution;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.set.SetTerm;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

/**
 * Test operations on set pattern
 * @author Ruslan Shevchenko
 */
public class SetPatternTest extends TestCase
{
    
    /** Creates a new instance of SetPatternTest */
    public SetPatternTest(String name) {
        super(name);
    }
    
    public void setUp()
    {
      TermWare.getInstance().init();  
    }
    
    public void testSetIdentity1() throws TermWareException
    {
      SetTerm setTerm=TermWare.getInstance().getTermFactory().createSetTerm();
      AtomTerm atom=TermWare.getInstance().getTermFactory().createAtom("qqq");
      setTerm.insert(atom);
      setTerm.insert(atom);
      assertTrue(setTerm.getArity()==1);
    }
    
    public void testSimpleSetReduce() throws TermWareException
    {
      SetTerm setTerm=TermWare.getInstance().getTermFactory().createSetTerm();
      AtomTerm a1=TermWare.getInstance().getTermFactory().createAtom("a1");
      AtomTerm a2=TermWare.getInstance().getTermFactory().createAtom("a2");        
      setTerm.insert(a1);
      setTerm.insert(a2);
      assertTrue(setTerm.getArity()==2);
      Term x1=TermWare.getInstance().getTermFactory().createX(0);
      Term x2=TermWare.getInstance().getTermFactory().createX(1);
      Term setPattern=TermWare.getInstance().getTermFactory().createSetPattern(x1,x2);      
      
      Substitution s = new Substitution();
      boolean r=setPattern.freeUnify(setTerm, s);
      
      assertTrue(r);
            
    }
    
    public void testSetReduce1() throws TermWareException
    {
      Term x1=TermWare.getInstance().getTermFactory().createX(0);
      Term x2=TermWare.getInstance().getTermFactory().createX(1);
      Term setPattern=TermWare.getInstance().getTermFactory().createSetPattern(x1,x2);      

      Term i1=TermWare.getInstance().getTermFactory().createInt(1);
      Term i2=TermWare.getInstance().getTermFactory().createInt(2);
      Term i3=TermWare.getInstance().getTermFactory().createInt(3);
      Term i4=TermWare.getInstance().getTermFactory().createInt(4);
      
      Term l11=TermWare.getInstance().getTermFactory().createTerm("l", i1,i1);
      Term l12=TermWare.getInstance().getTermFactory().createTerm("l", i1,i2);
      Term l13=TermWare.getInstance().getTermFactory().createTerm("l", i1,i3);
      Term l14=TermWare.getInstance().getTermFactory().createTerm("l", i1,i4);
      
      SetTerm setTerm=TermWare.getInstance().getTermFactory().createSetTerm();
      setTerm.insert(l11);
      setTerm.insert(l12);
      setTerm.insert(l13);
      setTerm.insert(l14);
      
      Substitution s = new Substitution();
      boolean r=setPattern.freeUnify(setTerm, s);
      
      assertTrue(r);
      
      Term s1=s.get(1);
      assertTrue(s1.getArity()==3);
      
      
      s=new Substitution();
      r=setPattern.freeUnify(s1, s);
      
      s1=s.get(1);
      assertTrue(s1.getArity()==2);
      
    }
    
     public void testSetReduce2() throws TermWareException
    {
        TermSystem ts1=new TermSystem(new FirstTopStrategy(),new DefaultFacts());
        ts1.addRule("{ $x: $y } -> $y");
        ts1.addRule(" { } -> END ");
        
        Term i1=TermWare.getInstance().getTermFactory().createInt(1);
        Term i2=TermWare.getInstance().getTermFactory().createInt(2);
        Term i3=TermWare.getInstance().getTermFactory().createInt(3);
        Term i4=TermWare.getInstance().getTermFactory().createInt(4);
      
        Term l11=TermWare.getInstance().getTermFactory().createTerm("l", i1,i1);
        Term l12=TermWare.getInstance().getTermFactory().createTerm("l", i1,i2);
        Term l13=TermWare.getInstance().getTermFactory().createTerm("l", i1,i3);
        Term l14=TermWare.getInstance().getTermFactory().createTerm("l", i1,i4);
      
        SetTerm setTerm=TermWare.getInstance().getTermFactory().createSetTerm();
        setTerm.insert(l11);
        setTerm.insert(l12);
        setTerm.insert(l13);
        setTerm.insert(l14);
      
        Term t=ts1.reduce(setTerm);
        
        
        assertTrue(t.isAtom() && t.getName().equals("END"));
            
    }
    
     public void testSetReduce3() throws TermWareException
    {
        TermSystem ts1=new TermSystem(new FirstTopStrategy(),new DefaultFacts());
        ts1.addRule("{ $x: $y } [ true ] -> $y");
        ts1.addRule(" { } -> END ");
        
        Term i1=TermWare.getInstance().getTermFactory().createInt(1);
        Term i2=TermWare.getInstance().getTermFactory().createInt(2);
        Term i3=TermWare.getInstance().getTermFactory().createInt(3);
        Term i4=TermWare.getInstance().getTermFactory().createInt(4);
      
        Term l11=TermWare.getInstance().getTermFactory().createTerm("l", i1,i1);
        Term l12=TermWare.getInstance().getTermFactory().createTerm("l", i1,i2);
        Term l13=TermWare.getInstance().getTermFactory().createTerm("l", i1,i3);
        Term l14=TermWare.getInstance().getTermFactory().createTerm("l", i1,i4);
      
        SetTerm setTerm=TermWare.getInstance().getTermFactory().createSetTerm();
        setTerm.insert(l11);
        setTerm.insert(l12);
        setTerm.insert(l13);
        setTerm.insert(l14);
      
        Term t=ts1.reduce(setTerm);
        
        
        assertTrue(t.isAtom() && t.getName().equals("END"));
            
    }
    
    public void testSetReduce4() throws TermWareException
    {
        TermSystem ts1=new TermSystem(new FirstTopStrategy(),new DefaultFacts());
        ts1.addRule("{ l($x,$y) : $z } -> $z");
        ts1.addRule(" { } -> END ");
        
        Term i1=TermWare.getInstance().getTermFactory().createInt(1);
        Term i2=TermWare.getInstance().getTermFactory().createInt(2);
        Term i3=TermWare.getInstance().getTermFactory().createInt(3);
        Term i4=TermWare.getInstance().getTermFactory().createInt(4);
      
        Term l11=TermWare.getInstance().getTermFactory().createTerm("l", i1,i1);
        Term l12=TermWare.getInstance().getTermFactory().createTerm("l", i1,i2);
        Term l13=TermWare.getInstance().getTermFactory().createTerm("l", i1,i3);
        Term l14=TermWare.getInstance().getTermFactory().createTerm("l", i1,i4);
      
        SetTerm setTerm=TermWare.getInstance().getTermFactory().createSetTerm();
        setTerm.insert(l11);
        setTerm.insert(l12);
        setTerm.insert(l13);
        setTerm.insert(l14);
      
        Term t=ts1.reduce(setTerm);
                
        assertTrue(t.isAtom() && t.getName().equals("END"));            
    } 
     
     
}
