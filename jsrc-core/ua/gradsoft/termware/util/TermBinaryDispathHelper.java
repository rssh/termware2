/*
 * DispathByPrimaryType0Helper.java
 *
 */

package ua.gradsoft.termware.util;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermWareException;

/**
 *Helper for dispath.
 * @author Ruslan Shevchenko
 */
public class TermBinaryDispathHelper
{
    
   public static Term doDispath(TermBinaryDispath x, Term frs,Term snd, Term origin) throws TermWareException
   {
      TermBinaryFunction fun=x.dispath(frs, snd); 
      if (fun==null) {
          return origin;
      }else{
          return fun.run(frs, snd);
      }
   }
    
   static class FrsTermBinaryFunction implements TermBinaryFunction
   {
       
       public Term run(Term frs, Term snd)
       { return frs; }
       
   }
   
   static class SndTermBinaryFunction implements TermBinaryFunction
   {
       
       public Term run(Term frs, Term snd)
       { return snd; }
       
   }
   
   static class NilTermBinaryFunction implements TermBinaryFunction
   {
       
       public Term run(Term frs, Term snd)
       { return TermFactory.createNIL(); }
       
   }
   
   
   
   
   public final TermBinaryFunction FFRS = new FrsTermBinaryFunction();
   public final TermBinaryFunction FSND = new FrsTermBinaryFunction();
   public final TermBinaryFunction FNIL = new NilTermBinaryFunction();
   
}
