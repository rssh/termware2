/*
 * String_LengthTransformer.java
 *
 * Created 10, 02, 2004, 9:06
 */

package ua.gradsoft.termware.transformers.string;


import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;


/**
 * Transformer for string.length
 *   length(s)='length of string s'
 */
public final class StringLengthTransformer extends AbstractBuildinTransformer {
    
  
    
    public String getDescription() {
        return "length of string s";
    }
    
    public String getName() {
        return "length";
    }
   
    /**
     * if <code> t </code> is string - transform one to length of string.
     */
    public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {    
      return static_transform(t,system,ctx);
    }
    
    /**
     * if <code> t </code> is string - transform one to length of string.
     */
    public static Term static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
    {
     if (t.getArity()!=1) return t;
     Term frs=t.getSubtermAt(0);
     if (!frs.isString()) return t;
     ctx.setChanged(true);
     return system.getInstance().getTermFactory().createInt(frs.getString().length());
    }

    /**
     *@return false
     */
    public boolean internalsAtFirst() {
        return false;
    }

    
}
