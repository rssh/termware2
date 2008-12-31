package ua.gradsoft.termware.strategies;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002, 2003, 2004
 *
 * $Id: FirstTopStrategy.java,v 1.10 2008-03-24 22:33:09 rssh Exp $
 */


import java.util.Iterator;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.ITermTransformer;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.util.LogHelper;


/**
 * FirstTop
 */
public class FirstTopStrategy extends AbstractTermRewritingStrategy 
{

 public FirstTopStrategy()
  { super(); }

 public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
   if (ctx.isStop()) {
     if (system.isLoggingMode()) {
        LogHelper.log(system, FirstTopStrategy.class, "ctx.isStop() true");    
     } 
     return t;
   }
   //--------------
   if (system.isLoggingMode()) {
     if (system.checkLoggedEntity("All")||system.checkLoggedEntity("Strategy")||system.checkLoggedEntity(this.getClass().getName())) {
       system.getEnv().getLog().print(getClass().getName()+": t=");
       t.print(system.getEnv().getLog());
       system.getEnv().getLog().println();
     }
   }
   //------
   if (t.isString()) {
       return t;
   }
   
   boolean svChanged = ctx.isChanged();
   ctx.setChanged(false);
   
   Iterator it=getStar().iterator(t.getName());
   
   while(it!=null && it.hasNext() && !ctx.isChanged()) {
       ITermTransformer tr=(ITermTransformer)it.next();
       if (system.isLoggingMode()) {
         if (system.checkLoggedEntity("All")||system.checkLoggedEntity("Strategies")||system.checkLoggedEntity(this.getClass().getName())) {           
            system.getEnv().getLog().println(getClass().getName()+":found transformer for:"+tr.getName());
         }
       }
       Term prevT=t;
       t=tr.transform(prevT,system,ctx);
       if (ctx.isStop()) return t;
       if (ctx.isChanged()) {
           if (system.isLoggingMode()) {
              LogHelper.log(system,FirstTopStrategy.class,"changed");
              if (system.checkLoggedEntity("StrategyReductions")) {
                  system.getEnv().getLog().println(getClass().getName() +": applied "+tr.getName()+", source="+tr.getSource());
              }
           }  
           return t;      
       }else{
           //if (system.isLoggingMode()) {
           //  LogHelper.log(system,FirstTopStrategy.class,"unchanged");
           //}  
       }
   }
   if (t.isComplexTerm()) {
     for(int i=0; i<t.getArity(); ++i) {
       Term x=t.getSubtermAt(i);
       x=transform(x,system,ctx);
       if (ctx.isChanged()) {
         t.setSubtermAt(i,x);
//         if (system.isLoggingMode()) {
//             LogHelper.log(system,FirstTopStrategy.class,"changed subterm "+i);
//         }
         return t; 
       }  
       if (ctx.isStop()) {
         if (system.isLoggingMode()) {
             LogHelper.log(system,FirstTopStrategy.class,"stop");
         }  
         ctx.setChanged(svChanged);
         return t;
       }
     }
   }   
   if (!ctx.isChanged()) {
     ctx.setChanged(svChanged);
   }
   return t;  
 }

 /**
  * get description of strategy
  */
  public String getDescription() {
      return "First top  - most top terms reduced first (from left).";
  }
  
  public String getName() {
      return "FirstTop";
  }
  
}
                                  
