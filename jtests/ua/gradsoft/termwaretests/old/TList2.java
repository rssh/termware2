/*
 * TList2.java
 *
 * Created  15, 04, 2004, 1:37
 */

package ua.gradsoft.termwaretests.old;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.strategies.*;

/**
 * check next test system:
 *<pre>
 *  s([$x:$y]) -> $x+s($y) 
 *  s([]) -> 0
 *</pre>
 * @author  Ruslan Shevchenko
 */
public class TList2 extends AbstractTest2 {
    
    /** Creates a new instance of TList2 */
    public TList2() {
    }
    
      public boolean runTest(IEnv env) {
        try {
        DefaultFacts tlist2Facts=new DefaultFacts();
        ITermRewritingStrategy strategy=new FirstTopStrategy();
                                                             
        TermWare.getInstance().setEnv(env);
        TermSystem tlist2=new TermSystem(strategy,tlist2Facts);
        tlist2.addRule("s([$x:$y]) -> $x+s($y) ");
        tlist2.addRule("s([]) -> 0");
        TermWare.addGeneralTransformers(tlist2);
        //tlist2.setDebugMode(true);
        //tlist2.setDebugEntity("All");
        Term t=TermWare.getInstance().getTermFactory().createParsedTerm("s([3,3])");
        Term r=tlist2.reduce(t);
        if (!r.isInt()) {
            System.err.print("tlist2: r=");
            r.print(System.err);
            System.err.println();
            return false;
        }else {
            return r.getInt()==6;
        }
      }catch(TermWareException ex){
        System.err.println("eror:"+ex.getMessage());
      }
      return false;
    }
    
}
