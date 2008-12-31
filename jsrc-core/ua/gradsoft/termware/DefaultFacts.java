/*
 * part of TermWare
 * (C) Ruslan Shevchenko, 2002-2008
 * $Id: DefaultFacts.java,v 1.14 2008-03-24 22:33:08 rssh Exp $
 */

package ua.gradsoft.termware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import ua.gradsoft.termware.exceptions.AssertException;
import ua.gradsoft.termware.exceptions.ConversionException;
import ua.gradsoft.termware.strategies.FirstTopStrategy;
import ua.gradsoft.termware.transformers.facts.AssignTransformer;




/**
 * this is Facts database, which accept next types of terms:
 *<ul>
 *  <li> sys Terms (i. e. usial logical and ariphmetics operations).
 *  <li> calls of public methodss, declared in DefaultFacts or subclasses of DefaultFacts.
 *   i. e. look at next example:
 *<pre>
 *  class MyFacts extends DefaultFacts
 *  {
 *   ...
 *   public String nearestKnownLocation(int x, int y){
 *     ...
 *   }
 *}
 *</pre>
 *    then setting/checking <code> nearestKnownLocation </code> will be evaluated to 
 *   call of this method.
 *   Usially, all custom facts databases are inheried from DefaultFacts.
 *</ul>
 **/
public class DefaultFacts implements IFacts
{       

 public  DefaultFacts() throws TermWareException
  { this("default"); }

 public  DefaultFacts(String domainName) throws TermWareException
  {
   nullFacts_=new NullFacts();
   // TODO: imperativeStrategy
   ITermRewritingStrategy strategy=new FirstTopStrategy();
   internalSystem_=new TermSystem(strategy,nullFacts_,TermWare.getInstance());
   TermWare.addGeneralTransformers(internalSystem_);
   addFactsTransformers(internalSystem_);
   domainName_=domainName;   
  }

 public String   getDomainName()
  { return domainName_; }

 /**
  * Check term t.
  * <ul>
  *  <li>  reduce term t in internal system.
  *  <li>  if <code> t </code> or subterm of <code> t </code> is a name of method of this
  * class - try to evaluate appropriative method.
  *  <li>  try to check attribute with this term.
  *</ul>
  *@param t  - term to check
  *@param ctx - transformation context
  **/
 public boolean  check(Term t,TransformationContext ctx) throws TermWareException
 {     
  if (isLoggingMode()) {
      if (internalSystem_.checkLoggedEntity("All")||internalSystem_.checkLoggedEntity("Facts")||internalSystem_.checkLoggedEntity(this.getClass().getName())) {
        ctx.getEnv().getLog().print(getClass().getName()+": check ");
        t.print(ctx.getEnv().getLog());
        ctx.getEnv().getLog().println();
      }
  }
  
  // list - process sequentially.   
  if (t.isComplexTerm() && t.getNameIndex().equals(TermWareSymbols.CONS_INDEX) && t.getArity()==2) {
      
      Term curr=t;
      while(!curr.isNil()) {
        if (curr.getArity()!=2) {
            throw new AssertException("arity of cons must be 2");
        }
        check(curr.getSubtermAt(0),ctx);
        curr=curr.getSubtermAt(1);
      }
  }else{
      t=internalSystem_.reduce(t,ctx); 
  }
  if (isLoggingMode()) {
      if (internalSystem_.checkLoggedEntity("All")||internalSystem_.checkLoggedEntity("Facts")||internalSystem_.checkLoggedEntity(this.getClass().getName())) {      
         getEnv().getLog().print(getClass().getName()+":check result ");
         t.print(getEnv().getLog());
         getEnv().getLog().println();
      }
  }
  if (t.isBoolean()) return t.getBoolean();
  else return false;
 }


 public void     set(Term t, TransformationContext ctx) throws TermWareException
 {
  if (internalSystem_.isLoggingMode()) {
    if (internalSystem_.checkLoggedEntity("All")||internalSystem_.checkLoggedEntity("Facts")||internalSystem_.checkLoggedEntity(this.getClass().getName())) {      
      ctx.getEnv().getLog().print(getClass().getName()+": set ");
      t.print(ctx.getEnv().getLog());
      ctx.getEnv().getLog().println();
      ctx.getEnv().getLog().println("internalSystem="+internalSystem_.toString());
    }
  }
  
  // list - process sequentially.   
  if (t.isComplexTerm() && t.getNameIndex().equals(TermWareSymbols.CONS_INDEX) && t.getArity()==2) {      
      Term curr=t;
      while(!curr.isNil()) {
        if (curr.getArity()!=2) {
            throw new AssertException("arity of cons must be 2");
        }
        set(curr.getSubtermAt(0),ctx);
        curr=curr.getSubtermAt(1);
      }
  }else{
      t=internalSystem_.reduce(t,ctx); 
  }
  
  //attributes_.set(t);
 }


 public IEnv getEnv() {
     return internalSystem_.getEnv();
 }
 
 public void setEnv(IEnv env) {
     internalSystem_.setEnv(env);
 }
 
 /**
  * add transformers, based on reflective class analysis.
  */
 private void addFactsTransformers(TermSystem system) throws TermWareException
 { 
   SortedSet<Method> trace=new TreeSet<Method>(new Comparator<Method>(){
       public int compare(Method mx, Method my) {
           int c = mx.getName().compareTo(my.getName());
           if (c!=0) return c;
           c=mx.getParameterTypes().length - my.getParameterTypes().length;
           return c;
       }
       
       public boolean equals(Method x, Method y) {
           return this.compare(x,y)==0;
       }
   });
   system.addNormalizer("assign",AssignTransformer.INSTANCE);
   addFactsTransformers(this.getClass(),system, trace); 
 }
 
 private void addFactsTransformers(Class<?> targetClass, TermSystem system, SortedSet<Method> trace) throws TermWareException
 {
  Method[] methods=targetClass.getDeclaredMethods();
  for(int i=0; i<methods.length; ++i) {
      if (!Modifier.isPublic(methods[i].getModifiers())) {
        //  System.err.println("is not public, skip");
          continue;
      }
      if (trace.contains(methods[i])) {
        //  System.err.println("already exist, skip");
          continue;
      }
      if (methods[i].getName().equals("set")) {
        // skip one
         continue;
      }
      if (system.isLoggingMode()) {
          if (system.checkLoggedEntity("All")||system.checkLoggedEntity("Facts")) {
             getEnv().getLog().println("defaultFacts.addNormalizer "+methods[i].getName());
          }
      }
      system.addNormalizer(methods[i].getName(), 
               new DefaultFacts.MethodTransformer(this, methods[i]));
      trace.add(methods[i]);
  }
  
  // now the same for parents.
  if (!targetClass.equals(Object.class)) {
     addFactsTransformers(targetClass.getSuperclass(),system,trace);
  }
  
 }

 /**
  * print term to standard output of transformation context.
  */
 public void print(TransformationContext ctx, Term t) throws TermWareException
 {
   if (t.isString()) {
     ctx.getEnv().getOutput().print(t.getString());
   }else{
     t.print(getEnv().getOutput());   
   }
   getEnv().getOutput().flush();
 }
 
 /**
  * println term to standard output of transformation context
  */
 public boolean println(TransformationContext ctx,Term t) throws TermWareException
 {
   print(ctx,t);
   ctx.getEnv().getOutput().println();
   ctx.getEnv().getOutput().flush();
   return true;
 }
 
 /**
  * println CR/LF
  */
 public boolean println(TransformationContext ctx) throws TermWareException
 {
   ctx.getEnv().getOutput().println();
   ctx.getEnv().getOutput().flush();
   return true;
 }
 
 /**
  * set stop flag of current transformation.
  *(this will cause stop of term redusing in currenct system)
  */
 public void setCurrentStopFlag(TransformationContext ctx, boolean value) {
     if (internalSystem_.isLoggingMode()) {
         if (internalSystem_.checkLoggedEntity("All")||internalSystem_.checkLoggedEntity(DefaultFacts.class.getName())) {
            ctx.getEnv().getLog().println(getClass().getName()+":set stop flag to "+value);
         }
     }
     ctx.setStop(value);
 }
 
 public boolean isLoggingMode() {
     return internalSystem_.isLoggingMode();
 } 
 
 public void setLoggingMode(boolean mode) {
     internalSystem_.setLoggingMode(mode);
 }
 
 
 
 public void setLoggedEntity(String s) {
     internalSystem_.setLoggedEntity(s);
 }
 
 public void unsetLoggedEntity(String s) {
     internalSystem_.unsetLoggedEntity(s);
 }
 
 public void setLoggedEntities(Collection<String> s) {
     internalSystem_.setLoggedEntities(s);
 }
 
 public void clearLoggedEntities()
 {
   internalSystem_.clearLoggedEntities();  
 }
 
 
 /**
  * Transformer, wrapped arround method.
  */
  public static class MethodTransformer extends AbstractBuildinTransformer
  {
   
   public MethodTransformer(DefaultFacts facts, Method method)
   {
    facts_=facts;
    method_=method;
    Class[] parameterTypes=method.getParameterTypes();
    if (parameterTypes.length!=0) {
        if (parameterTypes[0].equals(TransformationContext.class)) {
            addCtx_=true;
        }else{
            addCtx_=false;
        }
    }else{
        addCtx_=false;
    }
   }
      
   public boolean internalsAtFirst()
   { return true; }
   
   public String getDescription() {
       return "builtin method, see "+method_.getDeclaringClass().getName() +" API documentation for "+method_.getName();
   }
   
   public String getName() {
       return method_.getName();
   }
   
   /**
    * transform term <code> t </code> in system <code> system </code> with transformation context <code> ctx </code>
    */
   public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
       if (system.isLoggingMode()) {
           if (system.checkLoggedEntity("All")||system.checkLoggedEntity("Facts")) {
             system.getEnv().getLog().print(getClass().getName());
             system.getEnv().getLog().println(":apply facts method "+method_.getName()+" to "+TermHelper.termToString(t));
           }
       }
       
    
       Class[] parameterTypes = method_.getParameterTypes();
       if ( !addCtx_ && t.getArity()!=parameterTypes.length ) {
           if (system.isLoggingMode()) {
               if (system.checkLoggedEntity("All")||system.checkLoggedEntity("Facts")) {
                   system.getEnv().getLog().print(getClass().getName());
                   system.getEnv().getLog().println(":arity does not match, skip ");
               }
           }
           return t;
       }else if (addCtx_ && t.getArity()!=parameterTypes.length-1) {
           if (system.isLoggingMode()) {
               if (system.checkLoggedEntity("All")||system.checkLoggedEntity("Facts")) {
                   system.getEnv().getLog().print(getClass().getName());
                   system.getEnv().getLog().println(":arity does not match, skip ");
               }
           }           
           return t;
       }
       int intAddCtx=(addCtx_?1:0);
       Object[] parameters = new Object[parameterTypes.length];
       try {
         if (addCtx_) {
             parameters[0]=ctx;
         }
         for(int i=0; i<parameterTypes.length-intAddCtx; ++i) {
           Term subterm=t.getSubtermAt(i);  
           if (subterm.isComplexTerm()) {
               if (!subterm.getName().equals("array")) {
                   subterm=system.reduce(subterm);
                   t.setSubtermAt(i,subterm);
               }
           }
           parameters[i+intAddCtx]=system.getInstance().getTypeConversion().getAsObjectWithClass(parameterTypes[i+intAddCtx],subterm);
         }
       }catch(ConversionException ex){
           system.getEnv().getLog().println("exception during casting term to java object:"+ex.getMessage());
           system.getEnv().getLog().println("during call of "+method_.getDeclaringClass().getName()+"."+method_.getName() );
           //TODO: log error in debug mode
           return t;
       }
       Object o=null;
       try {
           o = method_.invoke(facts_,parameters);
       }catch(IllegalAccessException ex){
            throw new TermWareRuntimeException(ex);
       }catch(InvocationTargetException ex){
           throw new TermWareRuntimeException(ex);
       }
       if (system.isLoggingMode()) {
           if (system.checkLoggedEntity("All")||system.checkLoggedEntity("Facts")) {
             system.getEnv().getLog().print(getClass().getName());
             if (o==null) {
               system.getEnv().getLog().println(":received null");
             }else{
               system.getEnv().getLog().println(":received java object "+o.getClass().getName()+" "+o.toString());
             }
           }
       }
       ctx.setChanged(true);
       return system.getInstance().getTypeConversion().adopt(o);
   }
   
   private DefaultFacts     facts_;
   private Method           method_;
   private boolean          addCtx_;
   
  }
 
 private String domainName_;
 private IFacts nullFacts_;
 private TermSystem internalSystem_;

}
