/*
 * GeneralSystemMultiplyTest.java
 *
 */

package ua.gradsoft.termwaretests.systems;

import java.math.*;
import junit.framework.*;
import ua.gradsoft.termware.*;

/**
 *
 * @author Ruslan Shevchenko
 */
public class GeneralSystemMultiplyTest extends TestCase
{
    
 private TermSystem general_;
    
    protected void setUp() throws TermWareException
    { 
        TermWare.getInstance().init();
        general_=TermWare.getInstance().resolveSystem("general");
    }
    
    protected void tearDown()
    {
        general_=null;
    }
  
    
    public void testMultiplyBigDecimalBigDecimal1()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createBigDecimal(BigDecimal.valueOf(1));
      Term snd=TermWare.getInstance().getTermFactory().createBigDecimal(BigDecimal.valueOf(2));
      Term multiplyTerm=TermWare.getInstance().getTermFactory().createTerm("multiply",frs,snd);
      
      Term t=general_.reduce(multiplyTerm);
      
      assertTrue(t.isBigDecimal());
      assertEquals(2,t.getAsInt(TermWare.getInstance()));
      
    }
    
    
    public void testMultiplyBigDecimalBigDecimal2()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createBigDecimal(new BigDecimal(0.1));
      Term snd=TermWare.getInstance().getTermFactory().createBigDecimal(new BigDecimal(10.0));
      Term multiplyTerm=TermWare.getInstance().getTermFactory().createTerm("multiply",frs,snd);
      
      Term t=general_.reduce(multiplyTerm);
      
      assertTrue(t.isBigDecimal());
      assertEquals(1,t.getAsInt(TermWare.getInstance()));
      
    }
    
    
    public void testMultiplyBigDecimalDouble()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createBigDecimal(new BigDecimal(0.1));
      Term snd=TermWare.getInstance().getTermFactory().createDouble(10.0);
      Term multiplyTerm=TermWare.getInstance().getTermFactory().createTerm("multiply",frs,snd);
      
      Term t=general_.reduce(multiplyTerm);
      
      assertTrue(t.isBigDecimal());
      assertEquals(1,t.getAsInt(TermWare.getInstance()));
      
    }
    
    
    
    public void testShortShort()  throws TermWareException
    {
        
      
      Term frs=TermWare.getInstance().getTermFactory().createShort((short)100);
      Term snd=TermWare.getInstance().getTermFactory().createShort((short)20);
      Term multiplyTerm=TermWare.getInstance().getTermFactory().createTerm("multiply",frs,snd);
      
     // general_.setDebugMode(true);
     // general_.setDebugEntity("All");
      Term t=general_.reduce(multiplyTerm);
      
      assertTrue(t.isInt());
      assertEquals(2000,t.getAsInt(TermWare.getInstance()));
      
    }
     
    public void testIntShort()  throws TermWareException
    {
        
      
      Term frs=TermWare.getInstance().getTermFactory().createInt(100);
      Term snd=TermWare.getInstance().getTermFactory().createShort((short)20);
      Term multiplyTerm=TermWare.getInstance().getTermFactory().createTerm("multiply",frs,snd);
      
      //general_.setDebugMode(true);
      //general_.setDebugEntity("All");
      Term t=general_.reduce(multiplyTerm);
      
      assertTrue(t.isInt());
      assertEquals(2000,t.getAsInt(TermWare.getInstance()));
      
    }
    
    
}
