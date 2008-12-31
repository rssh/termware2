/*
 * ListCarTransformer.java
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
 *car([$x:$y]) -> $x, 
 *car([]) -> []
 * otherwise unchanged
 * @author Ruslan Shevchenko
 */
public class ListCarTransformer extends AbstractBuildinTransformer 
{
    
    private ListCarTransformer() {}
    
    public static final ListCarTransformer INSTANCE = new ListCarTransformer();
    
    public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx); }
    
    
    static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {                        
        if (t.getArity()==1) {
            Term l = t.getSubtermAt(0);
            if (l.getArity()==2 && l.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
               ctx.setChanged(true);
               return l.getSubtermAt(0);
            }else if (l.isNil()){
                ctx.setChanged(true);
                return l;
            }                    
        }        
        return t;
    }
    
    
    public String getDescription() {
        return "car(cons(x,y)) -> x, car(NIL)->NIL, otherwise unchanged";
    }
    
    public String getName() {
        return "car";
    }
    
    public boolean internalsAtFirst() {
        return false;
    }
    
    
    
}
