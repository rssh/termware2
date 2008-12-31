/*
 * T2Facts.java
 *
 * Created  19/01/2004, 2:39
 */

package ua.gradsoft.termwaretests.old;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;

/**
 *Show's how to pass information from facts to rules.
 * @author  Ruslan Shevchenko
 */
public class T4Facts extends DefaultFacts 
{
    
    /** Creates a new instance of T2Facts */
    public T4Facts() throws TermWareException
    {
        super("T4");
        x_=0;
    }
    
    /**
     * add to current set of substitutions association between propositional
     *  variable <code> t </code> and integer x.
     *  (just set propositional variable <code> t </code> to <code> x </code>)
     **/
    public void fillX(TransformationContext ctx, Term t) throws TermWareException
    {
        if (!t.isX()) {
           throw new AssertException("t is not X");
        }
        Substitution s = ctx.getCurrentSubstitution();
        s.put(t,TermFactory.createInt(x_));
        //System.err.print("putted into ");
        //t.print(System.err);
        //System.err.println(" "+x_);
    }

    public int  getX()
    {
     return x_;
    }    

    public void setX(int x)
    {
        x_=x;
    }
    
    
    private int x_;
    
}
