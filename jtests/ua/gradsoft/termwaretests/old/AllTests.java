package ua.gradsoft.termwaretests.old;


import java.io.*;
import java.lang.reflect.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.envs.*;


public class AllTests
{

  
 
  public String[] tests={ 
     "T1" ,"T2" ,"T3_1","T3_2","T3","T4","T5","String.Subst1Test","T6","GenTransformers.TestNeq" ,
     "TList1", "TList2" 
  };
  
  private static boolean inJUnit = false;
  private static String  failedTest="none";

  public static void main(String[] args)
  {
   AllTests all=new AllTests(); 
   all.runAll(args);
  }
  
  public static boolean isInJUnit()
  { return inJUnit; }
  
  public static void setInJUnit(boolean value)
  { inJUnit=value; }
  
  public static String getFailedTest()
  { return failedTest; }
  
  public boolean runAll(String[] args) 
  {
   boolean retval=true;
   for(int i=0; i<tests.length; ++i) {
       boolean testResult=false;
       try {
         testResult = runTest(tests[i], args);
         if (testResult) {
             ++nSuccess;
         }else{
             System.err.println("test "+tests[i]+" failed");
             ++nFailures;
         }
       }catch(ClassNotFoundException ex){
         System.err.println("some class for "+tests[i]+" is not found");
         return false;
       }
       retval &= testResult;
       // for JUnit: return after first failure
       if (!retval) {
           failedTest=tests[i];
           return retval;
       }
   }
   return retval;
  }


  public boolean runTest(String classNameWithoutTestPackage, String[] args)
                                                 throws ClassNotFoundException
  {
    Class testClass=Class.forName("ua.gradsoft.termwaretests.old."+classNameWithoutTestPackage);
    Object o;
    try {
      o=testClass.newInstance();
    }catch(InstantiationException ex){
      System.err.println("Can't create new instance of "+classNameWithoutTestPackage);
      System.err.println(ex.getMessage());
      return false;
    }catch(IllegalAccessException ex){
      System.err.println("Can't create new instance of "+classNameWithoutTestPackage);
      System.err.println(ex.getMessage());
      return false;
    }    
    boolean testSuccess=false;
    if (o instanceof AbstractTest1) {
        AbstractTest1 test=(AbstractTest1)o;
        testSuccess=test.runTest(args);
    }else if (o instanceof AbstractTest2) {
        IEnv env=new SystemEnv();
        if (!termWareSingletonInitialized_) {
            // TODO: Make LogSysEnv ?
            try {
               TermWare.getInstance().init(args);
            }catch(TermWareException ex){
                System.err.println("error during TermWareSingleton.init(env,args);");
                testSuccess=false;
            }
            termWareSingletonInitialized_=true;
        }
        AbstractTest2 test=(AbstractTest2)o;
        testSuccess=test.runTest(env);
    }
    if (!inJUnit) {
      System.out.print(classNameWithoutTestPackage);
      if (testSuccess) {
        System.out.println(":success");
      }else{
        System.out.println(":failure");
      }
    }
    return testSuccess;
  }

  private int nSuccess=0;
  private int nFailures=0;
  private boolean termWareSingletonInitialized_=false;

}