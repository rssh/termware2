package ua.gradsoft.termware.transformers.general;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.util.LogHelper;


/**
 *Transformer for not equal
 **/
public class EqTransformer extends AbstractBuildinTransformer {
    
    public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
    {        
       //if (!t.getName().equals("eq")) return t;
        if (t.getArity()==2) {
            Term frs = t.getSubtermAt(0);
            Term snd = t.getSubtermAt(1);
            
            /**
             * at first - reduce subexpressions.
             */
            frs=system.reduce(frs,ctx);
            snd=system.reduce(snd,ctx);
            
            if (system.isLoggingMode()) {
              LogHelper.log(system, EqTransformer.class, "apply eq transformer, term=",  t);
            }

            if (frs.termCompare(snd)==0) {
                ctx.setChanged(true);
                if (system.isLoggingMode()) {
                    LogHelper.log(system,EqTransformer.class, "return true");              
                }
                return system.getInstance().getTermFactory().createBoolean(true);
            }            
        }
        if (system.isLoggingMode()) {
           LogHelper.log(system,EqTransformer.class, "return false");              
        }
        ctx.setChanged(true);
        return system.getInstance().getTermFactory().createBoolean(false);
    }
    
    
    public String getDescription() {
        return "eq(x,y) - true if x and y are equal";
    }
    
    public String getName() {
        return "eq";
    }
    
    public boolean internalsAtFirst() {
        return true;
    }
        
    
}
