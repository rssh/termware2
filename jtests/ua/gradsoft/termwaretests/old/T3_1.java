/*
 * T3_1.java
 *
 */

package ua.gradsoft.termwaretests.old;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.envs.*;
import ua.gradsoft.termware.strategies.*;


/**
 * check functionality of TermFactory.createParsedTerm
 */
public class T3_1 extends AbstractTest1
{
    
    /** Creates a new instance of T3_1 */
    public T3_1() {
    }

    public boolean runTest(String[] args) 
    {
      try {
        TermWare.getInstance().setOptions(args);
        Term t=TermWare.getInstance().getTermFactory().createParsedTerm("{1,2,3,4,qqq,7,8}");
        return true;
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
      T3_1 t3_1=new T3_1();
      boolean success=t3_1.runTest(args);
      if (success) {
          System.out.println("T3 success");
      }else{
          System.out.println("T3 failure");
      }
    }
    
}
