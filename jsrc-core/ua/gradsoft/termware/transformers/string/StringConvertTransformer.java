/*
 * StringConvertTransformer.java
 *
 */

package ua.gradsoft.termware.transformers.string;


import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;


/**
 *Transformer for String::convert - convert argument to string representation,
 *  (by call ITermHelper.termToString)
 * @author  Ruslan Shevchenko
 */
public final class StringConvertTransformer extends AbstractBuildinTransformer {
    
    
    public boolean internalsAtFirst()
    { return false; }
    
    public String getDescription() {
        return "convert term to string representation (i. e. print one to string)";
    }
    
    public String getName() {
        return "convert";
    }
    
    public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException 
    {
        return static_transform(t,system,ctx);
    }
    
    
    public static Term static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException 
    {
      t=system.getInstance().getTermFactory().createString(TermHelper.termToString(t));
      ctx.setChanged(true);
      return t;
    }
    
}
