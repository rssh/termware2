package ua.gradsoft.termware.transformers.general;

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002
 * $Id: ArityTransformer.java,v 1.2 2007-03-19 11:21:39 rssh Exp $
 */

     
/**
 * arity transformer
 *  arity(t(x1 .. xn)) = n
 **/
public class ArityTransformer extends AbstractBuildinTransformer
{

    private ArityTransformer() {};
    
    public static ArityTransformer INSTANCE = new ArityTransformer();
    
 public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
  { return static_transform(t,system,ctx); }


static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
  //if (!t.getName().equals("arity")) return t;
  if (t.getArity()!=1) {
     return t;
  }  
  ctx.setChanged(true);
  return TermFactory.createInt(t.getSubtermAt(0).getArity());
 }


 public String getName() {
     return "arity";
 }
 
 public String getDescription() {
     return "arity(t(x1..xn)) -> n";
 }
   
 
}
 