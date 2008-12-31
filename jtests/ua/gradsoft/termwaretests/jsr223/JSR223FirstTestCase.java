/*
 * JSR223FirstTestCase.java
 *
 * Created on July 19, 2007, 4:56 PM
 *
 */

package ua.gradsoft.termwaretests.jsr223;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import junit.framework.TestCase;

/**
 *
 * @author rssh
 */
public class JSR223FirstTestCase extends TestCase
{
    
  protected void setUp()
  {
    ScriptEngineManager m = new ScriptEngineManager();
    scriptEngine_ = m.getEngineByName("termware");  
    if (m==null) {
        System.err.println("m==null");
    }
  }
    
  public void testX_PrintStringConstant() throws ScriptException
  {
     String outputQQQ =  scriptEngine_.getFactory().getOutputStatement("qqq");
     ScriptContext ctx = scriptEngine_.getContext();
     ByteArrayOutputStream byos = new ByteArrayOutputStream();
     PrintWriter pwr = new PrintWriter(byos);
     ctx.setWriter(pwr);
     scriptEngine_.eval(outputQQQ,ctx);
     pwr.flush();
     String output = new String(byos.toByteArray());
     assertEquals("output(qqq)","qqq",output);
  }
  
  public void testXPrintStringVar() throws ScriptException
  {      
      ScriptContext ctx = scriptEngine_.getContext();
      ByteArrayOutputStream byos = new ByteArrayOutputStream();
      PrintWriter pwr = new PrintWriter(byos);
      ctx.setWriter(pwr);     
      scriptEngine_.put("variable","AAA");
      Object o = scriptEngine_.eval("printString($variable)");
      pwr.flush();
      String output = new String(byos.toByteArray());
      assertEquals("output(variable=AAA)","AAA",output);         
  }
  
  public void testMethodCallSyntax1() throws ScriptException
  {
    ScriptEngineFactory factory = scriptEngine_.getFactory();
    
    scriptEngine_.eval("let f($x,$y) -> $x+$y ;");
    scriptEngine_.put("x",5);
    scriptEngine_.put("y",3);
    String s = factory.getMethodCallSyntax("general","f","$x","$y");  
    Object o = scriptEngine_.eval(s);
    assertTrue("o must be number", o instanceof Number);
    Number n = (Number)o;
    int sum = n.intValue();
    assertEquals("Sum must be 8",8,sum);
  }

  public static class XProvider
  {
      XProvider(int x)
      { x_=x; }
      public int getX() { return x_; } ;
      private int x_;
  }
  
  public void testMethodCallSyntax2() throws ScriptException
  {
    ScriptEngineFactory factory = scriptEngine_.getFactory();
    
    scriptEngine_.put("x",new XProvider(10));
    String s = factory.getMethodCallSyntax("$x","getX");
    //System.err.println("s="+s);
    Object o = scriptEngine_.eval(s);
    assertTrue("o must be number", o instanceof Number);
    Number n = (Number)o;
    int x = n.intValue();
    assertEquals("x must be 10",10,x);
  }

  public interface IXProvider
  {
      public int getX();
  }

  public void testMethodCallSyntax3() throws ScriptException
  {
    ScriptEngineFactory factory = scriptEngine_.getFactory();
    
    scriptEngine_.put("x",new IXProvider(){
        public int getX() { return 11; }
    }
    );
    String s = factory.getMethodCallSyntax("$x","getX");
    //System.err.println("s="+s);
    Object o = scriptEngine_.eval(s);
    assertTrue("o must be number", o instanceof Number);
    Number n = (Number)o;
    int x = n.intValue();
    assertEquals("x must be 11",11,x);
  }
  
  
  public void testSequentialProgramm() throws ScriptException
  {
      ScriptContext ctx = scriptEngine_.getContext();
      ByteArrayOutputStream byos = new ByteArrayOutputStream();
      PrintWriter pwr = new PrintWriter(byos);
      ctx.setWriter(pwr);     
      String outputQQQ =  scriptEngine_.getFactory().getOutputStatement("qqq");
      String outputAAA =  scriptEngine_.getFactory().getOutputStatement("aaa");
      String qqqaaaProgram = scriptEngine_.getFactory().getProgram(outputQQQ,outputAAA);
      Object o = scriptEngine_.eval(qqqaaaProgram);
      pwr.flush();
      String output = new String(byos.toByteArray());
      assertEquals("program(output(qqq),output(aaa))","qqqaaa",output);      
  }       
  
  public void testCompilable1() throws ScriptException
  {
      scriptEngine_.eval("let square($x) -> $x * $x");
      Compilable sce = (Compilable)scriptEngine_;      
      CompiledScript cs = sce.compile("square($a)");
      scriptEngine_.put("a",2);
      Object o = cs.eval();
      assertTrue("o must be number",o instanceof Number);
      Number no = (Number)o;
      assertEquals("value must be 4",4,no.intValue());
      scriptEngine_.put("a",3);
      o=cs.eval();
      assertTrue("o must be number",o instanceof Number);
      no = (Number)o;
      assertEquals("value must be 9",9,no.intValue());
  }
  
  public void testInvokableFunction() throws Exception
  {
      scriptEngine_.eval("let square($x) -> $x * $x");
      Invocable ise = (Invocable)scriptEngine_;
      Object o = ise.invokeFunction("square",5);
      assertTrue("o must be number",o instanceof Number);
      Number no = (Number)o;
      assertEquals("value must be 25",25,no.intValue());      
  }

   public void testInvokableMethodSys() throws Exception
  {
      scriptEngine_.eval("sys.system(myNewSystem,default,ruleset(getX()->10,getY()->11),FirstTop);");    
      Invocable ise = (Invocable)scriptEngine_;
      Object myNewSystem = scriptEngine_.eval("myNewSystem");
      Object o = ise.invokeMethod(myNewSystem,"getX");
      assertTrue("o must be number",o instanceof Number);
      Number no = (Number)o;
      assertEquals("value must be 10",10,no.intValue());      
  }
 
   public interface IPoint
   {
       int getX();
       int getY();
   }
   
   public void testGetInterface1() throws Exception
   {
      scriptEngine_.eval("sys.system(myNewSystem,default,ruleset(getX()->10,getY()->11),FirstTop);");    
      Invocable ise = (Invocable)scriptEngine_;
      Object myNewSystem = scriptEngine_.eval("myNewSystem");
      IPoint p = ise.getInterface(myNewSystem,IPoint.class);
      int x = p.getX();
      assertEquals("x must be 10",10,x);
   }
   
   public interface ISum
   {
       int sum(int x, int y);
       double sum(double x, double y);
   }
   
   public void testGetInterface2() throws Exception
   {
      scriptEngine_.eval("let sum($x,$y) -> $x+$y;");    
      Invocable ise = (Invocable)scriptEngine_;
      ISum sum = ise.getInterface(ISum.class);    
      int x = sum.sum(3,4);
      assertEquals("x must be 7",7,x);
      double dx = sum.sum(3,5.4);
      assertEquals("x must be 8.4",8.4,dx);      
   }
   
  
  private ScriptEngine scriptEngine_;
    
}
