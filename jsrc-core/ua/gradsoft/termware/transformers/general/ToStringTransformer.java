package ua.gradsoft.termware.transformers.general;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2005
 * $Id: ToStringTransformer.java,v 1.2 2007-01-26 19:27:19 rssh Exp $
 */


import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;



                           
/**
 * transformer for converting to string terms.
 **/
public final class ToStringTransformer extends AbstractBuildinTransformer
{

    private ToStringTransformer() {}
    
    public static ToStringTransformer INSTANCE = new ToStringTransformer();

public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
  return static_transform(t,system,ctx);
 }

static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
  if (t.getArity()!=1) { 
   return t;
  }else{
      Term retval;
      if (t.getSubtermAt(0).isString()) {
          retval=t.getSubtermAt(0);
      }else{
          String s=TermHelper.termToPrettyString(t.getSubtermAt(0));
          retval=system.getInstance().getTermFactory().createString(s);
      }
      ctx.setChanged(true);
      return retval;
  }
 }


 public String getDescription() {
     return "convert to string term";
 }
 
 public String getName() {
     return "toString";
 }

    public boolean internalsAtFirst() {
        return true;
    }
 
}
 