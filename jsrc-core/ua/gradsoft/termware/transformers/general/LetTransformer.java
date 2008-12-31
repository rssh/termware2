package ua.gradsoft.termware.transformers.general;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2005
 * $Id: LetTransformer.java,v 1.3 2007-08-04 08:54:44 rssh Exp $
 */

import java.io.*;
import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.util.LogHelper;

                          

/**
 * LetTransformer - add argument as rule to current system.
 * usage:
 * <pre>
 *   let rule  -- introduce rule into current system.
 *   let ( $v1 <- expr1, ,.. $vN <- exprN ) expr - substitute v1, .. vN by reductions of expr1...exprN, and put v1..vN into current substitution.
 * </pre>
 **/
public class LetTransformer extends AbstractBuildinTransformer
{

 private LetTransformer()
 {}
 
 public static final LetTransformer INSTANCE = new LetTransformer();

    
 public Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
   return static_transform(t,system,ctx);
 }
 
 public static Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
   if (system.isLoggingMode()) {  
      LogHelper.log(system,LetTransformer.class,"let: rule=",t);
   }

   if (t.getArity()==1) {
       Term retval = static_transform_addrule(t,system,ctx);
       if (system.isLoggingMode()) {
           LogHelper.log(system,LetTransformer.class,"let return:",retval);           
       }
       return retval;
   }else if (t.getArity()==2) {
       Term retval=static_transform_letexpr(t.getSubtermAt(0),t.getSubtermAt(1),system,ctx);
       if (system.isLoggingMode()) {
           LogHelper.log(system,LetTransformer.class,"let substitution:",ctx.getCurrentSubstitution());
           LogHelper.log(system,LetTransformer.class,"let return:",retval);           
       }
       return retval;
   }else{
       if (system.isLoggingMode()) {
           LogHelper.log(system,LetTransformer.class,"let, arity not match, return unchanded",t);
       }   
       return t;
   }
 }
   
 public static Term  static_transform_addrule(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {       
   system.addRule(t.getSubtermAt(0));
   ctx.setChanged(true);
   return BooleanTerm.getBooleanTerm(true);
 }

 public static Term  static_transform_letexpr(Term assigments, Term resultexpr, TermSystem system, TransformationContext ctx) throws TermWareException
 {       
   ctx.newCurrentSubstitution();  
   while(!assigments.isNil()) {
       Term assigment=assigments.getSubtermAt(0);
       assigments=assigments.getSubtermAt(1);
       Term var = assigment.getSubtermAt(0);
       Term cexpr = assigment.getSubtermAt(1);
       cexpr=system.reduce(cexpr);
       ctx.getCurrentSubstitution().put(var,cexpr);
   }     
   ctx.setChanged(true);
   return resultexpr.subst(ctx.getCurrentSubstitution());
 }



 public String getDescription() {
     return "let(rule) - add rule to system, and return true <BR>"+
            "let(assigments,retval) - perform assigments and substitute retval <BR>";
 }
 
 public String getName() {
     return "let";
 }

 

}
 