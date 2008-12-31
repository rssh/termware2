package ua.gradsoft.termware.transformers.general;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002, 2003, 2004, 2005
 */


import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.util.LogHelper;
                          
/**
 * less_eq(x,y) = x <= y - x less or equal then y
 * <pre> 
 *   less_eq(boolean, boolean)  -> usial less or equal in ariphmetics sence.
 *   less_eq(numeric, numeric)  -> usial less or equal in ariphmetics sence.
 *   less_eq(string,  string )  -> Compares lexicographically
 * </pre>
 **/
public class LessEqTransformer extends AbstractBuildinTransformer
{

    
 public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
  { return static_transform(t,system,ctx); }


static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
  if (t.getArity()!=2) {
     throw new AssertException("less_eq must have arity 2");
  }
  LogHelper.log(system,LessEqTransformer.class,"less_eq, t=", t);
  Term frs = t.getSubtermAt(0);
  Term snd = t.getSubtermAt(1);
  Term retval=t;
  if (frs.isBoolean()) {
      if (snd.isBoolean()) {
          ctx.setChanged(true);
          retval = system.getInstance().getTermFactory().createBoolean(frs.getBoolean()==false || snd.getBoolean()==true);
      }
  }else if (frs.isNumber() && snd.isNumber()) {
      ctx.setChanged(true);
      retval=system.getInstance().getTermFactory().createBoolean(frs.termCompare(snd) <= 0);
  }else if (frs.isString() && snd.isString() ) {
      ctx.setChanged(true);
      retval=system.getInstance().getTermFactory().createBoolean(frs.termCompare(snd) <= 0);
  }
  LogHelper.log(system,LessEqTransformer.class,"return ",retval);
  return retval;
}


    public boolean internalsAtFirst() {
        return false;
    }

  
 public String getDescription() {
     return STATIC_DESCRIPTION_; 
 }
 
 public String getName() {
     return "less_eq";
 }
 
 private final static String STATIC_DESCRIPTION_ = 
  " less_eq(x,y) = x <= y  (x less or equal then y) <BR>"+
  " <pre> \n"+
  "   less_eq(boolean, boolean)  -> usial less or equal in ariphmetics sence. \n" +
  "   less_eq(numeric, numeric)  -> usial less or equal in ariphmetics sence. \n" +
  "   less_eq(string,  string )  -> Compares lexicographically \n"+
  " </pre>"
  ;
 
}
 