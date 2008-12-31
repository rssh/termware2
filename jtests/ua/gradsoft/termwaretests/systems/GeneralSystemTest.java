/*
 * GeneralSystemTest.java
 *
 */

package ua.gradsoft.termwaretests.systems;

import junit.framework.*;
import ua.gradsoft.termware.*;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

/**
 *
 * @author Ruslan Shevchenko
 */
public class GeneralSystemTest extends TestCase
{
    private TermSystem general_;
    private TermSystem local_;
    
    protected void setUp() throws TermWareException
    { 
        TermWare.getInstance().init();
        general_=TermWare.getInstance().resolveSystem("general");
        local_=new TermSystem(new FirstTopStrategy(),new DefaultFacts(),TermWare.getInstance());
        TermWare.addGeneralTransformers(local_);  
    }
    
    protected void tearDown()
    {
        general_=null;
        local_=null;
    }
    
    public void testGeneralExists()
    {
      assertTrue(general_!=null);
    }
    
    public void testConditionRuleSuccess() throws TermWareException
    {
      local_.addRule("x($x) [ $x==1 ] -> y" );
      Term t0=TermWare.getInstance().getTermFactory().createInt(1);
      Term t1=TermWare.getInstance().getTermFactory().createTerm("x",t0);
      Term t=local_.reduce(t1);
      assertEquals("y",t.getName());
//      local_=new TermSystem(new FirstTopStrategy(),new DefaultFacts(),TermWare.getInstance());
//      TermWare.addGeneralTransformers(local_);  
    }
    
    public void testConditionRuleFail() throws TermWareException
    {
      local_.addRule("x($x) [ $x==1 ] -> y" );
      Term t0=TermWare.getInstance().getTermFactory().createInt(2);
      Term t1=TermWare.getInstance().getTermFactory().createTerm("x",t0);
      Term t=local_.reduce(t1);
      assertEquals("x",t.getName());
 //     local_=new TermSystem(new FirstTopStrategy(),new DefaultFacts(),TermWare.getInstance());
 //     TermWare.addGeneralTransformers(local_);  
    }
    
    /**
     * test condition rule with on-Fair
     */
    public void testComplexConditionRule1() throws TermWareException
    {
      local_.addRule("x($x) [ $x==1 ] -> y "+
                                    "!-> z" );  
      Term t0=TermWare.getInstance().getTermFactory().createInt(2);
      Term t1=TermWare.getInstance().getTermFactory().createTerm("x",t0);
      Term t=local_.reduce(t1);
      
     assertEquals("z",t.getName());
     
    }
    
    public void testComplexConditionRule2() throws TermWareException
    {
      local_.addRule("x($x)  [ $x==1 ] -> y \n"+
                     "     |  [ $x==2 ] -> z \n"+
                                    "!-> w \n" );  
      Term t0=TermWare.getInstance().getTermFactory().createInt(2);
      Term t1=TermWare.getInstance().getTermFactory().createTerm("x",t0);
      Term t=local_.reduce(t1);
      
      assertEquals("z",t.getName());
     
    }
    
    
    public void testIfSuccess() throws TermWareException
    {
      
      local_.addRule("x($x) -> $x==1 ? y : z" );      
      Term t0=TermWare.getInstance().getTermFactory().createInt(1);
      Term t1=TermWare.getInstance().getTermFactory().createTerm("x",t0);
      Term t=local_.reduce(t1);
      assertEquals("y",t.getName());
      local_=new TermSystem(new FirstTopStrategy(),new DefaultFacts(),TermWare.getInstance());
      TermWare.addGeneralTransformers(local_);  
    }
    
    public void testIfFail() throws TermWareException
    {
      local_.addRule("x($x) -> $x==1 ? y : z" );
      Term t0=TermWare.getInstance().getTermFactory().createInt(2);
      Term t1=TermWare.getInstance().getTermFactory().createTerm("x",t0);
     // local_.setDebugMode(true);
     // local_.setDebugEntity("All");
      Term t=local_.reduce(t1);
      assertEquals("z",t.getName());
      local_=new TermSystem(new FirstTopStrategy(),new DefaultFacts(),TermWare.getInstance());
      TermWare.addGeneralTransformers(local_);  
    }
    
    
    public void testTermNameAndArity1() throws TermWareException
    {
      Term t0=TermWare.getInstance().getTermFactory().createTerm("a","b");
      Term t=TermWare.getInstance().getTermFactory().createTerm("term_name",t0);
      
      t=general_.reduce(t);
      
      assertTrue("term_name return string",t.isString());
      assertEquals("name of a(b)","a",t.getString());
      
      t=TermWare.getInstance().getTermFactory().createTerm("arity",t0);
      t=general_.reduce(t);
      assertTrue("arity return int",t.isInt());
      assertEquals("arity of a(b)",1,t.getInt());
      
    }
  
    public void testIs1() throws TermWareException
    {
     Term t0=TermWare.getInstance().getTermFactory().createX(0);
     Term t1=TermWare.getInstance().getTermFactory().createTerm("isX",t0);
     Term t=general_.reduce(t1);
     assertTrue("isX on x",t.getBoolean());
     
     t0=TermWare.getInstance().getTermFactory().createAtom("qqq");
     t1=TermWare.getInstance().getTermFactory().createTerm("isX",t0);
     t=general_.reduce(t1);
     assertFalse("isX on atom",t.getBoolean());
     
     t1=TermWare.getInstance().getTermFactory().createTerm("isAtom",t0);
     t=general_.reduce(t1);
     assertTrue("isAtom on atom",t.getBoolean());
     
    }
    
    public void  testToBoolean() throws TermWareException
    {
      Term t0=TermWare.getInstance().getTermFactory().createInt(0);  
      Term t1=TermWare.getInstance().getTermFactory().createTerm("toBoolean",t0);
      Term t=general_.reduce(t1);
    }
    
    public void  testToBigDecimal() throws TermWareException
    {
      Term t0=TermWare.getInstance().getTermFactory().createInt(10);    
      Term t1=TermWare.getInstance().getTermFactory().createTerm("toBigDecimal",t0);
      Term t=general_.reduce(t1);
      assertTrue("toBigDecimal on int",t.isBigDecimal());
    }
    
    public void  testToString() throws TermWareException
    {
      Term t0=TermWare.getInstance().getTermFactory().createInt(10);    
      Term t1=TermWare.getInstance().getTermFactory().createTerm("toString",t0);
      Term t=general_.reduce(t1);
      assertTrue("toString on int",t.isString());
    }
    
    
}
