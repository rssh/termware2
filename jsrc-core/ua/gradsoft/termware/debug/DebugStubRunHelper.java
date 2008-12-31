/*
 * DebugStubRunHelper.java
 *
 * Created on July 5, 2007, 5:57 AM
 *
 */

package ua.gradsoft.termware.debug;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import ua.gradsoft.termware.Substitution;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.exceptions.ExternalException;

/**
 *Helper class to run debug stub.
 * @author rssh
 */
public class DebugStubRunHelper {
    

    /**
     * run unification debug stub.
     *@param stubClass - generated class of stub.
     *@param t - term to unify.
     *@param p - pattern to unify
     *@param s - substitution.
     *@return result of unification.
     */
    public static boolean runUnificationDebugStub(Class<? extends UnificationDebugStub> stubClass,Term t,Term p,Substitution s) throws TermWareException {
        try {
            Constructor stubConstructor = stubClass.getConstructor(Term.class,Term.class,Substitution.class);
            UnificationDebugStub stub = (UnificationDebugStub)stubConstructor.newInstance(t,p,s);
            return stub.getResult();
        }catch(NoSuchMethodException ex){
            throw new ExternalException(ex);
        }catch(InstantiationException ex){
            throw new ExternalException(ex);
        }catch(IllegalAccessException ex){
            throw new ExternalException(ex);
        }catch(InvocationTargetException ex){
            throw new ExternalException(ex);
        }
        
    }

    /**
     * run substitution debug stub.
     *@param stubClass - generated class of stub.
     *@param t - term to substitute. 
     *@param s - substitution.
     *@return substituted term
     */
    public static Term runSubstitutionDebugStub(Class<? extends SubstitutionDebugStub> stubClass,Term t,Substitution s) throws TermWareException {
        try {
            Constructor stubConstructor = stubClass.getConstructor(Term.class,Substitution.class);
            SubstitutionDebugStub stub = (SubstitutionDebugStub)stubConstructor.newInstance(t,s);
            return stub.getResult();
        }catch(NoSuchMethodException ex){
            throw new ExternalException(ex);
        }catch(InstantiationException ex){
            throw new ExternalException(ex);
        }catch(IllegalAccessException ex){
            throw new ExternalException(ex);
        }catch(InvocationTargetException ex){
            throw new ExternalException(ex);
        }        
    }

    /**
     * run action debug stub.
     *@param stubClass - generated class of stub.
     *@param ts - term system, in which we call facts. 
     *@param t  - action term.
     *@param ctx - transformation context
     */
    public static void runSetFactsDebugStub(Class<? extends SetFactsDebugStub> stubClass, TermSystem ts, Term t, TransformationContext ctx) throws TermWareException {
        try {
            Constructor stubConstructor = stubClass.getConstructor(TermSystem.class,Term.class,TransformationContext.class);
            SetFactsDebugStub stub = (SetFactsDebugStub)stubConstructor.newInstance(ts,t,ctx);
            boolean b = stub.getResult();
        }catch(NoSuchMethodException ex){
            throw new ExternalException(ex);
        }catch(InstantiationException ex){
            throw new ExternalException(ex);
        }catch(IllegalAccessException ex){
            throw new ExternalException(ex);
        }catch(InvocationTargetException ex){
            throw new ExternalException(ex);
        }        
    }
    
    /**
     * run condition debug stub.
     *@param stubClass - generated class of stub.
     *@param ts - term system, in which we call facts. 
     *@param t  - condition term.
     *@param ctx - transformation context
     *@return result of condition check.
     */
    public static boolean runConditionDebugStub(Class<? extends ConditionDebugStub> stubClass, TermSystem ts, Term t, TransformationContext ctx) throws TermWareException {
        try {
            Constructor stubConstructor = stubClass.getConstructor(TermSystem.class,Term.class,TransformationContext.class);
            ConditionDebugStub stub = (ConditionDebugStub)stubConstructor.newInstance(ts,t,ctx);
            boolean b = stub.getResult();
            return b;
        }catch(NoSuchMethodException ex){
            throw new ExternalException(ex);
        }catch(InstantiationException ex){
            throw new ExternalException(ex);
        }catch(IllegalAccessException ex){
            throw new ExternalException(ex);
        }catch(InvocationTargetException ex){
            throw new ExternalException(ex);
        }        
    }
    
    
    
}
