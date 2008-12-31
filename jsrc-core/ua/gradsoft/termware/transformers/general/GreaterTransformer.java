package ua.gradsoft.termware.transformers.general;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002, 2003, 2004, 2005
 */


import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.util.LogHelper;
                          
/**
 * greater(x,y) = x > y
 * <pre> 
 *   greater(boolean, boolean)  -> usial less in ariphmetics sence.
 *   greater(numeric, numeric)  -> usial less in ariphmetics sence.
 *   greater(string,  string )  -> Compares lexicographically
 * </pre>
 **/
public class GreaterTransformer extends AbstractBuildinTransformer
{

    
 public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
  { return static_transform(t,system,ctx); }


static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
  if (t.getArity()!=2) {
     throw new AssertException("greater must have arity 2");
  }
  LogHelper.log(system,GreaterTransformer.class,"greater, t=", t);
  Term frs = t.getSubtermAt(0);
  Term snd = t.getSubtermAt(1);
  Term retval=t;
  if (frs.isBoolean()) {
      if (snd.isBoolean()) {
          ctx.setChanged(true);
          retval = system.getInstance().getTermFactory().createBoolean(frs.getBoolean()==true && snd.getBoolean()==false);
      }
  }else if (frs.isNumber() && snd.isNumber()) {
      ctx.setChanged(true);
      retval=system.getInstance().getTermFactory().createBoolean(frs.termCompare(snd) > 0);
  }else if (frs.isString() && snd.isString() ) {
      ctx.setChanged(true);
      retval=system.getInstance().getTermFactory().createBoolean(frs.termCompare(snd) > 0);
  }
  LogHelper.log(system,GreaterTransformer.class,"return ",retval);
  return retval;
}


    public boolean internalsAtFirst() {
        return false;
    }

  
 public String getDescription() {
     return STATIC_DESCRIPTION_; 
 }
 
 public String getName() {
     return "greater";
 }
 
 private final static String STATIC_DESCRIPTION_ = 
  " greater(x,y) = x > y \n"+
  " <pre> \n"+
  "   greater(boolean, boolean)  -> usial greater in ariphmetics sence. \n" +
  "   greater(numeric, numeric)  -> usial greater in ariphmetics sence. \n" +
  "   greater(string,  string )  -> Compares lexicographically \n"+
  " </pre>"
  ;
 
}
 