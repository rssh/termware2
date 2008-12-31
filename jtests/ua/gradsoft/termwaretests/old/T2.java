/*
 * T2.java
 *
 * Created  19, 02, 2004, 2:45
 */

package ua.gradsoft.termwaretests.old;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.strategies.*;


/**
 *
 */
public class T2 extends AbstractTest1
{
    
    /** Creates a new instance of T2 */
    public T2() {
    }

    public  boolean runTest(String[] args) 
    {
      try {
        TermWare.getInstance().init(args);
        T2Facts t2facts=new T2Facts();
        ITermRewritingStrategy strategy=new FirstTopStrategy();
                                                                       
        TermSystem t2=new TermSystem(strategy,t2facts);
        t2.addRule("x->y [ setFlag(true) ]");
        t2.addRule("y    [ getFlag()     ] -> z ");
        //t2.setLoggingMode(true);
        Term t=TermWare.getInstance().getTermFactory().createAtom("x");
        Term r=t2.reduce(t);
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
      T2 t2=new T2();
      boolean success=t2.runTest(args);
      if (success) {
          System.out.println("T2 success");
      }else{
          System.out.println("T2 failure");
      }
    }
    
}
