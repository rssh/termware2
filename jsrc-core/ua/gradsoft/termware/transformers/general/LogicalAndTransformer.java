package ua.gradsoft.termware.transformers.general;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002, 2003, 2004
 */


import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.util.LogHelper;
                          
/**
 * logical_and(x,y) = x && y
 **/
public class LogicalAndTransformer extends AbstractBuildinTransformer
{

    
 public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
  { return static_transform(t,system,ctx); }


 static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
  if (t.getArity()!=2) {
     throw new AssertException("logical_and must have arity 2");
  }
  LogHelper.log(system,LogicalAndTransformer.class,"LogicalAnd, t=", t);
  Term frs = t.getSubtermAt(0);
  Term snd = t.getSubtermAt(1);
  Term retval=t;
  if (frs.isBoolean() && snd.isBoolean()) {
      ctx.setChanged(true);
      retval = system.getInstance().getTermFactory().createBoolean( frs.getBoolean() && snd.getBoolean() );
  }else if (frs.isBoolean()) {
      if (frs.getBoolean()) {
          // true && y -> y
          ctx.setChanged(true);
          retval=snd;
      }else{
          // false &&  y -> false
          ctx.setChanged(true);
          retval=frs;
      }
  }else if (snd.isBoolean()) {
      if (snd.getBoolean()) {
          // x && true -> x
          ctx.setChanged(true);
          retval=frs;
      }else{
          // x && false -> x
          ctx.setChanged(true);
          retval=snd;
      }
  }
  LogHelper.log(system, LogicalAndTransformer.class,"return ",retval);
  return retval;
}
  
 public String getDescription() {
     return STATIC_DESCRIPTION_; 
 }
 
 public String getName() {
     return "logical_and";
 }
 
 private final static String STATIC_DESCRIPTION_ = 
  " logical_and(x,y) = x && y \n"+
  " <pre> \n"+
  "   logical_and(boolean, boolean)  -> logical multiplication of arguments. \n" +
  " </pre>"
  ;

    public boolean internalsAtFirst() {
        return false;
    }
 
}
 