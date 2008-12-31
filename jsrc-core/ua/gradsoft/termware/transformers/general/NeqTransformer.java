package ua.gradsoft.termware.transformers.general;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.util.LogHelper;


/**
 *Transformer for not equal
 **/
public class NeqTransformer extends AbstractBuildinTransformer {
    
    public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
    {        
       //if (!t.getName().equals("neq")) return t;
        if (t.getArity()==2) {
            Term frs = t.getSubtermAt(0);
            Term snd = t.getSubtermAt(1);
                                    
            if (system.isLoggingMode()) {
                LogHelper.log(system,NeqTransformer.class,"apply neq for ", t);
            }
            
            // reduce subexpression
            frs = system.reduce(frs);
            snd = system.reduce(snd);
            
            //if (TermHelper.isPrimitive(frs) && TermHelper.isPrimitive(snd)) {
            if (frs.termCompare(snd)!=0) {
                ctx.setChanged(true);
                if (system.isLoggingMode()) {
                    LogHelper.log(system,NeqTransformer.class,"return true");
                }
                return TermFactory.createBoolean(true);
            }else{
                ctx.setChanged(true);  
                if (system.isLoggingMode()) {
                    LogHelper.log(system,NeqTransformer.class,"return false");
                }
                return TermFactory.createBoolean(false);
            }
            //}
        }else{ //(t.getArity()!=2) {
            throw new AssertException("neq must have arity 2");
        }
        //if (sys.isLoggingMode()) {
        //  sys.getEnv().getLog().println("unchanged");
        //}
    }
    
    
    public String getDescription() {
        return "neq(x,y) - true if x and y are not equal";
    }
    
    public String getName() {
        return "neq";
    }
    
    public boolean internalsAtFirst() {
        return false;
    }
    
    
    
}
