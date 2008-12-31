package ua.gradsoft.termware.strategies;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002, 2003, 2004, 2005
 * (C) GradSoft Ltd. http://www.gradsoft.ua
 * $Id: TopDownStrategy.java,v 1.7 2008-01-13 01:39:37 rssh Exp $
 */

import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.util.*;
                           
/**
 * at first transform top terms, than subterms.
 **/
public class TopDownStrategy extends AbstractTermRewritingStrategy 
{

 public TopDownStrategy()
  { super(); }

 public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
   if (ctx.isStop() || t.isString()) return t;
   if (system.isLoggingMode()) {
      LogHelper.log(system,"Strategy",TopDownStrategy.class,"t=",t); 
   }

   Iterator it=null;
   boolean svChanged=ctx.isChanged();
   do {
     ctx.setChanged(false);
     it=getStar().iterator(t.getName());
      
     if (it!=null) {
       while(it.hasNext() && !ctx.isChanged() && !ctx.isStop()) {
         ITermTransformer tr=(ITermTransformer)it.next();
         t=tr.transform(t,system,ctx);
         if (ctx.isChanged()) {
           svChanged=true;
           break;
         }
       }       
     }
   }while(ctx.isChanged());

   svChanged=(svChanged||ctx.isChanged());

   if (t.isComplexTerm() && !ctx.isStop()) {
     for(int i=0; i<t.getArity() && !ctx.isStop(); ++i) {
       ctx.setChanged(false);
       Term x=t.getSubtermAt(i);
       x=transform(x,system,ctx);
       if (ctx.isChanged()) {
         if (system.isLoggingMode()) {
            LogHelper.log(system,"Strategy",TopDownStrategy.class,"substitution in subterm "+i);  
         }
         svChanged=true;
         t.setSubtermAt(i,x); 
       }  
     }
   }
   ctx.setChanged(svChanged||ctx.isChanged()); 
   if (system.isLoggingMode()) {
      LogHelper.log(system,"Strategy",TopDownStrategy.class,"t=",t); 
   }
   return t;  
 }


 @Override
  public String getDescription() {
      return "TopDown Strategy - reduce from top to down";
  }  
 
 @Override
  public String getName() {
      return "TopDown";
  }
  
}
                                  
