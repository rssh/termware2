/*
 * TestNeq.java
 *
 * Created 21, 02, 2004, 10:06
 */

package ua.gradsoft.termwaretests.old.GenTransformers;

import ua.gradsoft.termware.*;
import ua.gradsoft.termwaretests.old.*;

/**
 *Test for neq default transformer
 * @author  Ruslan Shevchenko
 */
public class TestNeq extends AbstractTest2 
{
        
    public boolean runTest(IEnv env) 
    {
     try {
         TermSystem general=TermWare.getInstance().getRoot().resolveSystem("general");
         Term t=TermWare.getInstance().getTermFactory().createParsedTerm("neq(p(x),p(x))");
         Term r=general.reduce(t);
         if (!(r.isBoolean() && r.getBoolean()==false)) {
            System.out.println("r1="+TermHelper.termToString(r));
            return false;
         }
         t=TermWare.getInstance().getTermFactory().createParsedTerm("neq(p(\"x\"),p(\"x\"))");
         r=general.reduce(t);
         if (r.isBoolean()) {
             if (r.getBoolean()==false) {
                 return true;
             }else{
                 return false;
             }
         }else{
             System.out.println("r2="+TermHelper.termToString(r));
             return false;
         }
     }catch(TermWareException ex){
         ex.printStackTrace();
         return false;
     }
    }
    
}
