package ua.gradsoft.termware.transformers.general;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2005
 * $Id: IfTransformer.java,v 1.3 2007-07-13 20:50:20 rssh Exp $
 */

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.exceptions.AssertException;
import ua.gradsoft.termware.util.LogHelper;


/**
 * if(x,y,z)  ( x ? y : z )
 *   if (x is true - return x, otherwise y);
 */
public class IfTransformer extends AbstractBuildinTransformer
{

 public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
  { return static_transform(t,system,ctx); }


static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {    
  if (t.getArity()!=3) {
     throw new AssertException("arity of if must be 3");
  }
  if (system.isLoggingMode()) {
      LogHelper.log(system,IfTransformer.class,"if applied for ",t);
  }
  boolean condition=false;
  boolean frsIsBool=false;
  if (!t.getSubtermAt(0).isBoolean()) {
     boolean svChanged=ctx.isChanged();
     ctx.setChanged(false);
     Term frs=ToBooleanTransformer.static_transform(t.getSubtermAt(0),system,ctx);
     if (ctx.isChanged()) {            
        frsIsBool=true;
        condition=frs.getBoolean();
     }
     ctx.setChanged(svChanged);
  }else{
     frsIsBool=true;
     condition=t.getSubtermAt(0).getBoolean();
  }
  if (frsIsBool) {
     ctx.setChanged(true);
     Term retval = (condition) ? t.getSubtermAt(1) : t.getSubtermAt(2) ;
     if (system.isLoggingMode()) {
        LogHelper.log(system,IfTransformer.class,"return ",retval);
     }    
     return retval;
  }else{
     if (system.isLoggingMode()) {
        LogHelper.log(system,IfTransformer.class,"return ",t);
     }     
     return t;
  }
 }


 public String getDescription() {
     return "if(x,y,z) = [ x ? y : z ] - if x is true, then return y, otherwise - z ";
 }
 
 public String getName() {
     return "if";
 }

    public boolean internalsAtFirst() {
        return false;
    }
 
}
 