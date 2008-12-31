/*
 * BTStrategy.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.strategies;

import java.util.Iterator;
import ua.gradsoft.termware.ITermTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareSymbols;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.util.LogHelper;

/**
 * Strategy, which at first try to maximum evaluate subexpressions,
 *than - expression.
 */
public class BTStrategy extends AbstractTermRewritingStrategy {
    
    public BTStrategy() {
        super(); }
    
    public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return transform(t,system,ctx,0);
    }
    
    public Term transform(Term t, TermSystem system, TransformationContext ctx, int recursiveIndex) throws TermWareException {
                        
        if (ctx.isStop() || t.isString()) return t;
        
        
        //--------------
        if (system.isLoggingMode()) {
            if (system.checkLoggedEntity("All")||system.checkLoggedEntity("Strategy")||system.checkLoggedEntity(this.getClass().getName())) {
                system.getEnv().getLog().print(getClass().getName()+": t=");
                t.print(system.getEnv().getLog());
                system.getEnv().getLog().println();
            }
        }
        
        boolean svChanged=ctx.isChanged();
        boolean wasChanged=false;
        boolean wasChangedAtLeastOnce=false;
        do {
            wasChanged=false;
            if (t.isComplexTerm()) {
                // special processing for lists.
                if (t.getNameIndex().equals(TermWareSymbols.CONS_INDEX) && t.getArity()==2) {
                    Term nt=t;
                    Term pnt=null;
                    while(!nt.isNil() 
                           && 
                         nt.isComplexTerm()
                           &&
                         nt.getNameIndex().equals(TermWareSymbols.CONS_INDEX)
                           &&
                         nt.getArity()==2
                                             ) {
                        Term x = nt.getSubtermAt(0);                       
                        do {
                            ctx.setChanged(false);
                            if (recursiveIndex < MAX_RECURSIVE_INDEX) {
                                x=transform(x,system,ctx, recursiveIndex+1);
                                if (ctx.isChanged()) {
                                    wasChanged=true;
                                    nt.setSubtermAt(0,x);
                                }                            
                            }else{
                                if (wasChanged) {
                                    ctx.setChanged(true);
                                }
                            }
                        }while(false);
                        pnt=nt;
                        nt=nt.getSubtermAt(1);
                    }
                    if (!nt.isNil()) {
                      if (pnt!=null) {
                         ctx.setChanged(false);
                         Term x=transform(nt,system,ctx,recursiveIndex+1);
                         if (ctx.isChanged()) {
                             wasChanged=true;
                             pnt.setSubtermAt(1,x);
                         }else{
                             if (wasChanged) {
                               ctx.setChanged(true);
                             }
                         }
                      }else{
                         // impossible, but let do standart procedure
                         // (think - may be better throw assertion ?)
                         for(int i=0; i<nt.getArity(); ++i) {
                            Term x=t.getSubtermAt(i);
                            ctx.setChanged(false); 
                            x=transform(x, system,ctx, recursiveIndex+1);
                            if (ctx.isChanged()) {
                                t.setSubtermAt(i,x);
                            }
                         }
                      }
                    }
                } else {
                    for(int i=0; i<t.getArity(); ++i) {                
                      Term x=t.getSubtermAt(i);
                      do {                         
                         if (recursiveIndex < MAX_RECURSIVE_INDEX) {
                            ctx.setChanged(false); 
                            x=transform(x, system,ctx, recursiveIndex+1);
                            if (ctx.isChanged()) {
                                t.setSubtermAt(i,x);
                                wasChanged=true;
                            }//else{
                         }else{   
                            if (svChanged || wasChanged) {
                                ctx.setChanged(true);
                            }                               
                            return t;
                         }
                         //   t.setSubtermAt(i,x);
                         //}
                         if (ctx.isStop()) {
                            return t;
                         }
                      }while(false);
                    }
                }
            }
            
            
            //------
            Iterator it=getStar().iterator(t.getName());
            ctx.setChanged(false);
            
            while(it!=null && it.hasNext() ) {
                ITermTransformer tr=(ITermTransformer)it.next();
                if (system.isLoggingMode()) {
                    if (system.checkLoggedEntity("All")||system.checkLoggedEntity("Strategies")||system.checkLoggedEntity(this.getClass().getName())) {
                        system.getEnv().getLog().println(getClass().getName()+":found transformer for:"+tr.getName()+"[t.getName() is "+t.getName()+"]");
                    }
                }               
                t=tr.transform(t,system,ctx);
                if (ctx.isStop()) return t;
                if (ctx.isChanged()) {
                    if (system.isLoggingMode()) {
                        LogHelper.log(system,BTStrategy.class,"changed");
                        if (system.checkLoggedEntity("StrategyReductions")) {
                           system.getEnv().getLog().println(getClass().getName() +": applied "+tr.getName());
                           system.getEnv().getLog().println(", source="+tr.getSource());
                        }                       
                    }
                    wasChanged=true;
                    wasChangedAtLeastOnce=true;
                }else{
                    //if (system.isLoggingMode()) {
                    // system.getEnv().getLog().println("not-changed");
                    //}
                }
            }
        }while(wasChanged);
        
        if (svChanged || wasChangedAtLeastOnce) {
            ctx.setChanged(true);
        }
        
        
        return t;
    }
    
    /**
     * get description of strategy
     */
    public String getDescription() {
        return "BT  - try at first evaluate subexpression, while this is possible, than go up";
    }
    
    public String getName() {
        return "BT";
    }
    
    private static final int MAX_RECURSIVE_INDEX = 500;
    
}
