/*
 * GeneralSystemMinusTest.java
 *
 * Created 10/01/2005, 8:12
 */

package ua.gradsoft.termwaretests.systems;


import java.math.*;
import junit.framework.*;
import ua.gradsoft.termware.*;



/**
 *Test 'minus' default transformer
 * @author Ruslan Shevchenko
 */
public class GeneralSystemMinusTest extends TestCase
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
    
    public void testInt1() throws TermWareException
    {
        Term frs=TermFactory.createInt(1);
        Term minusTerm=TermWare.getInstance().getTermFactory().createTerm("minus",frs);
        
        Term t=general_.reduce(minusTerm);
        
        assertEquals(-1,t.getInt());
    }
   
    public void testBigDecimalBigDecimal1()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createBigDecimal(BigDecimal.valueOf(7));
      Term snd=TermWare.getInstance().getTermFactory().createBigDecimal(BigDecimal.valueOf(7));
      Term minusTerm=TermWare.getInstance().getTermFactory().createTerm("minus",frs,snd);
      
      Term t=general_.reduce(minusTerm);
      
      //assertTrue(t.isBigDecimal());
      assertEquals(0,t.getAsInt(TermWare.getInstance()));
      
    }

    public void testBigDecimalBigDecimal2()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createBigDecimal(BigDecimal.valueOf(7));
      Term snd=TermWare.getInstance().getTermFactory().createBigDecimal(BigDecimal.valueOf(8));
      Term minusTerm=TermWare.getInstance().getTermFactory().createTerm("minus",frs,snd);
      
      Term t=general_.reduce(minusTerm);
      
      //assertTrue(t.isBigDecimal());
      assertEquals(-1,t.getAsInt(TermWare.getInstance()));
      
    }

    public void testDoubleInt()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createDouble(5.0);
      Term snd=TermWare.getInstance().getTermFactory().createInt(3);
      Term minusTerm=TermWare.getInstance().getTermFactory().createTerm("minus",frs,snd);
      
      Term t=general_.reduce(minusTerm);
      
      //assertTrue(t.isBigDecimal());
      assertEquals(2,t.getAsInt(TermWare.getInstance()));      
    }
    
    public void testDoubleDouble()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createDouble(5.0);
      Term snd=TermWare.getInstance().getTermFactory().createDouble(6.0);
      Term minusTerm=TermWare.getInstance().getTermFactory().createTerm("minus",frs,snd);
      
      Term t=general_.reduce(minusTerm);
      
      assertTrue(t.isDouble());
      assertEquals(-1,t.getAsInt(TermWare.getInstance()));      
    }
    
    public void testShortShort()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createShort((short)10);
      Term snd=TermWare.getInstance().getTermFactory().createShort((short)11);
      Term minusTerm=TermWare.getInstance().getTermFactory().createTerm("minus",frs,snd);
      
      Term t=general_.reduce(minusTerm);
      
      assertTrue(t.isInt());
      assertEquals(-1,t.getAsInt(TermWare.getInstance()));      
    }
    
    
}
