/*
 * ListInsertTransformer.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
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
import ua.gradsoft.termware.util.LogHelper;

/**
 *
 *Insert(e,pos,l) - insert e at pos position in list l.
 *Insert(e,l) - same as Insert(e,0,l)
 *More formal definition:
 *<code>
 *insert($x, 0,    $y) [isList($y)] -> [$x:$y]
 *insert($x, $pos, [$y:$z]) [isInt(pos)] -> [$y:insert($x,$pos-1,$z)]
 *insert($x, $pos  NIL) -> cons($x,NIL)
 *</code>
 * @author Ruslan Shevchenko
 */
public class ListInsertTransformer extends AbstractBuildinTransformer
{
    
    private ListInsertTransformer() {}
    
    public static final ListInsertTransformer INSTANCE = new ListInsertTransformer();
    
    public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx); }
    
    
    static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {                        
        if (system.isLoggingMode()) {
            LogHelper.log(system,ListInsertTransformer.class,"found List::insert for ",t);
        }
        if (t.getArity()==2) {
            Term e = t.getSubtermAt(0);
            Term l = t.getSubtermAt(1);
            if (l.isNil() || (l.isComplexTerm() && l.getNameIndex().equals(TermWareSymbols.CONS_INDEX) && l.getArity()==2)) {
                ctx.setChanged(true);
                Term retval=system.getInstance().getTermFactory().createConsTerm(e,l);
                if (system.isLoggingMode()) {
                  LogHelper.log(system,ListInsertTransformer.class,"List::insert result ",retval);
                }
                return retval;
            }else{
                if (system.isLoggingMode()) {
                  LogHelper.log(system,ListInsertTransformer.class,"List::insert return unchanged ");
                }                
            }      
            return t;
        }else if (t.getArity()==3) {
            Term e = t.getSubtermAt(0);
            Term pos = t.getSubtermAt(1);
            Term l = t.getSubtermAt(2);
            int ipos=0;
            try {
                ipos = pos.getAsInt(system.getInstance());
            }catch(ConversionException ex){
                if (system.isLoggingMode()) {
                    LogHelper.log(system,ListInsertTransformer.class,"pos is not int, return unchanged");
                }
                return t;
            }
            Term retval=insert(e,ipos,l,system);
            ctx.setChanged(true);
            if (system.isLoggingMode()) {
               LogHelper.log(system,ListInsertTransformer.class,"List::insert result ",retval);                
            }
            return retval;            
        }else{
            if (system.isLoggingMode()) {
              LogHelper.log(system,ListInsertTransformer.class,"arity does not match, unchanged ");                            
            }
            return t;
        }
    }
    
    public static Term insert(Term el,int pos, Term list,TermSystem system) throws TermWareException
    {        
        Term retval=null;
        if (pos==0) {
            retval=system.getInstance().getTermFactory().createConsTerm(el,list);
        }else{
          Term curr=list;  
          Term revCurr=NILTerm.getNILTerm();
          while(!curr.isNil() && pos>0) {
              revCurr=system.getInstance().getTermFactory().createConsTerm(curr.getSubtermAt(0),revCurr);
              curr=curr.getSubtermAt(1);              
              --pos;
          }
          revCurr=system.getInstance().getTermFactory().createConsTerm(el,revCurr);
          retval=curr;
          while(!revCurr.isNil()) {
              retval=system.getInstance().getTermFactory().createConsTerm(revCurr.getSubtermAt(0),retval);
              revCurr=revCurr.getSubtermAt(1);
          }
        }
        return retval;
    }
    
    public String getDescription() {
        return "insert(e,pos,x) - insert e at position pos in list x\n"+
               "insert(e,x) - same as insert(e,0,x)";
    }
    
    public String getName() {
        return "insert";
    }
    
    public boolean internalsAtFirst() {
        return false;
    }
    

    
    
}
