/*
 * Number2StringTestCase.java
 *
 * Created on July 24, 2007, 6:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.termwaretests.systems;

import junit.framework.TestCase;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;

/**
 *
 * @author rssh
 */
public class Number2StringTestCase extends TestCase
{
    
   protected void setUp()
   {
     TermWare.getInstance().init();     
   }
   
   public void test123456789() throws Exception
   {
      TermSystem ts = TermWare.getInstance().getOrCreateDomain("examples").resolveSystem("Number2String");
      Term t = TermWare.getInstance().getTermFactory().createParsedTerm("p(123456789)");
      t=ts.reduce(t);
      assertTrue("t must be string",t.isString());
      assertTrue("t must ends with nine",t.getString().endsWith("nine"));
   }
   
}
