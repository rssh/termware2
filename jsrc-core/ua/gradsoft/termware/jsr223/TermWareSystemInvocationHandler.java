/*
 * TermWareSystemInvocationHandler.java
 *
 */

package ua.gradsoft.termware.jsr223;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import javax.script.ScriptException;
import ua.gradsoft.termware.TermSystem;

/**
 *Invocation handler for TermSystem.
 * Obj.f(x1..xn) interpreted as redusing of term f(x1...xN) in system.
 * @author rssh
 */
public class TermWareSystemInvocationHandler implements InvocationHandler
{
    
    /** Creates a new instance of TermWareSystemInvocationHandler */
    public TermWareSystemInvocationHandler(TermSystem termSystem, TermWareScriptEngine engine) {
        termSystem_=termSystem;
        engine_=engine;
    }
    
    public  Object invoke(Object proxy, Method m, Object[] args) throws ScriptException, NoSuchMethodException
    {
       String methodName = m.getName();
       return engine_.invokeMethod(termSystem_,methodName,args);
    }
    
    
    private TermSystem termSystem_;
    private TermWareScriptEngine engine_;
}
