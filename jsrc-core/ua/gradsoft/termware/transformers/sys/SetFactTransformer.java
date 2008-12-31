package ua.gradsoft.termware.transformers.sys;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002,2003, 2004, 2005
 * $Id: SetFactTransformer.java,v 1.1 2005-01-09 19:28:21 rssh Exp $
 */

import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.util.*;
import ua.gradsoft.termware.exceptions.*;


/**
 *Transformer for setFacts
 *
 *setFacts(t1..tn) - reduced to true, with effect of setting into knowledge database
 * of current system facts t1..tn.
 *
 *@see IFacts
 **/
public class SetFactTransformer extends AbstractBuildinTransformer {
    
    /**
     *@return false
     */
    public boolean internalsAtFirst() {
        return false;
    }
    
    public  Term  transform(Term t, TermSystem sys, TransformationContext ctx) throws TermWareException {
        return static_transform(t,sys,ctx); }
    
    
    static public  Term  static_transform(Term t, TermSystem sys, TransformationContext ctx) throws TermWareException {
        if (!t.getName().equals("setFact")) return t;
        if (t.getArity()<2) {
            return t;
        }
        IFacts facts = sys.getInstance().getFacts(t.getSubtermAt(0));
        for(int i=1; i<t.getArity(); ++i) {
            facts.set(t.getSubtermAt(i),ctx);
        }
        ctx.setChanged(true);
        return TermFactory.createBoolean(true);
    }
    
    public String getDescription() {
        return staticDescription_;
    }
    
    public String getName() {
        return "setFacts";
    }
    
    private final static String staticDescription_=
            "setFacts(t1..tn) - reduced to true, with effect of setting into knowledge database "+
            " of current system facts t1..tn. "
            ;
    
    
}
