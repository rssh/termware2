/*
 * LogHelper.java
 *
 */

package ua.gradsoft.termware.util;

import ua.gradsoft.termware.Substitution;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;

/**
 *Helper for logging into system environments.
 * @author Ruslan Shevchenko
 */
public class LogHelper {
    
   /**
    * output message <code> message </code> if system <code> sys </code>
    *in logging mode and logged entities are "All" or <code> aClass.getName() </code>.
    */
    public static void log(TermSystem sys,Class aClass,String message)
    {
      if (sys.isLoggingMode()) {
          if (sys.checkLoggedEntity("All")||sys.checkLoggedEntity(aClass.getName())) {
              sys.getEnv().getLog().print(aClass.getName());
              sys.getEnv().getLog().print(":");
              sys.getEnv().getLog().println(message);
          }
      }  
    }

   /**
    * output message <code> message </code> with term <code> t </code> if system <code> sys </code>
    *in logging mode and logged entities are "All" or <code> aClass.getName() </code>.
    */    
    public static void log(TermSystem sys,Class aClass,String message, Term t)
    {
      if (sys.isLoggingMode()) {
          if (sys.checkLoggedEntity("All")||sys.checkLoggedEntity(aClass.getName())) {
              sys.getEnv().getLog().print(aClass.getName());
              sys.getEnv().getLog().print(":");
              sys.getEnv().getLog().print(message);
              t.println(sys.getEnv().getLog());
          }
      }  
    }
    
   /**
    * output message <code> message </code> with substitution <code> s </code> if system 
    * <code> sys </code> in logging mode and logged entities are "All" or 
    * <code> aClass.getName() </code>.
    */    
    public static void log(TermSystem sys, Class aClass, String message, Substitution s)
    {
      if (sys.isLoggingMode()) {
          if (sys.checkLoggedEntity("All")||sys.checkLoggedEntity(aClass.getName())) {
              sys.getEnv().getLog().print(aClass.getName());
              sys.getEnv().getLog().print(":");
              sys.getEnv().getLog().print(message);
              s.print(sys.getEnv().getLog());            
              sys.getEnv().getLog().println();
          }
      }  
    }

   /**
    * output message <code> message </code> if system 
    * <code> sys </code> in logging mode and logged entities are "All" or <code>debugEntity</code>
    * or <code> aClass.getName() </code>.
    */       
    public static void log(TermSystem sys,String debugEntity, Class aClass,String message)
    {
      if (sys.isLoggingMode()) {
          if (sys.checkLoggedEntity("All")||sys.checkLoggedEntity(debugEntity)||sys.checkLoggedEntity(aClass.getName())) {
              sys.getEnv().getLog().print(aClass.getName());
              sys.getEnv().getLog().print(":");
              sys.getEnv().getLog().println(message);
          }
      }  
    }
    
   /**
    * output message <code> message </code> with term <code> t </code> if system 
    * <code> sys </code> in logging mode and logged entities are "All" or <code>debugEntity</code>
    * or <code> aClass.getName() </code>.
    */           
    public static void log(TermSystem sys,String debugEntity, Class aClass,String message, Term t)
    {
      if (sys.isLoggingMode()) {
          if (sys.checkLoggedEntity("All")||sys.checkLoggedEntity(debugEntity)||sys.checkLoggedEntity(aClass.getName())) {
              sys.getEnv().getLog().print(aClass.getName());
              sys.getEnv().getLog().print(":");
              sys.getEnv().getLog().print(message);
              t.println(sys.getEnv().getLog());
          }
      }  
    }

   /**
    * output message <code> message </code> with substitution <code> s </code> if system 
    * <code> sys </code> in logging mode and logged entities are "All" or <code>debugEntity</code>
    * or <code> aClass.getName() </code>.
    */               
    public static void log(TermSystem sys,String debugEntity, Class aClass,String message, Substitution s)
    {
      if (sys.isLoggingMode()) {
          if (sys.checkLoggedEntity("All")||sys.checkLoggedEntity(debugEntity)||sys.checkLoggedEntity(aClass.getName())) {
              sys.getEnv().getLog().print(aClass.getName());
              sys.getEnv().getLog().print(":");
              sys.getEnv().getLog().print(message);              
              s.print(sys.getEnv().getLog());            
              sys.getEnv().getLog().println();
          }
      }  
    }
    
}
