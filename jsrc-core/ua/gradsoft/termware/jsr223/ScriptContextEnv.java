/*
 * ScriptContextEnv.java
 *
 *(C) GradSoft Ltd, Kiev, Ukraine.
 *http://www.gradsoft.ua
 */

package ua.gradsoft.termware.jsr223;

import java.io.PrintWriter;
import java.io.Reader;
import javax.script.ScriptContext;
import ua.gradsoft.termware.IEnv;
import ua.gradsoft.termware.TermWareException;

/**
 *Environment, which passed to system throught ScriptingContext
 * @author rssh
 */
class ScriptContextEnv implements IEnv {
    
    /** Creates a new instance of ScriptContextEnv */
    public ScriptContextEnv(ScriptContext scriptContext) {
        scriptContext_=scriptContext;
        output_=new PrintWriter(scriptContext.getWriter());
        logWriter_=new PrintWriter(scriptContext.getErrorWriter());
    }
    
    
    /**
     * get standard output stream.
     **/ 
    public PrintWriter   getOutput() {
        return output_;
    }
    
    /**
     * get standard input stream.
     **/
    public Reader   getInput() {
        return scriptContext_.getReader(); }
    
    /**
     * get standard log stream.
     **/
    public PrintWriter   getLog() {
        return logWriter_; }
   
    public void show(TermWareException ex) {
        ex.printStackTrace(logWriter_);
        logWriter_.flush();
    }
   
    public void close() {
        output_.flush();
        logWriter_.flush();
    }
    
    private PrintWriter   output_;
    private PrintWriter   logWriter_;
    private ScriptContext scriptContext_;
    
}
