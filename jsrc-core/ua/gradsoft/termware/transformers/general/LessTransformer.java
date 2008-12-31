package ua.gradsoft.termware.transformers.general;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002, 2003, 2004
 */


import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.util.LogHelper;
                          
/**
 * less(x,y) = x < y
 * <pre> 
 *   less(boolean, boolean)  -> usial less in ariphmetics sence.
 *   less(numeric, numeric)  -> usial less in ariphmetics sence.
 *   less(string,  string )  -> Compares lexicographically
 * </pre>
 **/
public class LessTransformer extends AbstractBuildinTransformer
{

    
 public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
  { return static_transform(t,system,ctx); }


static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
  if (t.getArity()!=2) {
     throw new AssertException("less must have arity 2");
  }
  LogHelper.log(system,LessTransformer.class,"less, t=", t);
  Term frs = t.getSubtermAt(0);
  Term snd = t.getSubtermAt(1);
  Term retval=t;
  if (frs.isBoolean()) {
      if (snd.isBoolean()) {
          ctx.setChanged(true);
          retval = system.getInstance().getTermFactory().createBoolean(frs.getBoolean()==false && snd.getBoolean()==true);
      }
  }else if (frs.isNumber() && snd.isNumber()) {
      ctx.setChanged(true);
      retval=system.getInstance().getTermFactory().createBoolean(frs.termCompare(snd) < 0);
  }else if (frs.isString() && snd.isString() ) {
      ctx.setChanged(true);
      retval=system.getInstance().getTermFactory().createBoolean(frs.termCompare(snd) < 0);
  }
  LogHelper.log(system,LessTransformer.class,"return ",retval);
  return retval;
}
  
 public String getDescription() {
     return STATIC_DESCRIPTION_; 
 }
 
 public String getName() {
     return "less";
 }
 
 private final static String STATIC_DESCRIPTION_ = 
  " less(x,y) = x < y \n"+
  " <pre> \n"+
  "   less(boolean, boolean)  -> usial less in ariphmetics sence. \n" +
  "   less(numeric, numeric)  -> usial less in ariphmetics sence. \n" +
  "   less(string,  string )  -> Compares lexicographically \n"+
  " </pre>"
  ;

    public boolean internalsAtFirst() {
        return false;
    }
 
}
 