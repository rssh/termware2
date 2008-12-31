/*
 * T5.java
 *
 * Created 22, 01, 2004, 0:43
 */

package ua.gradsoft.termwaretests.old;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;

/**
 *Check, that cons(NIL,NIL) is not matched with cons(X,NIL)
 *  where X is atom, not NIL and not propositional variable
 * @author  Ruslan Shevchenko
 */
public class T5 extends AbstractTest2 {
    
    /** Creates a new instance of T5 */
    public T5() {
    }
    
    /**
     * check, that unify(cons(NIL,NIL),cons(X,nil)) is true.
     */
    public boolean runTest(IEnv env) {
        //System.out.println("1");
        boolean retval=false;
        try {
            Term t1=TermWare.getInstance().getTermFactory().createTerm("cons",
                    TermWare.getInstance().getTermFactory().createNil(),
                    TermWare.getInstance().getTermFactory().createNil()
                    );
            Term t2=TermWare.getInstance().getTermFactory().createTerm("cons",
                    TermWare.getInstance().getTermFactory().createAtom("X"),
                    TermWare.getInstance().getTermFactory().createNil()
                    );
            Substitution s = new Substitution();
            boolean r=t1.freeUnify(t2, s);
            if (!r) {
                retval=true;
            }
        }catch(TermWareException ex){
            env.getLog().println("T5: exception"+ex.toString());
            ex.printStackTrace(env.getLog());
        }
        return retval;
    }
    
}
