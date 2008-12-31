package ua.gradsoft.termware.transformers.general;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2005
 * $Id: ToBooleanTransformer.java,v 1.1 2005-04-02 18:29:23 rssh Exp $
 */

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.exceptions.ConversionException;
import ua.gradsoft.termware.util.LogHelper;


                           
/**
 * transformer for converting numeric and string values to boolean
 **/
public class ToBooleanTransformer extends AbstractBuildinTransformer
{


public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
  return static_transform(t,system,ctx);
 }

static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
  if (t.getArity()!=1) { 
   return t;
  }else{
   try {
     boolean b=t.getSubtermAt(0).getAsBoolean(system.getInstance());
     ctx.setChanged(true);
     return TermFactory.createBoolean(b);
   }catch(ConversionException ex){
     LogHelper.log(system,ToBooleanTransformer.class,ex.getMessage());
     // do nothing - not convertable.
   }
  }
  return t;
 }


 public String getDescription() {
     return "transform term to boolean";
 }
 
 public String getName() {
     return "toBoolean";
 }

    public boolean internalsAtFirst() {
        return true;
    }
 
}
 