/*
 * ReverseTransformer.java
 *
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.transformers.list;

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.NILTerm;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareSymbols;
import ua.gradsoft.termware.TransformationContext;

/**
 *reverse list.
 *reverse([$x:$y]) -> append($x,reverse($y))
 *reverse(NIL) -> nil
 * @author Ruslan Shevchenko
 */
public class ReverseTransformer extends AbstractBuildinTransformer
{
    
        
    private ReverseTransformer() {}
    
    public static final ReverseTransformer INSTANCE = new ReverseTransformer();
    
    public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx); }
    
    
    static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {                        
        if (t.getArity()==1) {
            Term l = t.getSubtermAt(0);
            if (l.getArity()==2 && l.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
               Term retval=NILTerm.getNILTerm();
               while(!l.isNil() &&  l.getArity()==2 && l.getNameIndex().equals(TermWareSymbols.CONS_INDEX) ) {
                   retval=system.getInstance().getTermFactory().createConsTerm(l.getSubtermAt(0),retval);
                   l=l.getSubtermAt(1);
               }
               if (!l.isNil()) {
                   retval=system.getInstance().getTermFactory().createTerm("reverse",retval);
               }
               ctx.setChanged(true);
               return retval;
            }else if (l.isNil()) {
                ctx.setChanged(true);
                return l;
            }                   
        }
        return t;
    }
    
    
    public String getDescription() {
        return "reverse list. i. e. reverse([x1...xn])->[xn...x1]";
    }
    
    public String getName() {
        return "reverse";
    }
    
    public boolean internalsAtFirst() {
        return false;
    }
    
    
    
}
