/*
 * IsListTransformer.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.transformers.general;

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareSymbols;
import ua.gradsoft.termware.TransformationContext;

/**
 *IsList transformer:
 *<code>
 *  isList(cons($x,$y)) -> true
 *  isList(NIL) -> true
 *otherwise  isList($x) -> false.
 *</code>
 * @author Ruslan Shevchenko
 */
public final class IsListTransformer extends AbstractBuildinTransformer {
    
    private IsListTransformer() {}
    
    public static final IsListTransformer INSTANCE = new IsListTransformer();
    
    public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx); }
    
    
    static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        if (t.getArity()!=1) {
            return t;
        }        
        ctx.setChanged(true);
        Term arg=t.getSubtermAt(0);
        if (arg.isNil()) {
            return TermFactory.createBoolean(true);
        }else if (arg.isComplexTerm()) {
            return TermFactory.createBoolean(arg.getNameIndex().equals(TermWareSymbols.CONS_INDEX));
        }
        return TermFactory.createBoolean(false);
    }
    
    
    public String getDescription() {
        return "isList(x) = true if term 'x' have look cons($x,$y) or NIL";
    }
    
    public String getName() {
        return "isList";
    }
    
    public boolean internalsAtFirst() {
        return false;
    }
    
    
    
}
