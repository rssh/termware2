/*
 * ConditionalRuleTestCase.java
 * 
 * Created on: 9, 08, 2005, 1:35
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


package ua.gradsoft.termwaretests.systems;

import junit.framework.TestCase;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

/**
 *Test case for conditional rule
 * @author Ruslan Shevchenko
 */
public class ConditionalRuleTestCase extends TestCase
{
    
    private TermSystem local_;
    
    /** Creates a new instance of ConditionalRuleTestCase */
    public ConditionalRuleTestCase(String name) {
        super(name);
    }
    
                  
    protected void setUp() throws TermWareException
    { 
        TermWare.getInstance().init();
        local_=new TermSystem(new FirstTopStrategy(),new DefaultFacts(),TermWare.getInstance());        
        TermWare.addGeneralTransformers(local_);
    }

    public void testConditions() throws TermWareException
    {
        local_.addRule(" x($x) [ $x==1 ] -> 1 \n"+
                            "| [ $x==2 ] -> 2 \n"+
                            "| [ $x==3 ] -> 3 \n" +
                            "| [ $x==4 ] -> 4 \n" +
                                       "!-> many");
        Term inTerm=TermWare.getInstance().getTermFactory().createTerm("x",1);
        
        Term outTerm=local_.reduce(inTerm);
        
        assertTrue(outTerm.getInt()==1);
        
        inTerm.setSubtermAt(0, TermWare.getInstance().getTermFactory().createInt(2));
        outTerm=local_.reduce(inTerm);
        assertTrue(outTerm.getInt()==2);
        
        inTerm.setSubtermAt(0, TermWare.getInstance().getTermFactory().createInt(3));
        outTerm=local_.reduce(inTerm);
        assertTrue(outTerm.getInt()==3);
        
        inTerm.setSubtermAt(0, TermWare.getInstance().getTermFactory().createInt(4));
        outTerm=local_.reduce(inTerm);
        assertTrue(outTerm.getInt()==4);
        
        inTerm.setSubtermAt(0, TermWare.getInstance().getTermFactory().createInt(5));
        outTerm=local_.reduce(inTerm);
        assertTrue(outTerm.getName().equals("many"));
                
    }
    
    public void  testNonNilCondition() throws TermWareException
    {
        local_.addRule("P($x) [ $x==x ] -> QQQ !-> AAA ");
        Term x = TermWare.getInstance().getTermFactory().createAtom("x");
        Term y = TermWare.getInstance().getTermFactory().createAtom("y");
        Term px = TermWare.getInstance().getTermFactory().createTerm("P",x);
        Term py = TermWare.getInstance().getTermFactory().createTerm("P",y);
        Term rpx = local_.reduce(px);
        assertTrue(rpx.getName().equals("QQQ"));
        Term rpy = local_.reduce(py);
        assertTrue(rpy.getName().equals("AAA"));
    }    
    
    public void  testNilCondition() throws TermWareException
    {
        //local_.setDebugEntity("All");
        //local_.setDebugMode(true);
        local_.addRule("P($x) [ $x==x ] -> QQQ !-> [] ");
        Term x = TermWare.getInstance().getTermFactory().createAtom("x");
        Term y = TermWare.getInstance().getTermFactory().createAtom("y");
        Term px = TermWare.getInstance().getTermFactory().createTerm("P",x);
        Term py = TermWare.getInstance().getTermFactory().createTerm("P",y);
        Term rpx = local_.reduce(px);
        assertTrue(rpx.getName().equals("QQQ"));
        Term rpy = local_.reduce(py);
        assertTrue(rpy.isNil());
    }
    
}
