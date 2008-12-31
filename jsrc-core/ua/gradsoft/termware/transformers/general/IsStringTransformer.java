package ua.gradsoft.termware.transformers.general;


/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2005
 * $Id: IsStringTransformer.java,v 1.2 2007-08-04 08:54:44 rssh Exp $
 */

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
            

/**
 * isString(x) = true if term 'x' is a string;
 */
public class IsStringTransformer extends AbstractBuildinTransformer
{

 public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
  { return static_transform(t,system,ctx); }


static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
  //if (!t.getName().equals("isString")) return t;
  if (t.getArity()!=1) {
     return t;
  }  
  ctx.setChanged(true);  
  return TermFactory.createBoolean(t.getSubtermAt(0).isString());
 }


 public String getDescription() {
     return "isString(x) = true if term 'x' is a string.";
 }
 
 public String getName() {
     return "isString";
 }

    public boolean internalsAtFirst() {
        return false;
    }
 
}
 