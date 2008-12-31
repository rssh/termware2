package ua.gradsoft.termware.transformers.general;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002,2003
 * $Id: TermNameTransformer.java,v 1.2 2007-06-29 16:34:56 rssh Exp $
 */

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;



/**
 * name(t(x1..xn)) = t
 **/
public class TermNameTransformer extends AbstractBuildinTransformer {
    
    
    public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx); }
    
    
    static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        //if (!t.getName().equals("term_name")) return t;
        if (t.getArity()!=1) {
            return t;
        }
        ctx.setChanged(true);
        return TermFactory.createString(t.getSubtermAt(0).getName());
    }
    
    
    public String getDescription() {
        return "name of functional symbol of term";
    }
    
    public String getName() {
        return "term_name";
    }
    
   
    
}
