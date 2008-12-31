package ua.gradsoft.termware.transformers.sys;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2005
 * $Id: DomainTransformer.java,v 1.2 2007-07-13 20:50:21 rssh Exp $
 */

import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.util.*;
import ua.gradsoft.termware.exceptions.*;

                           
/**
 * Transformer for implementing domain statement.
 */
public class DomainTransformer extends AbstractBuildinTransformer
{

 public  boolean internalsAtFirst()
 { return false; }

 public  Term  transform(Term t, TermSystem sys, TransformationContext ctx) throws TermWareException
  { return static_transform(t,sys,ctx); }


 static public  Term  static_transform(Term t, TermSystem sys, TransformationContext ctx) throws TermWareException
 {
  return static_transform(t,sys,ctx,sys.getInstance().getRoot());
 }

 static public  Term  static_transform(Term t, TermSystem sys, TransformationContext ctx,  Domain where) throws TermWareException
 {
  // TODO: check nameIndex instead name   
  if (!t.getName().equals("domain")) return t;
  if (sys.isLoggingMode()) {
    sys.getEnv().getLog().print("native: domain ");
    sys.getEnv().getLog().println();
  }
  if (t.getArity()<2) {
    sys.getEnv().getLog().println("native: domain: arity<2, return unchanged");
    return t;
  }
  Term nameTerm = t.getSubtermAt(0);
  boolean nameIsScoped = false;
  if (!nameTerm.isString() && !nameTerm.isAtom()) {    
    if (nameTerm.isComplexTerm() && nameTerm.getName().equals("_name")) {
      nameIsScoped = true;
    }else{
      if (sys.isLoggingMode()) {
        sys.getEnv().getLog().println("native: domain: first subterm is not name, return unchanged");
      }
      return t;
    }
  }else{
    if (nameTerm.getName().equals("root")) {
      throw new AssertException("domain 'root' is predefined");
    }
  }
  where=where.getOrCreateSubdomain(nameTerm);

  for(int i=1; i<t.getArity(); ++i) {
     Term current = t.getSubtermAt(i);
     if (current.isComplexTerm() && current.getName().equals("system")) {
       SystemTransformer.static_transform(current,sys,ctx,where);
     }else if(current.isComplexTerm() && current.getName().equals("domain")) {
       if (current.getArity()==0) {
          throw new AssertException("domain()");
       }else{
          static_transform(current,sys,ctx,where);
       }
     }else if(current.isComplexTerm() && current.getName().equals("javaFacts")) {
       JavaFactsTransformer.static_transform(current,sys,ctx,where); 
     }else{
       throw new AssertException("'domain' must contains only 'system' or 'domain' or 'facts' subterms");
     }
  }

  if (sys.isLoggingMode()) {
     sys.getEnv().getLog().println("native: domain "+TermHelper.termToString(nameTerm)+": -- added.");
  }
  ctx.setChanged(true);
  return TermFactory.createBoolean(true);
 }

 public String getDescription() {
     return STATIC_DESCRIPTION_;
 }
 
 public String getName() {
     return "domain";
 }
 
 private static String STATIC_DESCRIPTION_=
   "domain(t1,...tn) - add to current runtime definition of domain.\n"+
   "  t1         - name of new-created domain. \n"+
   "  t2 ... tn  - entities of this domain: 'system' or 'domain' or 'facts' subterms"
   ;
 
}
 