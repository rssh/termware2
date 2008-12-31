/*
 * T3_2.java
 *
 */

package ua.gradsoft.termwaretests.old;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.envs.*;
import ua.gradsoft.termware.set.*;
import ua.gradsoft.termware.strategies.*;


/**
 * check functionality of creating pattern
 */
public class T3_2 extends AbstractTest1
{
    
    /** Creates a new instance of T3_2 */
    public T3_2() {
    }

    public boolean runTest(String[] args) 
    {
      try {
        TermWare.getInstance().setOptions(args);
        Term t=TermWare.getInstance().getTermFactory().createParsedTerm("{$x:$y}");        
        if (t.getName().equals("set_pattern")) {
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
      T3_2 t3_2=new T3_2();
      boolean success=t3_2.runTest(args);
      if (success) {
          System.out.println("T3 success");
      }else{
          System.out.println("T3 failure");
      }
    }
    
}
