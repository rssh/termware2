/*
 * AbstractStringSystemTest.java
 */

package ua.gradsoft.termwaretests.old.String;

import java.io.*;

import ua.gradsoft.termwaretests.old.AbstractTest2;

import ua.gradsoft.termware.*;


/**
 *
 * @author  Ruslan Shevchenko
 */
public abstract class AbstractStringSystemTest extends AbstractTest2 {
    
    /** Creates a new instance of AbstractStringSystemTest */
    public AbstractStringSystemTest() {
    }
    
    public boolean runTest(IEnv env) {
      try {
         TermSystem sys=TermWare.getInstance().resolveSystem(TermWare.getInstance().getTermFactory().createAtom("String"));
         return runTest(sys);
      }catch(TermWareException ex){
          env.getLog().println(ex.getMessage());
          ex.printStackTrace(env.getLog());
          return false;
      }
    }
    
    public abstract boolean runTest(TermSystem system) throws TermWareException;
    
}
