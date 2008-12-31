package ua.gradsoft.termware.transformers.general;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002, 2003, 2004, 2005
 */


import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.util.LogHelper;
                          
/**
 * greater_eq(x,y) = x >= y
 * <pre> 
 *   greater_eq(boolean, boolean)  -> usial less in ariphmetics sence.
 *   greater_eq(numeric, numeric)  -> usial less in ariphmetics sence.
 *   greater_eq(string,  string )  -> Compares lexicographically
 * </pre>
 **/
public class GreaterEqTransformer extends AbstractBuildinTransformer
{

    
 public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
  { return static_transform(t,system,ctx); }


static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
  if (t.getArity()!=2) {
     throw new AssertException("greater_eq must have arity 2");
  }
  LogHelper.log(system,GreaterEqTransformer.class, "t=", t);
  Term frs = t.getSubtermAt(0);
  Term snd = t.getSubtermAt(1);
  
  Term retval=t;
  if (frs.isBoolean()) {
      if (snd.isBoolean()) {
          ctx.setChanged(true);
          retval = system.getInstance().getTermFactory().createBoolean(frs.getBoolean()==true || snd.getBoolean()==false);
      }
  }else if (frs.isNumber() && snd.isNumber()) {
      ctx.setChanged(true);
      retval=system.getInstance().getTermFactory().createBoolean(frs.termCompare(snd) >= 0);
  }else if (frs.isString() && snd.isString() ) {
      ctx.setChanged(true);
      retval=system.getInstance().getTermFactory().createBoolean(frs.termCompare(snd) >= 0);
  }
  LogHelper.log(system,GreaterEqTransformer.class,"return ",retval);
  return retval;
}


    public boolean internalsAtFirst() {
        return false;
    }

  
 public String getDescription() {
     return STATIC_DESCRIPTION_; 
 }
 
 public String getName() {
     return "greater_eq";
 }
 
 private final static String STATIC_DESCRIPTION_ = 
  " greater_eq(x,y) = x >= y \n"+
  " <pre> \n"+
  "   greater_eq(boolean, boolean)  -> usial compare in ariphmetics sence. \n" +
  "   greater_eq(numeric, numeric)  -> usial compare in ariphmetics sence. \n" +
  "   greater_eq(string,  string )  -> Compares lexicographically \n"+
  " </pre>"
  ;
 
}
 