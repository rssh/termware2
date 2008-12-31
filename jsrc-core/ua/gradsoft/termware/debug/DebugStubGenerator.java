/*
 * DebugStubGenerator.java
 *
 * Created on July 2, 2007, 5:19 AM
 *
 */

package ua.gradsoft.termware.debug;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.ToolProvider;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareInstance;
import ua.gradsoft.termware.exceptions.AssertException;
import ua.gradsoft.termware.exceptions.ExternalException;
import ua.gradsoft.termware.util.RuleTransformer;

/**
 *Generate debug stubs.
 */
public class DebugStubGenerator {
    
    /** Creates a new instance of DebugStubGenerator */
    public DebugStubGenerator(TermWareInstance instance) {
        instance_=instance;
    }
    
    public Class<? extends UnificationDebugStub> generateUnificationDebugStub(Term inPattern) throws TermWareException
    {
      return generateDebugStub(UnificationDebugStub.class,DebugStubTypeTraits.UNIFICATION,inPattern,TermLocationDirection.BEGIN_LINE,inPattern);
    }
    
    public Class<? extends SubstitutionDebugStub> generateSubstitutionDebugStub(Term outPattern) throws TermWareException
    {
      return generateDebugStub(SubstitutionDebugStub.class,DebugStubTypeTraits.SUBSTITUTION,outPattern,TermLocationDirection.BEGIN_LINE,outPattern);
    }
    
    public Class<? extends SetFactsDebugStub> generateSetFactsDebugStub(Term action) throws TermWareException
    {
      return generateDebugStub(SetFactsDebugStub.class,DebugStubTypeTraits.ACTION,action,TermLocationDirection.BEGIN_LINE,action);    
    }

    public Class<? extends SubstitutionDebugStub> generateFactsSubstitutionDebugStub(Term action) throws TermWareException
    {
      return generateDebugStub(SubstitutionDebugStub.class,DebugStubTypeTraits.ACTION_SUBSTITUTION,action,TermLocationDirection.END_LINE,action);
    }
    
    public Class<? extends ConditionDebugStub> generateConditionDebugStub(Term condition) throws TermWareException
    {
      return generateDebugStub(ConditionDebugStub.class,DebugStubTypeTraits.CONDITION,condition,TermLocationDirection.BEGIN_LINE,null);  
    }
    
    private String inputFileToOutputPackage(String fullFname)
    {
      // 1. delete extension (.def or .java)
      String current=fullFname;  
      int extDotPosition = fullFname.lastIndexOf(".");
      if (extDotPosition!=-1) {
          current=fullFname.substring(0,extDotPosition);
      }
      // 2. transform '/' to '.'
      current=current.replace('/','.');
      return current;
    }
    
    private List<String> prepareJavacOptions()
    {
       return Collections.singletonList("-g"); 
    }
    
    private <T> Class<? extends T > generateDebugStub(Class<T> tClass, DebugStubTypeTraits traits, Term locationSource, TermLocationDirection direction, Object rule) throws TermWareException
    {
      try{  
        SourceCodeLocation scl = null;
        switch(direction) {
            case BEGIN_LINE: 
                scl = SourceCodeAccessHelper.getLocationOfTermBegin(locationSource);   
                break;
            case END_LINE:
                scl = SourceCodeAccessHelper.getLocationOfTermEnd(locationSource);
                break;
            default:
                scl = SourceCodeAccessHelper.getLocationOfTerm(locationSource);
                break;            
        }
        String fileName=scl.getFileName();
        String packageName=inputFileToOutputPackage(fileName);        
        String className=packageName+"."+traits.getClassPrefix()+scl.getBeginLine();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector dl = new DiagnosticCollector();           
        DebugStubCompilationFileManager fm=new DebugStubCompilationFileManager(compiler.getStandardFileManager(dl,Locale.getDefault(),Charset.defaultCharset()));                                      
        fm.addLocation(className,scl);
        traits.setFileManagerTermWareSourceObject(fm,className,rule);
         DebugStubJavaSourceObject generatedJava = fm.getJavaDebugStubSourceInput(TERMWARE_LOCATION,className,Kind.SOURCE);
         //System.err.println("generated java:"+generatedJava.getCharContent(true));
         List<JavaFileObject> fos = Collections.<JavaFileObject>singletonList(generatedJava);         
         CompilationTask task=compiler.getTask(null,fm,dl,prepareJavacOptions(),null,fos);
         boolean result=task.call();         
         List<Diagnostic> diagnostics = dl.getDiagnostics();
         for(Diagnostic d:diagnostics) {
             System.err.println(d.getMessage(null));
         }
         byte[] compiledClass=fm.getCompiled(className);
         if (compiledClass==null) {
             throw new AssertException("Compilation of debug stub failed.");
         }
         byte[] smapClass = SMAPHelper.insertSmap(compiledClass,generatedJava.getSMap());         
         DebugStubClassLoader loader = new DebugStubClassLoader(className,smapClass);
         Class myClass = loader.getStubClass();
         //Object o = myClass.newInstance();
         return (Class<? extends T>)myClass;
       }catch(IOException ex){
           throw new ExternalException(ex);           
       //}catch(InstantiationException ex){
       //    throw new ExternalException(ex);           
       //}catch(IllegalAccessException ex){
       //    throw new ExternalException(ex);
       }
      }               
             

    static final String UNIFICATION_DEBUG_STUB_PREFIX="U";
    static final String SUBSTITUTION_DEBUG_STUB_PREFIX="S";
    static final String ACTION_DEBUG_STUB_PREFIX="A";
    static final String ACTION_SUBSTITUTION_DEBUG_STUB_PREFIX="F";
    static final String CONDITION_DEBUG_STUB_PREFIX="C";
    
    static final Location TERMWARE_LOCATION = new Location(){
        public String getName() { return "TermWare"; }
        public boolean isOutputLocation() { return true; }
    };
    
    private TermWareInstance instance_;
}
