/*
 * DebugStubTypeTraits.java
 *
 *(C) Grad-Soft Ltd, Kiev, Ukraine.
 *http://www.gradsoft.ua
 */

package ua.gradsoft.termware.debug;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.util.RuleTransformer;

/**
 *Traits of debug stub. Used by generator.
 * @author rssh
 */
public enum DebugStubTypeTraits {
    
   UNIFICATION(UnificationDebugStub.class,DebugStubGenerator.UNIFICATION_DEBUG_STUB_PREFIX)
   {
     public void setFileManagerTermWareSourceObject(DebugStubCompilationFileManager fm, String className, Object o)
     {
      fm.addSourceTerm(className,(Term)o);
     }
   }
   ,
   SUBSTITUTION(SubstitutionDebugStub.class,DebugStubGenerator.SUBSTITUTION_DEBUG_STUB_PREFIX)
   {
     public void setFileManagerTermWareSourceObject(DebugStubCompilationFileManager fm, String className, Object o)
     {
      fm.addSourceTerm(className,(Term)o);   
     }       
   }
   ,
   ACTION(SetFactsDebugStub.class,DebugStubGenerator.ACTION_DEBUG_STUB_PREFIX) 
   {
     public void setFileManagerTermWareSourceObject(DebugStubCompilationFileManager fm, String className, Object o)
     {
      fm.addSourceTerm(className,(Term)o);   
     }              
   }
   ,
   ACTION_SUBSTITUTION(SubstitutionDebugStub.class,DebugStubGenerator.ACTION_SUBSTITUTION_DEBUG_STUB_PREFIX)
   {
     public void setFileManagerTermWareSourceObject(DebugStubCompilationFileManager fm, String className, Object o)
     {
      fm.addSourceTerm(className,(Term)o);   
     }                     
   },
   CONDITION(ConditionDebugStub.class,DebugStubGenerator.CONDITION_DEBUG_STUB_PREFIX)
   {
     public void setFileManagerTermWareSourceObject(DebugStubCompilationFileManager fm, String className, Object o)
     {
      fm.addSourceTerm(className,(Term)o);            
     }                            
   }
   ;
         
   
   DebugStubTypeTraits(Class<?> debugStubClass, String classPrefix)
   {
     debugStubClass_ = debugStubClass;  
     classPrefix_ = classPrefix;
   }

   public Class<?> getDebugStubClass()
   { return debugStubClass_; }
   
   public String  getClassPrefix()
   { return classPrefix_; }

   public abstract void setFileManagerTermWareSourceObject(DebugStubCompilationFileManager fm, String className, Object o);
   
   private final Class<?>   debugStubClass_;
   private final String     classPrefix_; 
    
}
