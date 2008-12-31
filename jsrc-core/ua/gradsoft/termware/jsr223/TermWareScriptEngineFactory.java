/*
 * TermWareScriptEngineFactory.java
 *
 */

package ua.gradsoft.termware.jsr223;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import ua.gradsoft.termware.StringTerm;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 *JSR 223 compatible ScriptEngine factory for termware.
 * @author rssh
 */
public class TermWareScriptEngineFactory implements ScriptEngineFactory {
    
    
    
    public String getEngineName()
    { return NAME; }
    
    public String getEngineVersion()
    { return TermWare.VERSION; }
    
    public List<String> getExtensions()
    { return Collections.singletonList("def"); }
    
    public String getLanguageName()
    { return NAME; }
    
    public String getLanguageVersion()
    { return TermWare.VERSION; }
    
    public String getMethodCallSyntax(String obj,String m,String ... args)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(obj);
        sb.append(".");
        sb.append(m);
        sb.append("(");
        boolean isFirst=true;
        for(int i=0; i<args.length; ++i) {
            if (isFirst) {
                isFirst=false;
            }else{
                sb.append(",");
            }
            sb.append(args[i]);
        }
        sb.append(")");
        return sb.toString();
    }
    
    public String getOutputStatement(String string)
    {
       Term stringTerm = new StringTerm(string);
       StringWriter swr = new StringWriter();
       PrintWriter pwr = new PrintWriter(swr);
       pwr.print("printString(");
       stringTerm.print(pwr);
       pwr.print(")");
       pwr.flush();
       return swr.toString(); 
    }

    /**
     *return program, which sequentially evaluate all statements.
     *(in our case, we have sequential_reduce transformer, which
     * do this).
     */
    public String getProgram(String ... statements)
    {
      StringBuilder sb=new StringBuilder();
      sb.append("JSR223Program([");
      boolean isFirst=true;
      for(int i=0; i<statements.length; ++i) {
          if (isFirst) {
              isFirst=false;
          }else{
              sb.append(",");
          }
          sb.append(statements[i]);
      }
      sb.append("]);");
      return sb.toString();
    }
    
    public List<String>  getMimeTypes()
    { return Collections.singletonList("Application/X-TermWare-Script"); }
    
    public List<String>  getNames()
    {
        return Arrays.asList("TermWare","termware");
    }
    
    public Object getParameter(String key)
    {
      return parameters_.get(key);  
    }
    
    
    public ScriptEngine  getScriptEngine() 
    { 
      try {  
        return new TermWareScriptEngine(this, TermWare.getInstance()); 
      }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }
    }

    private static  TreeMap<String,Object> parameters_;
    static {
        parameters_=new TreeMap<String,Object>();
        parameters_.put(ScriptEngine.ENGINE,"TermWare");
        parameters_.put(ScriptEngine.ENGINE_VERSION, TermWare.VERSION);
        parameters_.put(ScriptEngine.NAME,"TermWare");
        parameters_.put(ScriptEngine.LANGUAGE,"TermWare");
        parameters_.put(ScriptEngine.LANGUAGE_VERSION,TermWare.VERSION);
        
        parameters_.put("THREADING","MULTITHREADED");        
    }

    public static final String NAME = "TermWare";
    
}
