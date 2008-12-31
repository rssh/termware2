/*
 * SublistTransformer.java
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
import ua.gradsoft.termware.exceptions.ConversionException;

/**
 *Transformer for sublist.
 *sublist(l,n1,n2) - get
 * @author Ruslan Shevchenko
 */
public class SublistTransformer extends AbstractBuildinTransformer {
    
    private SublistTransformer() {}
    
    public static final SublistTransformer INSTANCE = new SublistTransformer();
    
    public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx); }
    
    
    static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        if (t.getArity()==2) {
            Term l = t.getSubtermAt(0);
            Term pos = t.getSubtermAt(1);
            int ipos=0;
            try {
                ipos = pos.getAsInt(system.getInstance());
            }catch(ConversionException ex){
                return t;
            }
            while(ipos>0 && !l.isNil() && l.isComplexTerm() && l.getNameIndex().equals(TermWareSymbols.CONS_INDEX) && l.getArity()==2){
                --ipos;
                l=l.getSubtermAt(1);
            }
            if (ipos>0 && !l.isNil()) {
                l=system.getInstance().getTermFactory().createTerm("sublist",l,system.getInstance().getTermFactory().createInt(ipos));
            }
            ctx.setChanged(true);
            return l;
        }else if (t.getArity()==3) {
            Term l = t.getSubtermAt(0);
            Term pos1 = t.getSubtermAt(1);
            Term pos2 = t.getSubtermAt(2);
            int ipos1=0;
            int ipos2=0;
            try {
                ipos1 = pos1.getAsInt(system.getInstance());
                ipos2 = pos2.getAsInt(system.getInstance());
            }catch(ConversionException ex){
                return t;
            }
            while(ipos1>0 && !l.isNil() && l.isComplexTerm() && l.getNameIndex().equals(TermWareSymbols.CONS_INDEX) && l.getArity()==2) {
                --ipos1;
                --ipos2;
                l=l.getSubtermAt(1);
            }
            if (l.isNil()) {
                ctx.setChanged(true);
                return l;
            }
            Term curr=l;
            Term revCurr=NILTerm.getNILTerm();
            int i=0;
            while(ipos2>0 && !curr.isNil() && curr.getNameIndex().equals(TermWareSymbols.CONS_INDEX) && curr.getArity()==2) {
                revCurr=system.getInstance().getTermFactory().createConsTerm(curr.getSubtermAt(0),revCurr);
                --ipos2;
                ++i;
                curr=curr.getSubtermAt(1);
            }
            Term retval=NILTerm.getNILTerm();
            if (ipos2>0) {
                if (!curr.isNil()) {
                    if (!curr.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
                        retval=system.getInstance().getTermFactory().createTerm("sublist",curr,
                                system.getInstance().getTermFactory().createInt(0),
                                system.getInstance().getTermFactory().createInt(ipos2));
                    }
                }
            }
            while(!revCurr.isNil()) {
                retval=system.getInstance().getTermFactory().createConsTerm(revCurr.getSubtermAt(0),retval);
                revCurr=revCurr.getSubtermAt(1);
            }
            ctx.setChanged(true);
            return retval;
        }else{
            return t;
        }
    }
    
    
    public String getDescription() {
        return "sublist(l,pos1,pos2) - get sublist from position pos1 to pos2 in l\n";
    }
    
    public String getName() {
        return "sublist";
    }
    
    public boolean internalsAtFirst() {
        return false;
    }
    
    
    
    
}
