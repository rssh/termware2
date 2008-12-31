/*
 * ReduceTransformer.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.transformers.general;

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.util.LogHelper;

/**
 * Transformer which reduce own argument.
 */
public class ReduceTransformer extends AbstractBuildinTransformer
{
    
 
     public Term transform(Term t,TermSystem system,TransformationContext ctx) throws TermWareException
    {
      return static_transform(t,system,ctx);  
    }
    
    public Term static_transform(Term t,TermSystem system,TransformationContext ctx) throws TermWareException
    {       
       if (t.getArity()==1) {
           Term reduced=t.getSubtermAt(0);           
           if (system.isLoggingMode()) {
               LogHelper.log(system,ReduceTransformer.class,"apply reduce to ", reduced);
           }
           boolean svChanged = ctx.isChanged();
           ctx.setChanged(false);
           reduced = system.reduce(reduced, ctx);          
           if (ctx.isChanged()) {
               if (system.isLoggingMode()) {
                 LogHelper.log(system,ReduceTransformer.class,"changed, retval is ", reduced);               
               }
           }else{
               if (system.isLoggingMode()) {
                 LogHelper.log(system,ReduceTransformer.class,"not changed");
               }
               //ctx.setChanged(svChanged);
           }
           ctx.setChanged(true);
           return reduced;
       }else{
           //TODO: log
           return t;
       }   
    }
    
    public String getName()
    { return "reduce"; }
    
    public String getDescription()
    { return staticDescription_; }
    
    private static String staticDescription_=
            "reduce(t) -- force reduction of <code> t </code>";
}
