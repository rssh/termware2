package ua.gradsoft.termware.transformers.general;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2005
 * $Id: ToBigDecimalTransformer.java,v 1.1 2005-04-02 18:29:23 rssh Exp $
 */

import java.math.BigDecimal;
import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.exceptions.ConversionException;
import ua.gradsoft.termware.util.LogHelper;


                           
/**
 * transformer for converting terms to big decimal terms.
 **/
public class ToBigDecimalTransformer extends AbstractBuildinTransformer
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
     BigDecimal bd=t.getSubtermAt(0).getAsBigDecimal(system.getInstance());
     ctx.setChanged(true);
     return TermFactory.createBigDecimal(bd);
   }catch(ConversionException ex){
     LogHelper.log(system,ToBigDecimalTransformer.class,ex.getMessage());
     // do nothing - not convertable.
   }
  }
  return t;
 }


 public String getDescription() {
     return "convert to big decimal term if possible";
 }
 
 public String getName() {
     return "toBigDecimal";
 }

    public boolean internalsAtFirst() {
        return true;
    }
 
}
 