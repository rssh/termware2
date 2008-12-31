
package ua.gradsoft.termware.transformers.sys;

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.LongTerm;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;

/**
 *Transformer for sys::getCurrentTimeMillis - return number of milliseconds since midnight GMT on January 1st, 1970
 * @author rssh
 */
public class GetCurrentTimeInMillisTransformer extends AbstractBuildinTransformer
{

  private GetCurrentTimeInMillisTransformer()
  {}
    
  public static  GetCurrentTimeInMillisTransformer INSTANCE_ = new GetCurrentTimeInMillisTransformer();
    
 public  Term  transform(Term t, TermSystem sys, TransformationContext ctx) throws TermWareException
  { return static_transform(t,sys,ctx); }
 

 static public  Term  static_transform(Term t, TermSystem sys,  TransformationContext ctx) throws TermWareException
 {
     long l = System.currentTimeMillis();
     ctx.setChanged(true);
     return new LongTerm(l);
 }

 
 public String getDescription() {
     return staticDescription_;
 }
 
 /**
  * @return "getCurrentTimeMillis"
  */
 public String getName() {
     return "getCurrentTimeMillis";
 }
 
 private static final String staticDescription_ =
     "getCurrentTimeMillis - return number of milliseconds since midnight GMT on January 1st, 1970";
    
    
    
}
