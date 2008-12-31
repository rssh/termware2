/*
 * StringSystemTests.java
 *
 */

package ua.gradsoft.termwaretests.systems;

import junit.framework.*;

import ua.gradsoft.termware.*;

/**
 *Tests of string system
 * @author Ruslan Shevchenko
 */
public class StringSystemTests extends TestCase
{
    
    protected void setUp()
    { TermWare.getInstance().init(); }
    
    
    public void testResolution() throws TermWareException
    {
      TermSystem stringSystem=TermWare.getInstance().resolveSystem("String");  
      assertTrue(stringSystem!=null);
    }
    
    public void testConcatAsPlus() throws TermWareException
    {
      TermSystem stringSystem=TermWare.getInstance().resolveSystem("String");  
      Term t=TermWare.getInstance().getTermFactory().createParsedTerm("\"sss\"+\"qqq\"");
      //stringSystem.setDebugMode(true);
      //stringSystem.setDebugEntity("All");
      t=stringSystem.reduce(t);
      assertTrue("t is string",t.isString());
      assertEquals("sssqqq",t.getString());
    }
    
    public void testLength() throws TermWareException
    {
      TermSystem stringSystem=TermWare.getInstance().resolveSystem("String");  
      Term t=TermWare.getInstance().getTermFactory().createString("qqq");
      t=TermWare.getInstance().getTermFactory().createTerm("length",t);
      t=stringSystem.reduce(t);
      int len = t.getAsInt(TermWare.getInstance());
      assertEquals(3,len);
    }
    
    public void testMatches1() throws TermWareException
    {
      TermSystem stringSystem=TermWare.getInstance().resolveSystem("String");  
      Term t=TermWare.getInstance().getTermFactory().createParsedTerm("matches(\"aaa\",\"a+\")");
      t=stringSystem.reduce(t);
      assertTrue("t must be boolean",t.isBoolean());
      assertTrue("t must be true",t.getBoolean()==true);      
    }

    public void testMatches2() throws TermWareException
    {
      TermSystem stringSystem=TermWare.getInstance().resolveSystem("String");  
      Term t=TermWare.getInstance().getTermFactory().createParsedTerm("matches(\"aaa\",\"b+\")");
      t=stringSystem.reduce(t);
      assertTrue("t must be boolean",t.isBoolean());
      assertTrue("t must be false",t.getBoolean()==false);      
    }
    
    public void testMatches3() throws TermWareException
    {
      TermSystem stringSystem=TermWare.getInstance().resolveSystem("String");  
      Term t=TermWare.getInstance().getTermFactory().createParsedTerm("matches(\"aaa\",p(x))");
      t=stringSystem.reduce(t);
      assertTrue("t must be not boolean",!t.isBoolean());    
    }
    
    public void testMatches4() throws TermWareException
    {
      TermSystem stringSystem=TermWare.getInstance().resolveSystem("String");  
      //stringSystem.setDebugMode(true);
      //stringSystem.setDebugEntity("All");
      Term t=TermWare.getInstance().getTermFactory().createParsedTerm("matches(\"@ref{qqq}\",\"@ref\\\\{(\\\\w+)\\\\}\",$x1)");
      TransformationContext ctx = new TransformationContext();
      t=stringSystem.reduce(t,ctx);
      assertTrue("t must be boolean",t.isBoolean());  
      assertTrue("t must be true",t.getBoolean()==true);  
      Substitution s = ctx.getCurrentSubstitution();
      
      Term var = s.get(0);
      assertTrue("must exists free variable with index 0",var!=null);
      assertTrue("it must be string",var.isString());
      assertEquals("it must be qqq","qqq",var.getString());
    }
    
}
