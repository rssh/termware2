package ua.gradsoft.termware.transformers.general;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2005
 * $Id: ToIntTransformer.java,v 1.1 2005-04-02 18:29:23 rssh Exp $
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
 * transformer for converting to int terms.
 **/
public class ToIntTransformer extends AbstractBuildinTransformer
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
     int value=t.getSubtermAt(0).getAsInt(system.getInstance());
     ctx.setChanged(true);
     return system.getInstance().getTermFactory().createInt(value);
   }catch(ConversionException ex){
     LogHelper.log(system,ToLongTransformer.class,ex.getMessage());
     // do nothing - not convertable.
   }
  }
  return t;
 }


 public String getDescription() {
     return "convert to int term if possible";
 }
 
 public String getName() {
     return "toInt";
 }

    public boolean internalsAtFirst() {
        return true;
    }
 
}
 