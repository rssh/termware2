/*
 * ConditionDebugStub.java
 *
 * Created on July 9, 2007, 10:22 AM
 *
 *(C) GradSoft Ltd, Kiev, Ukraine.
 *http://www.gradsoft.ua
 */

package ua.gradsoft.termware.debug;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;

/**
 *
 * @author rssh
 */
public abstract class ConditionDebugStub {
    
    /** Creates a new instance of ConditionDebugStub */
    public ConditionDebugStub(TermSystem ts, Term c, TransformationContext ctx) throws TermWareException
    {
        system=ts;
        condition=c;
        transformationContext=ctx;
        result=system.checkFact(condition,transformationContext);
    }
    
    public abstract boolean getResult();
    
    
    protected TermSystem system;
    protected Term condition;
    protected TransformationContext transformationContext;
    protected boolean result;
    
}
