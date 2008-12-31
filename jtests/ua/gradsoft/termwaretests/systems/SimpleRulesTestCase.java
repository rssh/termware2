/*
 * SimpleRulesTestCase.java
 *
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
 *TestCase, which illustrate few simple rules.
 * @author RSSH
 */
public class SimpleRulesTestCase extends TestCase
{
        private TermSystem local_;
    
    /** Creates a new instance of ConditionalRuleTestCase */
    public SimpleRulesTestCase(String name) {
        super(name);
    }
    
                  
    protected void setUp() throws TermWareException
    { 
        TermWare.getInstance().init();
        local_=new TermSystem(new FirstTopStrategy(),new DefaultFacts(),TermWare.getInstance());  
        TermWare.addGeneralTransformers(local_);
    }

    public void  testCheckEmpty1() throws TermWareException
    {
        local_.addRule("CheckEmpty(pair($x,$y)) -> CheckEmpty($x) && $y");
        local_.addRule("CheckEmpty(NIL) -> true");
        local_.addRule("CheckEmpty([$x:$y]) -> false");
        
        local_.addRule("pair($x,$y) -> CheckEmpty(pair($x,$y))");
        
        //local_.setDebugEntity("All");
        //local_.setDebugMode(true);
        
        Term t1 = TermWare.getInstance().getTermFactory().createParsedTerm("pair([],true)");
        
        Term tr = local_.reduce(t1);
        
        assertTrue(tr.isBoolean());
        assertTrue(tr.getBoolean());
    }
    
    public void  testCheckEmpty2() throws TermWareException
    {
        local_.addRule("CheckEmpty(pair($x,$y)) -> CheckEmpty($x) && $y");
        local_.addRule("CheckEmpty(NIL) -> true");
        
        Term t1 = TermWare.getInstance().getTermFactory().createParsedTerm("CheckEmpty(pair(NIL,true))");

        Term tr = local_.reduce(t1);
        
        assertTrue(tr.isBoolean());
        assertTrue(tr.getBoolean());
        
    }
    
    public void  testCheckEmpty3() throws TermWareException
    {
        local_.addRule("CheckEmpty(pair($x,$y)) -> CheckEmpty($x) && $y");
        local_.addRule("CheckEmpty(NIL) -> true");
        
        Term t1 = TermWare.getInstance().getTermFactory().createParsedTerm("CheckEmpty(List.append(NIL,NIL))");

        Term tr = local_.reduce(t1);
        
        assertTrue(tr.isBoolean());
        assertTrue(tr.getBoolean());
        
    }
    
    
}
