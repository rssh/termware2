/*
 * GeneralSystemDivideTest.java
 *
 */

package ua.gradsoft.termwaretests.systems;


import java.math.*;
import junit.framework.*;
import ua.gradsoft.termware.*;


/**
 * Test various divide things.
 * @author Ruslan Shevchenko
 */
public class GeneralSystemDivideTest extends TestCase
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
 
    public void testBigDecimalBigDecimal1()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createBigDecimal(BigDecimal.valueOf(1));
      Term snd=TermWare.getInstance().getTermFactory().createBigDecimal(BigDecimal.valueOf(2));
      Term divideTerm=TermWare.getInstance().getTermFactory().createTerm("divide",frs,snd);
      
      Term t=general_.reduce(divideTerm);
      
      assertTrue(t.isBigDecimal());
      
      //System.err.println("1/2="+t.getAsDouble(TermWare.getInstance()));
      
      double delta=Math.abs(0.5-t.getAsDouble(TermWare.getInstance()));
      
      assertTrue(delta < 1e-10);
      
    }
    
    public void testBigIntegerBigDecimal1()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createBigInteger(BigInteger.ONE);
      Term snd=TermWare.getInstance().getTermFactory().createBigDecimal(new BigDecimal(2.0)/*BigDecimal.valueOf(2)*/);
      Term divideTerm=TermWare.getInstance().getTermFactory().createTerm("divide",frs,snd);
      
      Term t=general_.reduce(divideTerm);
      
      assertTrue(t.isBigDecimal());
     
      
      double delta=Math.abs(0.5-t.getAsDouble(TermWare.getInstance()));
      
      assertTrue(delta < 1e-10);
      
    }
    
    
    public void testDoubleBigDecimal()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createDouble(100);
      Term snd=TermWare.getInstance().getTermFactory().createBigDecimal(BigDecimal.valueOf(2));
      Term divideTerm=TermWare.getInstance().getTermFactory().createTerm("divide",frs,snd);
      
      Term t=general_.reduce(divideTerm);
      
      assertTrue(t.isBigDecimal());
      assertEquals(50,t.getAsInt(TermWare.getInstance()));
      
    }
    
    public void testShortShort()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createShort((short)(9));
      Term snd=TermWare.getInstance().getTermFactory().createShort((short)3);
      Term divideTerm=TermWare.getInstance().getTermFactory().createTerm("divide",frs,snd);
      
      Term t=general_.reduce(divideTerm);
      
      assertTrue(t.isInt());
      assertEquals(3,t.getAsInt(TermWare.getInstance()));
      
    }
    
    public void testIntInt()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createInt(9);
      Term snd=TermWare.getInstance().getTermFactory().createInt(3);
      Term divideTerm=TermWare.getInstance().getTermFactory().createTerm("divide",frs,snd);
      
      Term t=general_.reduce(divideTerm);
      
      assertTrue(t.isInt());
      assertEquals(3,t.getAsInt(TermWare.getInstance()));
      
    }
    
    
}
