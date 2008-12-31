/*
 * TermWareTermInvocationHandler.java
 *
 * Created on July 20, 2007, 10:43 PM
 */

package ua.gradsoft.termware.jsr223;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import javax.script.ScriptException;
import ua.gradsoft.termware.Term;

/**
 *Term invocation handler.
 *O.f(x1...xN) interpreted as apply(o,f(x1,...xN)) in system of provided engine.
 * @author rssh
 */
public class TermWareTermInvocationHandler implements InvocationHandler
{
    
    /** Creates a new instance of TermWareInvokable */
    public TermWareTermInvocationHandler(Term term, TermWareScriptEngine engine) {
        term_=term;     
        engine_=engine;
    }
    
    public  Object invoke(Object proxy, Method m, Object[] args) throws ScriptException, NoSuchMethodException
    {
       String methodName = m.getName();
       return engine_.invokeMethod(term_,methodName,args);
    }
    
    private Term term_;
    private TermWareScriptEngine engine_;
}
