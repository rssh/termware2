package ua.gradsoft.termware.transformers.general;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002, 2003, 2004
 */


import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.util.LogHelper;
                          
/**
 * logical_or(x,y) = x || y
 * <pre> 
 *   logical_or(boolean, boolean)  -> logical addition of arguments.
 * </pre>
 **/
public class LogicalOrTransformer extends AbstractBuildinTransformer
{

    
 public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
  { return static_transform(t,system,ctx); }


 static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
  if (t.getArity()!=2) {
     throw new AssertException("logical_or must have arity 2");
  }
  LogHelper.log(system,LogicalOrTransformer.class,"t=", t);
  Term frs = t.getSubtermAt(0);
  Term snd = t.getSubtermAt(1);
  Term retval=t;
  if (frs.isBoolean()) {
      if (snd.isBoolean()) { 
        ctx.setChanged(true);
        retval = system.getInstance().getTermFactory().createBoolean( frs.getBoolean() || snd.getBoolean() );
      }else if (frs.getBoolean()) {
          // true || x -> true
          ctx.setChanged(true);
          retval = BooleanTerm.getBooleanTerm(true);          
      }else{
          // false || x -> x
          ctx.setChanged(true);
          retval=snd;
      }
  }else if (snd.isBoolean()) {
      if (snd.getBoolean()) {
          // x || true -> true
          ctx.setChanged(true);
          retval = BooleanTerm.getBooleanTerm(true);
      }else{
          // x || false -> x
          ctx.setChanged(true);
          retval = frs;
      }
  }
  LogHelper.log(system,LogicalOrTransformer.class,"return ",retval);
  return retval;
}
  
 public String getDescription() {
     return STATIC_DESCRIPTION_; 
 }
 
 public String getName() {
     return "logical_or";
 }
 
 private final static String STATIC_DESCRIPTION_ = 
  "logical_or(x,y) = x || y \n"+
  " <pre> \n"+
  "  logical_or(boolean, boolean)  -> logical addition of arguments. \n"+
  " </pre> "
  ;

 
}
 