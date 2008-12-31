/*
 * T3.java
 *
 * Created  19, 01, 2004, 2:45
 */

package ua.gradsoft.termwaretests.old;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.strategies.*;


/**
 *
 * Show, how to pass information from facts to termsystem.
 * (we pass 4, at first from system to facts, then from facts to system.)
 *
 * @see T4Facts
 * @author  Ruslan Shevchenko
 */
public class T4 extends AbstractTest1
{
    
    /** Creates a new instance of T3 */
    public T4() {
    }

    public  boolean runTest(String[] args) 
    {
      try {
        TermWare.getInstance().init(args);
        ITermRewritingStrategy strategy=new FirstTopStrategy();
                                                                      
        TermSystem t4=new TermSystem(strategy,new T4Facts());
        t4.setReduceFacts(false);

        t4.addRule("x($y) -> y  [ setX($y)  ] ");
        t4.addRule("y     -> $x [ fillX($x) ] ");
        t4.addRule("4     -> z                  ");
        //t4.setDebugMode(true);
        //t4.setDebugEntity("All");
        Term t=TermWare.getInstance().getTermFactory().createParsedTerm("x(4)");
        Term r=t4.reduce(t);
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
      T4 t4=new T4();
      boolean success=t4.runTest(args);
      if (success) {
          System.out.println("T4 success");
      }else{
          System.out.println("T4 failure");
      }
    }
    
}
