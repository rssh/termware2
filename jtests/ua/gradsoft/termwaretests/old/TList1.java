/*
 * TList1.java
 *
 * Created 22, 03 2004, 15:51
 */

package ua.gradsoft.termwaretests.old;

import ua.gradsoft.termware.*;


/**
 *test that cons(X,NIL) can be unified with cons($0,$1)
 * @author  Ruslan Shevchenko
 */
public class TList1 extends AbstractTest2 {
    
    /** Creates a new instance of TList1 */
    public TList1() {
    }
    
    public boolean runTest(IEnv env)
    {
      try {  
        Term t1=TermWare.getInstance().getTermFactory().createTerm("cons",TermWare.getInstance().getTermFactory().createAtom("x"),
                                              TermWare.getInstance().getTermFactory().createNIL());
        Term t2=TermWare.getInstance().getTermFactory().createTerm("cons", TermWare.getInstance().getTermFactory().createX(0),
                                                TermWare.getInstance().getTermFactory().createX(1));
        Substitution s=new Substitution();
        boolean rc=t1.freeUnify(t2, s);
        return rc;
      }catch(TermWareException ex){
          System.err.println(ex.getMessage());
          ex.printStackTrace();
          return false;
      }
    }
    
}
