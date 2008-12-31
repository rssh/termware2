/*
 * SetFactsDebugStub.java
 *
 * Created on July 2, 2007, 6:39 AM
 */

package ua.gradsoft.termware.debug;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;

/**
 *Debug stub, generated for setting facts.
 * @author rssh
 */
public abstract class SetFactsDebugStub {
    
    public SetFactsDebugStub(TermSystem s, Term a, TransformationContext ctx) throws TermWareException
    {
      system=s;
      action=a;
      transformationContext=ctx;
      system.setFact(action,ctx);
    }
    
    public abstract boolean getResult();    

    private TermSystem system;
    private Term  action;
    private TransformationContext transformationContext;

}
