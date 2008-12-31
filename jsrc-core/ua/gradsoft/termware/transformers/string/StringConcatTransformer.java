/*
 * StringConcatTransformer.java
 *
 */

package ua.gradsoft.termware.transformers.string;


import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.util.LogHelper;

/**
 *Transformer for string::concat
 */
public final class StringConcatTransformer extends AbstractBuildinTransformer {

    public static final StringConcatTransformer INSTANCE = new StringConcatTransformer();
    
    private StringConcatTransformer() {}
    
    public String getDescription() {
        return "concat strings 'x' and 'y'";
    }
    
    public String getName() {
        return "concat";
    }
    
    public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx);
    }
    
    
    public static Term static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException 
    {
       if (system.isLoggingMode()) {
           LogHelper.log(system, StringConcatTransformer.class, "String::concat transformer for ", t);
       }
       StringBuffer sb=new StringBuffer();
       for(int i=0; i<t.getArity(); ++i) {
         if (t.getSubtermAt(i).isString()) {
            sb.append(t.getSubtermAt(i).getString());
         }else{
            if (system.isLoggingMode()) {
               LogHelper.log(system, StringConcatTransformer.class, "String::concat unchanded");                
            } 
            return t;
         }
       }
       ctx.setChanged(true);
       Term retval = system.getInstance().getTermFactory().createString(sb.toString());
       if (system.isLoggingMode()) {
          LogHelper.log(system, StringConcatTransformer.class, "String::concat return ",retval);                           
       }
       return retval;
    }
      
    
}
