package ua.gradsoft.termware.transformers.sys;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2005
 * $Id: GetPropertyTransformer.java,v 1.3 2008-03-24 22:33:10 rssh Exp $
 */

import java.util.*;
import ua.gradsoft.termware.*;
import ua.gradsoft.termware.util.*;
import ua.gradsoft.termware.exceptions.*;

/**
 *                           
 * getProperty(x) - reduced to property with name x.getName() of current system
 *   for now, only one property - 'debug' is supported.
 */
public class GetPropertyTransformer extends AbstractBuildinTransformer
{
   
 public  Term  transform(Term t, TermSystem sys, TransformationContext ctx) throws TermWareException
  { return static_transform(t,sys,ctx); }
 

 static public  Term  static_transform(Term t, TermSystem sys,  TransformationContext ctx) throws TermWareException
 {
  if (!t.getName().equals("getProperty")) return t;
  if (t.getArity()!=1) {
     throw new AssertException("getProperty must have arity 1");
  }  
  if (t.getSubtermAt(0).getName().equals("debug")) {
     ctx.setChanged(true); 
     return TermFactory.createBoolean(sys.isLoggingMode());
  }else{
     throw new AssertException("unknown property of term system:"+t.getName());
  }
 }

 
 public String getDescription() {
     return staticDescription_;
 }
 
 public String getName() {
     return "getProperty";
 }
 
 private static final String staticDescription_ =
     "getProperty(x) - reduced to property with name x.getName() of current system\n"+
     "   for now, only one property - 'debug' is supported";
 
}
 