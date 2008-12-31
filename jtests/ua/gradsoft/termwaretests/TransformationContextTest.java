/*
 * TransformationContextTest.java
 *
 */

package ua.gradsoft.termwaretests;

import junit.framework.*;

import ua.gradsoft.termware.*;

/**
 * Test TransformationContext
 */
public class TransformationContextTest extends TestCase
{
    
    protected void setUp()
    {
        TermWare.getInstance().init();
    }
    
    public void testSubstitution() throws TermWareException
    {
      TransformationContext ctx=new TransformationContext();
      ctx.getCurrentSubstitution().put(TermFactory.createX(0), TermFactory.createInt(4));
      assertFalse(ctx.getCurrentSubstitution().isEmpty());
    }
    
   //  public void testSwapSubstitution() throws TermWareException
   // {
   //   TransformationContext ctx=new TransformationContext();
   //   ctx.swapCurrentSubstitution(new Substitution());
   //   Substitution s = ctx.getCurrentSubstitution();
   //   s.put(TermFactory.createX(0), TermFactory.createInt(4));
   //   assertFalse(ctx.getCurrentSubstitution().isEmpty());
   // }
    
}
