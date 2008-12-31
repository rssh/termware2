/*
 * ParseTest.java
 *
 */

package ua.gradsoft.termwaretests;

import junit.framework.TestCase;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;

/**
 *Various tests for parser.
 * @author Ruslan Shevchenko
 */
public class ParseTest extends TestCase
{
    
    /** Creates a new instance of ParseTest */
    public ParseTest(String name) {
        super(name);
    }
    
    /**
     * test, that apply is right-associative operation.
     *i.e. x.y.z = apply(x,apply(y,z))
     */
    public void testApply1() throws TermWareException
    {
        Term t=TermWare.getInstance().getTermFactory().createParsedTerm("x.y.z.v.w");
        //System.err.print("x.y.z.v.w is ");
        //t.println(System.err);
        assertTrue(t.getName().equals("apply"));
        assertTrue(t.getSubtermAt(1).getName().equals("w"));
        Term t1=t.getSubtermAt(0);
        assertTrue(t1.getName().equals("apply"));
        assertTrue(t1.getSubtermAt(1).getName().equals("v"));
        Term t2=t1.getSubtermAt(0);
        assertTrue(t2.getName().equals("apply"));
        assertTrue(t2.getSubtermAt(1).getName().equals("z"));
        Term t3=t2.getSubtermAt(0);
        assertTrue(t3.getSubtermAt(0).getName().equals("x"));
        assertTrue(t3.getSubtermAt(1).getName().equals("y"));
    }

    /**
     * test, that plus is left-associative operation.
     *i.e. x+y+z = plus(plus(x,y),z)
     */
    public void testPlus1() throws TermWareException
    {
        Term t=TermWare.getInstance().getTermFactory().createParsedTerm("x+y+z+v+w");
        //System.err.print("x+y+z+v+w is ");
        //t.println(System.err);
        assertTrue(t.getName().equals("plus"));
        assertTrue(t.getSubtermAt(0).getName().equals("plus"));
        assertTrue(t.getSubtermAt(1).getName().equals("w"));
        Term t1=t.getSubtermAt(0);
        assertTrue(t1.getName().equals("plus"));
        assertTrue(t1.getSubtermAt(1).getName().equals("v"));
        Term t2=t1.getSubtermAt(0);
        assertTrue(t2.getName().equals("plus"));
        assertTrue(t2.getSubtermAt(1).getName().equals("z"));
        Term t3=t2.getSubtermAt(0);
        assertTrue(t3.getSubtermAt(0).getName().equals("x"));
        assertTrue(t3.getSubtermAt(1).getName().equals("y"));
    }
    
    public void testParseNumbers() throws TermWareException
    {
      Term t1=TermWare.getInstance().getTermFactory().createParsedTerm("1");  
      assertTrue("t1 must be integer",t1.isInt());
      Term t2=TermWare.getInstance().getTermFactory().createParsedTerm("1L");  
      assertTrue("t2 must be long",t2.isLong());
      Term t3=TermWare.getInstance().getTermFactory().createParsedTerm("1B");  
      assertTrue("t3 must be big integer",t3.isBigInteger());
      
      Term t4=TermWare.getInstance().getTermFactory().createParsedTerm("1.1");
      assertTrue("t4 must be double",t4.isDouble());
      
      Term t5=TermWare.getInstance().getTermFactory().createParsedTerm("1.1B");
      assertTrue("t5 must be big decimal",t5.isBigDecimal());
      
      Term t6=TermWare.getInstance().getTermFactory().createParsedTerm("1.1F");
      assertTrue("t6 must be float",t6.isFloat());

      Term t7=TermWare.getInstance().getTermFactory().createParsedTerm("1.1D");
      assertTrue("t7 must be double",t7.isDouble());
      
      
    }
    
}
