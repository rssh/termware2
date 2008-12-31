/*
 * ListLengthTransformer.java
 *
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.transformers.list;

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareSymbols;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.util.LogHelper;

/**
 *length.
 *<code>
 *  length([]) -> 0,
 *  length([$x:$y]) -> 1+length($y)
 *</code>
 * @author Ruslan Shevchenko
 */
public final class ListLengthTransformer extends AbstractBuildinTransformer
{
    
    private ListLengthTransformer() {}
    
    public static final ListLengthTransformer INSTANCE = new ListLengthTransformer();
    
    public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx); }
    
    
    static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        LogHelper.log(system,ListLengthTransformer.class,"ListLength transformer for ",t);
        if (!t.isComplexTerm()||t.getArity()!=1) {
            LogHelper.log(system,ListLengthTransformer.class,"return unchanged");
            return t;
        }
        Term l=t.getSubtermAt(0);
        if (l.isNil()) {
            ctx.setChanged(true);
            Term retval = TermWare.getInstance().getTermFactory().createInt(0);
            if (system.isLoggingMode()){
              LogHelper.log(system,ListLengthTransformer.class,"result=0");
            }
            return retval;
        }else if (l.isComplexTerm() && l.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
            int sum=0;
            while(!l.isNil()) {
                ++sum;
                if (l.getArity()==2 && l.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
                  l=l.getSubtermAt(1);
                }else{
                    break;
                }
            }
            if (!l.isNil()) {
                Term tsum = system.getInstance().getTermFactory().createInt(sum);                
                t=system.getInstance().getTermFactory().createTerm("plus",tsum,l);
            }else{
                t=TermWare.getInstance().getTermFactory().createInt(sum);    
            }            
            LogHelper.log(system,ListLengthTransformer.class,"result=",t);
            ctx.setChanged(true);            
            return t;
        }else{
            LogHelper.log(system,ListLengthTransformer.class,"is not list, unchanged");
        }
        LogHelper.log(system,ListLengthTransformer.class,"result=",t);
        return t;
    }
    
    
    public String getDescription() {
        return "return length of list";
    }
    
    public String getName() {
        return "Length";
    }
    
    public boolean internalsAtFirst() {
        return false;
    }
    
    
    
}
