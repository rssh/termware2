/*
 * UnifyTransformer.java
 *
 * (C) Grad-Soft Ltd, 2007, 2008
 */

package ua.gradsoft.termware.transformers.general;

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Substitution;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.util.LogHelper;

/**
 *unify(x,y) - do unification of x with y, filling current 
 *substitution if necessory. On success - fill substitution and
 * reduce to true, otherwise - reduce to false.
 */
public class UnifyTransformer extends AbstractBuildinTransformer
{
    
    /** Creates a new instance of UnifyTransformer */
    private UnifyTransformer() {
    }
    
    public static UnifyTransformer INSTANCE = new UnifyTransformer();

    public String getName()
    { return "unify"; }
    
    public String getDescription()
    { return  "unify(x,y) - do unification of x with y, filling current "+
              "substitution if necessory. On success - fill substitution and "+
              " reduce to true, otherwise - reduce to false. ";
    }
    
     public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return staticTransform(t,system,ctx); 
     }
    
    
     public  Term  staticTransform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        if (system.isLoggingMode()) { 
           LogHelper.log(system,UnifyTransformer.class,"unify, t=",t);
        }
        if (t.getArity()!=2) {
            if (system.isLoggingMode()) {
              LogHelper.log(system,UnifyTransformer.class,"arity!=2, unchanged");
            }
            return t;
        }else{
            Term frs = t.getSubtermAt(0);
            Term snd = t.getSubtermAt(1);
            Substitution s = new Substitution();
            Term sfrs = frs.subst(ctx.getCurrentSubstitution());
            Term ssnd = snd.subst(ctx.getCurrentSubstitution());
            boolean retval = sfrs.boundUnify(ssnd,s);
            if (retval) {
                if (!s.isEmpty()) {
                    ctx.getCurrentSubstitution().merge(s);
                }
            }
            if (system.isLoggingMode()) {
                LogHelper.log(system,UnifyTransformer.class,"changed, retval="+retval);
            }
            ctx.setChanged(true);
            return TermWare.getInstance().getTermFactory().createBoolean(retval);
        }
        
     }
     
  
    
    
}
