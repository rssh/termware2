package ua.gradsoft.termware.transformers.general;


/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2005
 * $Id: IsFloatTransformer.java,v 1.1 2005-04-02 18:29:23 rssh Exp $
 */

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
            

/**
 * isFloat(x) = true if term 'x' is a float value;
 */
public class IsFloatTransformer extends AbstractBuildinTransformer
{

 public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
  { return static_transform(t,system,ctx); }


static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
  if (t.getArity()!=1) {
     return t;
  }  
  ctx.setChanged(true);
  return TermFactory.createBoolean(t.getSubtermAt(0).isFloat());
 }


 public String getDescription() {
     return "isFloat(x) = true if term 'x' is a float value.";
 }
 
 public String getName() {
     return "isFloat";
 }

    public boolean internalsAtFirst() {
        return false;
    }
 
}
 