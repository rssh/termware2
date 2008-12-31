/*
 * Subst1Test.java
 *
 */

package ua.gradsoft.termwaretests.old.String;

import ua.gradsoft.termware.*;
import ua.gradsoft.termwaretests.old.AbstractTest2;

/**
 *
 * @author  Ruslan Shevchenko
 */
public class Subst1Test extends AbstractStringSystemTest {
    
    /** Creates a new instance of Subst1Test */
    public Subst1Test() {
    }
    
    /**
     * check, that subs("aaa","a","b") is "bbb"
     */
    public boolean runTest(TermSystem sys) throws TermWareException 
    {
        Term t=TermWare.getInstance().getTermFactory().createParsedTerm("subst(\"aaa\",\"a\",\"b\")");
        Term result=sys.apply(t);
        //
        return result.getString().equals("bbb");
    }
    
    private boolean mainRunTest(String[] args) throws TermWareException
    {
        TermWare.getInstance().init(args);
        TermSystem sys=TermWare.getInstance().resolveSystem(TermWare.getInstance().getTermFactory().createAtom("String"));
        //sys.setDebugMode(true);
        //sys.setDebugEntity("All");
        return runTest(sys);
    }
    
    public static void main(String[] args) {
        Subst1Test test=new Subst1Test();
        boolean res=false;
        try {
            res=test.mainRunTest(args);
        }catch(TermWareException ex){
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        System.out.println(""+res);
    }
}
