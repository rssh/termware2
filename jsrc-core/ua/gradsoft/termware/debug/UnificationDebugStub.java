/*
 * UnificationDebugStub.java
 *
 * Created on July 2, 2007, 3:02 AM
 *
 */

package ua.gradsoft.termware.debug;

import ua.gradsoft.termware.Substitution;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Base for unification debug stub.
 * @author rssh
 */
public abstract class UnificationDebugStub 
{

    public UnificationDebugStub(Term t,Term p,Substitution s) throws TermWareException
    {
      term=t;
      pattern=p;
      substitution=s;
      result=p.freeUnify(t,s);
    }
    
    /**
     * Generated subclasses override call of this method by
     *<code>t.freeUnify(pattern,s)</code>
     */
    public abstract boolean  getResult();
    
    protected Term term;
    protected Term pattern;
    protected Substitution substitution;
    protected boolean result;
    
}
