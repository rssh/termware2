/*
 * ListCdrTransformer.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.transformers.list;

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareSymbols;
import ua.gradsoft.termware.TransformationContext;

/**
 *cdr([$x:$y]) -> $y, cdr(NIL) -> NIL
 * @author Ruslan Shevchenko
 */
public class ListCdrTransformer extends AbstractBuildinTransformer
{
    
    private ListCdrTransformer() {}
    
    public static final ListCdrTransformer INSTANCE = new ListCdrTransformer();
    
    public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx); }
    
    
    static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {                        
        if (t.getArity()==1) {
            Term l = t.getSubtermAt(0);
            if (l.getArity()==2 && l.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
               ctx.setChanged(true);
               return l.getSubtermAt(1);
            }else if (l.isNil()) {
                ctx.setChanged(true);
                return l;
            }                   
        }
        return t;
    }
    
    
    public String getDescription() {
        return "cdr(cons(x,y)) -> y, cdr(NIL)->NIL, otherwise unchanged";
    }
    
    public String getName() {
        return "cdr";
    }
    
    public boolean internalsAtFirst() {
        return false;
    }
    
    

}
