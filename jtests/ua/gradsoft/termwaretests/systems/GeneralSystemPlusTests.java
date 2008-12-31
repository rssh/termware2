/*
 * GeneralSystemPlusTests.java
 *
 */

package ua.gradsoft.termwaretests.systems;


import java.math.*;
import junit.framework.*;
import ua.gradsoft.termware.*;



/**
 *Test 'plus' general transformer
 * @author Ruslan Shevchenko
 */
public class GeneralSystemPlusTests extends TestCase
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
    
   
    public void testPlusBigDecimalBigDecimal()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createBigDecimal(BigDecimal.valueOf(1));
      Term snd=TermWare.getInstance().getTermFactory().createBigDecimal(BigDecimal.valueOf(2));
      Term plusTerm=TermWare.getInstance().getTermFactory().createTerm("plus",frs,snd);
      
      Term t=general_.reduce(plusTerm);
      
      assertTrue(t.isBigDecimal());
      assertEquals(3,t.getAsInt(TermWare.getInstance()));
      
    }
    
    public void testPlusBigDecimalBigInteger() throws TermWareException
    {
      Term frs=TermWare.getInstance().getTermFactory().createBigDecimal(BigDecimal.valueOf(1));
      Term snd=TermWare.getInstance().getTermFactory().createBigInteger(BigInteger.valueOf(2));
      Term plusTerm=TermWare.getInstance().getTermFactory().createTerm("plus",frs,snd);
      
      Term t=general_.reduce(plusTerm);
      
      assertTrue(t.isBigDecimal());
      assertEquals(3,t.getAsInt(TermWare.getInstance()));
      
        
    }
    
    
    public void testPlusBigDecimalInt()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createBigDecimal(BigDecimal.valueOf(1));
      Term snd=TermWare.getInstance().getTermFactory().createInt(2);
      Term plusTerm=TermWare.getInstance().getTermFactory().createTerm("plus",frs,snd);
      
      Term t=general_.reduce(plusTerm);
      
      assertTrue(t.isBigDecimal());
      assertEquals(3,t.getAsInt(TermWare.getInstance()));
      
    }
    
    public void testPlusIntInt()  throws TermWareException
    {
      
      Term frs=TermWare.getInstance().getTermFactory().createInt(1);
      Term snd=TermWare.getInstance().getTermFactory().createInt(2);
      Term plusTerm=TermWare.getInstance().getTermFactory().createTerm("plus",frs,snd);
      
      Term t=general_.reduce(plusTerm);
      
      assertTrue(t.isInt());
      assertEquals(3,t.getInt());
      
    }
    
    public void testPlusLongInt() throws TermWareException
    {
      Term frs=TermWare.getInstance().getTermFactory().createLong(12345678L);
      Term snd=TermWare.getInstance().getTermFactory().createInt(1);
      Term plusTerm=TermWare.getInstance().getTermFactory().createTerm("plus",frs,snd);
      
      Term t=general_.reduce(plusTerm);
      
      assertTrue(t.isLong());
      assertEquals(12345679L,t.getLong());        
    }

    public void testPlusIntLong() throws TermWareException
    {
      Term frs=TermWare.getInstance().getTermFactory().createInt(1);
      Term snd=TermWare.getInstance().getTermFactory().createLong(12345678L);
      Term plusTerm=TermWare.getInstance().getTermFactory().createTerm("plus",frs,snd);
      
      Term t=general_.reduce(plusTerm);
      
      assertTrue(t.isLong());
      assertEquals(12345679L,t.getLong());        
    }
    
   
}
