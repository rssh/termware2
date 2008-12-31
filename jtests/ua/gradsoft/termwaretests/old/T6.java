/*
 * T6.java
 *
 */

package ua.gradsoft.termwaretests.old;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.strategies.*;


/**
 *Test next situaiton
 *<pre>
 *   1 -> p
 *   $x -> q
 *</pre>
 *result must be next:
 *<pre>
 *  1 -> p
 *  2 -> q
 *</pre>
 * @author  Ruslan Shevchenko
 */
public class T6 extends AbstractTest2 {
    
    /** Creates a new instance of T6 */
    public T6() {
    }
    
    public boolean runTest(IEnv env) {
        try {
        TermWare.getInstance().setEnv(env);    
        DefaultFacts t6facts=new DefaultFacts();
        ITermRewritingStrategy strategy=new FirstTopStrategy();
                                                                       
        TermSystem t6=new TermSystem(strategy,t6facts);
        t6.addRule("pq            -> p   [ setCurrentStopFlag(true)  ] ");
        t6.addRule("OTHERWISE($x) -> q   [ setCurrentStopFlag(true)  ] ");
       // t6.setDebugMode(true);
       // t6.setDebugEntity("All");
        Term t=TermWare.getInstance().getTermFactory().createAtom("pq");
        Term r=t6.reduce(t);
        if (!r.getName().equals("p")) {
            System.err.print("t6: r=");
            r.print(System.err);
            System.err.println();
            return false;
        }
        t=TermWare.getInstance().getTermFactory().createAtom("qq");
        r=t6.reduce(t);
        if (r.getName().equals("q")) {
            return true;
        }else{
            System.err.print("t6: r2=");
            r.print(System.err);
            System.err.println();
            return false;
        }
      }catch(TermWareException ex){
        System.err.println("eror:"+ex.getMessage());
      }
      return false;
    }
    
}
