/*
 * T1.java
 *
 */

package ua.gradsoft.termwaretests.old;

import ua.gradsoft.termware.*;

/**
 *Test, if we after initialization can load system 'xyz'
 * @author  Ruslan Shevchenko
 */
public class T1 extends AbstractTest1 {
    
    /** Creates a new instance of T1 */
    public T1() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      T1 t1=new T1();
      boolean success=t1.runTest(args);
      if (success) {
          System.out.println("T1 success");
      }else{
          System.out.println("T1 failure");
      }
    }
    
    public  boolean runTest(String[] args) 
    {      
      try {
        TermWare.getInstance().init(args);
        TermSystem t1=TermWare.getInstance().resolveSystem("examples/xyz");
        return true;
      }catch(TermWareException ex){
        System.err.println("eror:"+ex.getMessage());
        ex.printStackTrace();
      }
      return false;
    }
    
}
