/*
 * CloneTransformer.java
 *
 * Created on June 29, 2007, 7:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.termware.transformers.general;

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;

/**
 *Transformer, which clone current term.
 *<pre>
 *  clone(x) - return term clone (i.e. deep copy) of <code>x</code>
 *</pre>
 */
public class CloneTransformer extends AbstractBuildinTransformer
{
    
    public static CloneTransformer INSTANCE = new CloneTransformer();
    
    private CloneTransformer() {
    }
    
      public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx); }

    static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException 
    {
      Term retval=t;  
      if (t.getArity()==1) {
          retval=t.getSubtermAt(0).termClone();
          ctx.setChanged(true);
      }
      return retval;
    }
 
     public String getName() {
        return "clone";
    }
    
    public String getDescription() {
        return staticDescription_;
    }
    
    
    static String staticDescription_=
             "  clone(x) - return term clone (i.e. deep copy) of <code>x</code>.\n";      
      
    
}
