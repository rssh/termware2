/*
 * PrintStringTransformer.java
 *
 */

package ua.gradsoft.termware.transformers.general;

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.annotations.TransformerDescription;
import ua.gradsoft.termware.annotations.TransformerName;

/**
 *
 * @author rssh
 */
@TransformerName("printString")
@TransformerDescription("printString($x) - print string on system.out and always return true")
public class PrintStringTransformer extends AbstractBuildinTransformer {
    
    /** Creates a new instance of PrintStringTransformer */
    private PrintStringTransformer() {
    }
    
    public static final PrintStringTransformer INSTANCE = new PrintStringTransformer();

    public Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx);    
    }
    
    
    static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        //if (!t.getName().equals("term_name")) return t;
        if (t.getArity()!=1) {
            return t;
        }
        Term arg=t.getSubtermAt(0);
        if (arg.isString()) {
            ctx.getEnv().getOutput().print(arg.getString());
        }else{
            ctx.getEnv().getOutput().print(TermHelper.termToString(arg));
        }
        ctx.setChanged(true);
        return TermFactory.createBoolean(true);
    }
    
    
    
}
