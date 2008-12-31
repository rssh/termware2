/*
 * T3.java
 *
 * Created  19, 01, 2004, 2:45
 */

package ua.gradsoft.termwaretests.old;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.envs.*;
import ua.gradsoft.termware.strategies.*;


/**
 *
 * @author  Ruslan Shevchenko
 */
public class T3 extends AbstractTest1
{
    
    /** Creates a new instance of T3 */
    public T3() {
    }

    public boolean runTest(String[] args) 
    {
      try {
        TermWare.getInstance().setOptions(args);
        ITermRewritingStrategy strategy=new FirstTopStrategy();
                                                                      
        IFacts defaultFacts = new DefaultFacts();
                                                               
        TermSystem t3=new TermSystem(strategy,defaultFacts);
        if (!AllTests.isInJUnit()) {
          t3.addRule("{ $x: $y } -> $y [ print($x) ] ");
        }else{
          t3.addRule("{ $x: $y } -> $y ");  
        }
        t3.addRule("{ }        -> z               ");
        t3.setLoggingMode(false);
       // t3.setDebugEntity("All");        
        Term t=TermWare.getInstance().getTermFactory().createParsedTerm("{1,2,3,4,qqq,7,8}");        
        Term r=t3.reduce(t);
        if (r.getName().equals("z")) {
            return true;
        }else{
            return false;
        }
      }catch(TermWareException ex){
        System.err.println("eror:"+ex.getMessage());
      }
      return false;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
      T3 t3=new T3();
      boolean success=t3.runTest(args);
      if (success) {
          System.out.println("T3 success");
      }else{
          System.out.println("T3 failure");
      }
    }
    
}
