/*
 * AbstractTest2.java
 *
 * Created 22, 01, 2004, 0:01
 */

package ua.gradsoft.termwaretests.old;

import ua.gradsoft.termware.*;

/**
 *Test, when termware already initialized.
 * Caller at first initialize TermWare, then call runTest
 *
 * @author  Ruslan Shevchenko
 */
public abstract class AbstractTest2 {
    
    /** Creates a new instance of AbstractTest2 */
    public AbstractTest2() {
    }
    
    /**
     * run test.
     */
    public abstract boolean runTest(IEnv env);
    
}
