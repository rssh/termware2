/*
 * ListAppendTransformer.java
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
import ua.gradsoft.termware.util.LogHelper;

/**
 *append(e,l) - append e to the end of l.
 * @author Ruslan Shevchenko
 */
public class ListAppendTransformer extends AbstractBuildinTransformer
{
    
    private ListAppendTransformer() {}
    
    public static final ListAppendTransformer INSTANCE = new ListAppendTransformer();
    
    public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx); }
    
    
    static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {                        
        if (t.getArity()==2) {
            if (system.isLoggingMode()) {
                LogHelper.log(system,ListAppendTransformer.class,"builtin List.append");
            }
            Term e = t.getSubtermAt(0);
            Term l = t.getSubtermAt(1);
            if (e.isNil()) {                
                ctx.setChanged(true);
                if (system.isLoggingMode()) {
                    LogHelper.log(system,ListAppendTransformer.class,"return second argument");
                }
                return l;
            }
            try {
              Term retval=append(e,l,system);
              ctx.setChanged(true);
              if (system.isLoggingMode()) {
                  LogHelper.log(system,ListAppendTransformer.class,"changed, return ",retval);
              }     
              return retval;
            }catch(InvalidListTermException ex){
                if (system.isLoggingMode()) {
                    LogHelper.log(system,ListAppendTransformer.class,"exception during append (InvalidList), return unchanged");
                }
                return t;
            }
        }else{
          if (system.isLoggingMode()) {
              LogHelper.log(system,ListAppendTransformer.class,"arity!=2, return unchanged");
          }   
          return t;
        }
    }
    
    public static Term append(Term e, Term l, TermSystem system) throws TermWareException
    {        
          Term curr=l;            
          Term revCurr=NILTerm.getNILTerm();
          while(!curr.isNil()) {
              if (! (l.isComplexTerm() && l.getNameIndex().equals(TermWareSymbols.CONS_INDEX) && l.getArity()==2)) {
                  throw new InvalidListTermException(l);
              }
              revCurr=system.getInstance().getTermFactory().createConsTerm(curr.getSubtermAt(0),revCurr);
              curr=curr.getSubtermAt(1);                          
          }
          revCurr=system.getInstance().getTermFactory().createConsTerm(e,revCurr);
          Term retval=NILTerm.getNILTerm();
          while(!revCurr.isNil()) {
              retval=system.getInstance().getTermFactory().createConsTerm(revCurr.getSubtermAt(0),retval);
              revCurr=revCurr.getSubtermAt(1);
          }        
        return retval;
    }
    
    public String getDescription() {
        return "append(e,x) - insert e at the end of list x\n";
    }
    
    public String getName() {
        return "append";
    }
    
    public boolean internalsAtFirst() {
        return false;
    }
    


    
}
