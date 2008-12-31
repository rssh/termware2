/*
 * TermWareScriptEngine.java
 *
 *
 *
 */

package ua.gradsoft.termware.jsr223;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.Substitution;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareInstance;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.debug.JavaCompiledFileAndLine;
import ua.gradsoft.termware.debug.SourceCodeLocation;
import ua.gradsoft.termware.exceptions.ExternalException;
import ua.gradsoft.termware.parsers.terms.TermReader;
import ua.gradsoft.termware.parsers.terms.util.StringIndex;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

/**
 *JSR-223 Compatible scripting engine.
 * @author rssh
 */
public class TermWareScriptEngine extends AbstractScriptEngine implements Compilable, Invocable {
    
    
    
    /**
     * Creates a new instance of TermWareScriptEngine
     */
    TermWareScriptEngine(TermWareScriptEngineFactory factory, TermWareInstance instance) throws TermWareException {
        factory_=factory;
        instance_=instance;
        termSystem_=new TermSystem(new FirstTopStrategy(),new DefaultFacts(),instance);
        TermWare.addGeneralTransformers(termSystem_);   
    }
    
    
    
    public ScriptEngineFactory  getFactory() {
        return factory_; }
    
    public Object eval(Reader reader, ScriptContext ctx) throws ScriptException {
        String fname = (String)ctx.getAttribute(ScriptEngine.FILENAME);
        if (fname==null) {
            fname="<unknown>";
        }
        TermReader termReader = new TermReader(reader,fname,0,instance_);
        return eval(termReader,ctx);
    }
    
    
    public Object eval(String text, ScriptContext ctx) throws ScriptException {
        SourceCodeLocation scl =  JavaCompiledFileAndLine.deduceFileAndLine(3);
        if (!text.endsWith(";")) {
            text=text+";";
        }
        TermReader termReader = new TermReader(new StringReader(text),scl.getFileName(),scl.getBeginLine(),instance_);
        return eval(termReader,ctx);
    }
    
    public CompiledScript compile(Reader reader) throws ScriptException {
        String fname = (String)this.get(ScriptEngine.FILENAME);
        if (fname==null) {
            fname="<unknown>";
        }
        TermReader termReader = new TermReader(reader,fname,0,instance_);
        return compile(termReader);
    }
    
    public CompiledScript compile(String text) throws ScriptException {
        SourceCodeLocation scl =  JavaCompiledFileAndLine.deduceFileAndLine(3);
        if (!text.endsWith(";")) {
            text=text+";";
        }
        TermReader termReader = new TermReader(new StringReader(text),scl.getFileName(),scl.getBeginLine(),instance_);
        return compile(termReader);
    }
    
    public  Bindings createBindings() {
        Bindings retval = new SimpleBindings();
        //context.setBindings(retval,ScriptContext.ENGINE_SCOPE);
        return retval;
    }
    
    public <T>  T getInterface(Object o, Class<T> oClass) {
        if (o.getClass().isAssignableFrom(oClass)) {
            return (T)o;
        }else if (o instanceof Term){
            Term t = (Term)o;
            if (oClass.isInterface()) {
                Class<?>[] classes = new Class<?>[1];
                classes[0]=oClass;
                return (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),classes, new TermWareTermInvocationHandler(t,this));
            }else{
                throw new IllegalArgumentException();
            }
        }else if (o instanceof TermSystem) {
            if (oClass.isInterface()) {
                TermSystem ts = (TermSystem)o;
                Class<?>[] classes = new Class<?>[1];
                classes[0]=oClass;
                return (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),classes, new TermWareSystemInvocationHandler(ts,this));
            }else{
                throw new IllegalArgumentException();
            }
        }else{
            throw new IllegalArgumentException();
        }
    }
    
    public <T>  T getInterface(Class<T> tClass) {
        if (tClass.isInterface()) {
            Class<?>[] classes = new Class<?>[1];
            classes[0]=tClass;
            return (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),classes, new TermWareSystemInvocationHandler(termSystem_,this));
        }else{
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * form term function(arg1,...argN) and try to interpret one.
     */
    public Object invokeFunction(String function, Object ... args) throws ScriptException, NoSuchMethodException {
        try {
            Term[] targs = new Term[args.length];
            for(int i=0; i<args.length; ++i){
                targs[i] = instance_.getTypeConversion().adopt(args[i]);
            }
            Term fTerm = instance_.getTermFactory().createTerm(function,targs);
            TransformationContext ctx = new TransformationContext();
            // TODO: think about saving previous engine scope ?
            //   or may be current behaviour more strict.
            //createSubstitution(ctx,context.getBindings(ScriptContext.ENGINE_SCOPE),);
            Term rTerm = termSystem_.reduce(fTerm,ctx);
            if (!ctx.isChanged()) {
                throw new NoSuchMethodException();
            }
            return instance_.getTypeConversion().getAsObject(rTerm);
        }catch(TermWareException ex){
            throw new ScriptException(ex);
        }
    }
    
    public Object invokeMethod(Object o, String m, Object ... args) throws ScriptException, NoSuchMethodException {
        try {            
            Term[] targs;        
            if (args!=null) {
              targs = new Term[args.length];
              for(int i=0; i<args.length; ++i){
                targs[i] = instance_.getTypeConversion().adopt(args[i]);
              }
            }else{
              targs = new Term[0];  
            }
            Term fTerm = instance_.getTermFactory().createTerm(m,targs);
            Term rTerm = null;
            Object retval = null;
            TransformationContext ctx = new TransformationContext();
            if (o instanceof TermSystem) {
                TermSystem ts = (TermSystem)o;
                rTerm = ts.reduce(fTerm,ctx);
            }else if (o instanceof Term) {
                Term t = (Term)o;
                Term applyTerm = instance_.getTermFactory().createTerm("apply",t,fTerm);
                rTerm = termSystem_.reduce(applyTerm,ctx);
            }else{
                // try normal invoke
                Class<?> oClass = o.getClass();
                Class<?>[] argsClasses = new Class<?>[args.length];
                for(int i=0; i<args.length; ++i){
                    argsClasses[i]=args[i].getClass();
                }
                Method method = oClass.getDeclaredMethod(m,argsClasses);
                if (method==null) {
                    throw new NoSuchMethodException();
                }else{
                    try{
                        retval = method.invoke(o,args);
                    }catch(IllegalAccessException ex){
                        throw new ExternalException(ex);
                    }catch(InvocationTargetException ex){
                        throw new ExternalException(ex);
                    }
                    ctx.setChanged(true);
                }
            }
            if (!ctx.isChanged()) {
                throw new NoSuchMethodException();
            }
            if (retval==null && rTerm!=null) {
                retval = instance_.getTypeConversion().getAsObject(rTerm);
            }
            return retval;
        }catch(TermWareException ex){
            throw new ScriptException(ex);
        }
    }
    
    private Object eval(TermReader termReader,ScriptContext ctx) throws ScriptException {
        try {
            Term t = termReader.readStatementWrapped();
            StringIndex si = termReader.getStringIndex();
            return eval(t,si,ctx);
        }catch(TermWareException ex){
            throw new ScriptException(ex);
        }
    }
    
    private CompiledScript compile(TermReader termReader) throws ScriptException {
        try {
            Term t = termReader.readStatementWrapped();
            StringIndex si = termReader.getStringIndex();
            return new CompiledTermWareScript(t,si,this);
        }catch(TermWareException ex){
            throw new ScriptException(ex);
        }
    }
    
    Object eval(Term t, StringIndex si, ScriptContext ctx) throws ScriptException {
        try {
            // TODO: think about creating new system ?
            TransformationContext tctx = new TransformationContext();
            tctx.setEnv(new ScriptContextEnv(ctx));
            createSubstitution(tctx,ctx.getBindings(ScriptContext.ENGINE_SCOPE),si);
            t = t.subst(tctx.getCurrentSubstitution());
            t=termSystem_.reduce(t,tctx);
            Object retval = instance_.getTypeConversion().getAsObject(t);
            substituteBinding(tctx,ctx.getBindings(ScriptContext.ENGINE_SCOPE),si);
            return retval;
        }catch(TermWareException ex){
            throw new ScriptException(ex);
        }
    }
    
    
    private void createSubstitution(TransformationContext tctx,Bindings bindings,StringIndex si) throws TermWareException {
        for(Map.Entry<String,Object> e : bindings.entrySet()) {
            int number = si.getIndex(e.getKey());
            Object value = e.getValue();
            Term t = instance_.getTypeConversion().adopt(value);
            tctx.getCurrentSubstitution().put(instance_.getTermFactory().createX(number),t);
        }
    }
    
    private void substituteBinding(TransformationContext ctx, Bindings bindings, StringIndex si) throws TermWareException {
        Substitution s = ctx.getCurrentSubstitution();
        for(Map.Entry<String,Integer> e: si.getMap().entrySet()) {
            Term t = s.get(e.getValue());
            if (t!=null) {
                bindings.put(e.getKey(),instance_.getTypeConversion().getAsObject(t));
            }
        }
    }
    
    public TermWareInstance getInstance() {
        return instance_; }
    
    public TermSystem getTermSystem() {
        return termSystem_; }
    
    private TermWareScriptEngineFactory factory_;
    
    private TermWareInstance            instance_;
    private TermSystem                  termSystem_;
}
