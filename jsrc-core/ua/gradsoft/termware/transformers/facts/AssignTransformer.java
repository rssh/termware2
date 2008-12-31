/*
 * AssignTransformer.java
 *
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.transformers.facts;

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.util.LogHelper;

/**
 *Assign - assigment. Called only from facts database.
 * @author Ruslan Shevchenko
 */
public class AssignTransformer extends AbstractBuildinTransformer
{
    
    public static AssignTransformer INSTANCE = new AssignTransformer();
    
      public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx); }
    
    
  
    static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException 
    {        
      if (system.isLoggingMode())  {
          LogHelper.log(system,AssignTransformer.class,"assign for ",t);
          LogHelper.log(system,AssignTransformer.class,"system="+system.toString());
      }
      if (t.getArity()==2) {
          Term var = t.getSubtermAt(0);
          Term value = t.getSubtermAt(1);
          if (var.isX()) {
              value=system.reduce(value);
              ctx.getCurrentSubstitution().put(var, value);
              t=value;
              ctx.setChanged(true);
              if (system.isLoggingMode()) {
                  LogHelper.log(system,AssignTransformer.class,"assign: substituted to ",value);
              }
          }else{
              if (system.isLoggingMode()) {
                  LogHelper.log(system,AssignTransformer.class,"assign do nothing: first argument not isX");
              }
          }
      }else{
          if (system.isLoggingMode()) {
              LogHelper.log(system,AssignTransformer.class,"assign do nothing, arity!=2");
          }
      }      
      return t;
    }
 
     public String getName() {
        return "assign";
    }
    
    public String getDescription() {
        return staticDescription_;
    }
    
    
    static String staticDescription_=
             "  assign(x,y) - reduce <code> y </code> ant set set received value to propositional valiable <code> x </code>.\n";      
    
}
