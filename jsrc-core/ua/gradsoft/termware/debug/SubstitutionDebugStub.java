/*
 * SubstitutionDebugStub.java
 *
 */

package ua.gradsoft.termware.debug;

import ua.gradsoft.termware.Substitution;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Base for debug stub which generated for substitution.
 * @author rssh
 */
public abstract class SubstitutionDebugStub 
{
    
    public SubstitutionDebugStub(Term t, Substitution s) throws TermWareException
    {
      term=t;
      substitution=s;
      result=term.subst(s);
    }

    public abstract Term getResult();
    
    protected Term term;
    protected Substitution substitution;
    protected Term result; 
    
}
