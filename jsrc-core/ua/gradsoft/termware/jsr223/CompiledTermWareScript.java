/*
 * CompiledTermWareScript.java
 *
 * Created on July 17, 2007, 6:10 PM
 *
 */

package ua.gradsoft.termware.jsr223;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.parsers.terms.util.StringIndex;

/**
 * Term with associations between names and free variables.
 *(For fast reductions without parsing)
 */
class CompiledTermWareScript extends CompiledScript
{
    
    /** Creates a new instance of CompiledTermWareScript */
    CompiledTermWareScript(Term t, StringIndex xIndex, TermWareScriptEngine engine) {
        t_=t;
        xIndex_=xIndex;
        engine_=engine;
    }
    
    public Object eval(ScriptContext ctx) throws ScriptException
    {
      return engine_.eval(t_,xIndex_,ctx);  
    }
    
    public ScriptEngine getEngine()
    { return engine_; }
    
    private Term t_;
    private StringIndex xIndex_;
    private TermWareScriptEngine engine_;
}
