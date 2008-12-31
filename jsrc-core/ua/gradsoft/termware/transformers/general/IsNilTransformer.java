package ua.gradsoft.termware.transformers.general;


/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2005
 * $Id: IsNilTransformer.java,v 1.2 2007-01-26 19:27:19 rssh Exp $
 */

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
            

/**
 * isNil(x) = true if term 'x' is nil;
 */
public class IsNilTransformer extends AbstractBuildinTransformer
{

    private IsNilTransformer(){}
    
    public static IsNilTransformer INSTANCE = new IsNilTransformer();
    
 public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
  { return static_transform(t,system,ctx); }


static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
  //if (!t.getName().equals("isNil")) return t;
  if (t.getArity()!=1) {
     return t;
  }  
  ctx.setChanged(true);
  return TermFactory.createBoolean(t.getSubtermAt(0).isNil());
 }


 public String getDescription() {
     return "isNil(x) = true if term 'x' is nil.";
 }
 
 public String getName() {
     return "isNil";
 }

    public boolean internalsAtFirst() {
        return false;
    }
 
}
 