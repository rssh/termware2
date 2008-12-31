/*
 * WhereTransformer.java
 *
 */

package ua.gradsoft.termware.transformers.general;

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.annotations.TransformerDescription;
import ua.gradsoft.termware.annotations.TransformerName;
import ua.gradsoft.termware.util.LogHelper;

/**
 *Perform assigments
 * @author rssh
 */
@TransformerName("where")
@TransformerDescription("where(expr,assigments-list)  - perform assigments of free variables, than substitute expr")
public final class WhereTransformer extends AbstractBuildinTransformer
{
  
   private WhereTransformer()
   {}
   
   public static WhereTransformer INSTANCE = new WhereTransformer();

   public Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
   {
     if (system.isLoggingMode()) {
         LogHelper.log(system,WhereTransformer.class,"where, t=",t);
     }  
     if (t.getArity()==2) {
         Term retval = LetTransformer.static_transform_letexpr(t.getSubtermAt(1),t.getSubtermAt(0),system,ctx);
         if (system.isLoggingMode()) {
           LogHelper.log(system,WhereTransformer.class,"substitution=",ctx.getCurrentSubstitution());    
           LogHelper.log(system,WhereTransformer.class,"result=",retval);  
         }
         return retval;
     }else{
         if (system.isLoggingMode()) {
           LogHelper.log(system,WhereTransformer.class,"arity does not match, return unchanged");
         }
         return t;
     }
   }
    
    
    
    
}
