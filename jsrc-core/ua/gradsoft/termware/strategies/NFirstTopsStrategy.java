
package ua.gradsoft.termware.strategies;

import java.util.Iterator;
import ua.gradsoft.termware.ITermTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.util.LogHelper;

/**
 *Strategy, which try at first do reduction of first level.
 * @author rssh
 */
public class NFirstTopsStrategy  extends AbstractTermRewritingStrategy 
{
    
 public NFirstTopsStrategy()
 { this(1000); }

 public NFirstTopsStrategy(int n)
  { super(); 
    n_=n;
  }


 public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
     return transform(t,system,ctx,n_,0);
 }
 
 
 public Term transform(Term t, TermSystem system, TransformationContext ctx, int n, int level) throws TermWareException
 {
     
   if (ctx.isStop()) return t;
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
   
   boolean svChanged=ctx.isChanged();
   ctx.setChanged(false);
   
   boolean debug=false;
   if (t.getName().equals("ten")) {    
       LogHelper.log(system,NFirstTopsStrategy.class,"debug start");
       debug=true;
   }
   
   int c=n;
   
   Iterator it=getStar().iterator(t.getName());
   
   if (debug) {
       if (it==null || ! it.hasNext()) {
          LogHelper.log(system,NFirstTopsStrategy.class,"we have not transformer for "+t.getName());
       }       
       if (ctx.isChanged()) {
           LogHelper.log(system,NFirstTopsStrategy.class,"was changed at start");
       }
   }
   
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
              LogHelper.log(system,NFirstTopsStrategy.class,"changed");
              LogHelper.log(system,"StrategyReductions",NFirstTopsStrategy.class,"applied "+tr.getName()+", t=",t);
           }  
           ++c;
           if (c>= n_ || level != 0) {
             return t;      
           }
       }else{
           //if (system.isLoggingMode()) {
           //  LogHelper.log(system,FirstTopStrategy.class,"unchanged");
           //}  
       }
   }
   if (t.isComplexTerm()) {
     for(int i=0; i<t.getArity(); ++i) {
       Term x=t.getSubtermAt(i);
       if (system.isLoggingMode()) {
           LogHelper.log(system, NFirstTopsStrategy.class, "x=",x);
       }
       x=transform(x,system,ctx,1,level+1);
       if (ctx.isChanged()) {
         t.setSubtermAt(i,x);
//         if (system.isLoggingMode()) {
//             LogHelper.log(system,FirstTopStrategy.class,"changed subterm "+i);
//         }
         ++c;
         if (level > 0) {
           return t;  
         }
       }  
       if (ctx.isStop()) {
         if (system.isLoggingMode()) {
             LogHelper.log(system,NFirstTopsStrategy.class,"stop");
         }  
         if (!ctx.isChanged()) {
             ctx.setChanged(svChanged);
         }
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
      return "NFirstTops  - try to reduce terms at first n levels.";
  }
  
  public String getName() {
      return "NFirstTops";
  }
    
  private int n_;  

}
