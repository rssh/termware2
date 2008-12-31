/*
 * JECheck.java
 *
 * 
 */

package ua.gradsoft.termwaredemos.jsa;

import ua.gradsoft.termware.*;

/**
 * Check for empty exceptions in Java classes.
 * 
 * @author  Ruslan Shevchenko
 */
public class JECheck {
    
    /** Creates a new instance of JECheck */
    public JECheck() {
    }
    
    public static void main(String[] args)
    {
      JECheck checker = new JECheck();
      if (checker.init(args)) {
         checker.run();
      }
    }
    
    private boolean init(String[] args)
    {
       if (args.length==0) {
           usage();
           return false;
       }
       for(int i=0; i<args.length; ++i) {
           if (args[i].equals("-f")) {
               if (i==args.length-1) {
                   System.err.println("option -f require argument");
                   return false;
               }else{
                   fname_=args[++i];
               }
           }
       }
       if (fname_==null) {
           System.err.println("You must set name of file to process");
           return false;
       }
       return true;
    }
    
    private void usage()
    {
        System.out.println("JSECheck - program for detecting empty catch clause in Java Program");
        System.out.println(" usage: JSECheck  [-I directory-to-search-java-file] -f java-file ");
        System.out.println("for example: JSECheck -I my-source-dir -f MyClass.java ");        
    }
    
    private void run()
    {
      try {
       // jeSystem_=TermWare.getInstance().getRoot().getDirectSubdomain("examples").resolveSystem("JECheck");
       TermWareInstance instance=TermWare.getInstance();
       Domain root = instance.getRoot();
       Domain examples=root.getDirectSubdomain("examples");
       jeSystem_=examples.resolveSystem("JECheck");
      }catch(TermWareException ex){
          System.err.println("Error during loading of system:"+ex.getMessage());
          return;
      }
      //jeSystem_.setDebugMode(true);
      //jeSystem_.setDebugEntity("Facts");
      try {
        processFile(fname_);
      }catch(TermWareException ex){
             System.err.println("Error during processing file "+fname_+":"+ex.getMessage());
      }
    }
    
    private void processFile(String fname) throws TermWareException
    {
        Term source = TermWare.getInstance().load(fname,TermWare.getInstance().getParserFactory("Java"),TermFactory.createNil());
        source.print(System.out);
        System.out.println();
        
        Term result=jeSystem_.reduce(source);
       
        System.out.println("result:"+TermHelper.termToString(result));
       
        JECheckFacts myFacts=(JECheckFacts)jeSystem_.getFacts();
        
        System.out.println("discovered "+myFacts.getNProblems()+" problems");
        
        //throw new AssertException("Yet not implemented");
    }
    
    private TermSystem jeSystem_;  
    private String fname_=null;
    
}
