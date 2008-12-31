/*
 * DebugStubCompilationFileManager.java
 *
 * Created on July 3, 2007, 11:14 PM
 *
 */

package ua.gradsoft.termware.debug;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.TreeMap;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.util.RuleTransformer;

/**
 *Implementation of debug file manager, which keep on-line created in-memory input
 *and in-memory output.
 * @author rssh
 */
public class DebugStubCompilationFileManager extends ForwardingJavaFileManager
{
    
    /** Creates a new instance of DebugStubCompilationFileManager */
    public DebugStubCompilationFileManager(JavaFileManager delegated) {
        super(delegated);
    }
    
    
    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws IllegalStateException {@inheritDoc}
     */
    public JavaFileObject getJavaFileForInput(Location location,
                                              String className,
                                              Kind kind)
        throws IOException
    {
        DebugStubJavaSourceObject stub = getJavaDebugStubSourceInput(location,className,kind);
        return (stub!=null) ? stub : super.getJavaFileForInput(location,className,kind);
    }

    public DebugStubJavaSourceObject getJavaDebugStubSourceInput(Location location,
                                              String className,
                                              Kind kind)
        throws IOException
    {
        if (location.getName().equals("TermWare") && kind==Kind.SOURCE) {
          String[] nameComponents = className.split("\\.");  
          String lastNameComponent = nameComponents[nameComponents.length-1];
          String packageName=className.substring(0,className.lastIndexOf("."));
          String javaFname=className.replace('.','/')+".java";     
          SourceCodeLocation scl = generatedLocations_.get(className);
          if (scl==null) {
              throw new RuntimeException("source code location for "+className+" is not set");
          }
          Term sourceTerm = sourceTerms_.get(className);
          if (lastNameComponent.startsWith(DebugStubGenerator.UNIFICATION_DEBUG_STUB_PREFIX)) {
             try {
               return new UnificationDebugStubJavaSourceObject(javaFname,packageName,lastNameComponent,scl);
             }catch(URISyntaxException ex){
                 // impossible.
                 throw new RuntimeException("Can't create debug stub",ex);
             }
          }else if(lastNameComponent.startsWith(DebugStubGenerator.SUBSTITUTION_DEBUG_STUB_PREFIX)){
             try {
               return new SubstitutionDebugStubJavaSourceObject(javaFname,packageName,lastNameComponent,scl);
             }catch(URISyntaxException ex){
                 // impossible.
                 throw new RuntimeException("Can't create debug stub",ex);
             }              
          }else if(lastNameComponent.startsWith(DebugStubGenerator.ACTION_DEBUG_STUB_PREFIX)){
             try {
               return new SetFactsDebugStubJavaSourceObject(javaFname,packageName,lastNameComponent,scl);
             }catch(URISyntaxException ex){
                 // impossible.
                 throw new RuntimeException("Can't create debug stub",ex);
             }             
          }else if(lastNameComponent.startsWith(DebugStubGenerator.ACTION_SUBSTITUTION_DEBUG_STUB_PREFIX)){
             try {
               return new SubstitutionDebugStubJavaSourceObject(javaFname,packageName,lastNameComponent,scl);
             }catch(URISyntaxException ex){
                 // impossible.
                 throw new RuntimeException("Can't create debug stub",ex);
             }                            
          }else if(lastNameComponent.startsWith(DebugStubGenerator.CONDITION_DEBUG_STUB_PREFIX)) {
             try {
               return new ConditionDebugStubJavaSourceObject(javaFname,packageName,lastNameComponent,scl);
             }catch(URISyntaxException ex){
                 // impossible.
                 throw new RuntimeException("Can't create debug stub",ex);
             }                                          
          }else{
              // impossible.
              throw new RuntimeException("name "+lastNameComponent+" does not starts with known prefix");
          }
        }else{
          throw new RuntimeException("Location must be 'TermWare'");
        }
    }
    
    
    /**
     * create new ByteCodeOutputFileObject
     */
    public JavaFileObject getJavaFileForOutput(Location location,
                                               String className,
                                               Kind kind,
                                               FileObject sibling)
        throws IOException
    {
      try {  
        BytecodeOutputFileObject retval = new BytecodeOutputFileObject(location.getName());
        outputs_.put(className,retval);
        return retval;
      }catch(URISyntaxException ex){
          throw new IOException("Can't open file in memory",ex);
      }
    }
    
    public void addLocation(String className, SourceCodeLocation location)
    {
      generatedLocations_.put(className,location);  
    }
    
    public void addSourceTerm(String className, Term t)
    {
      sourceTerms_.put(className,t);  
    }
    
    public byte[]  getCompiled(String className)
    {
      BytecodeOutputFileObject bofo = outputs_.get(className);
      if (bofo==null) {
          return null;
      }
      return bofo.getBytes();
    }
    
    
    /**
     * Location of generated sources.
     */
    private Map<String,SourceCodeLocation> generatedLocations_ = new TreeMap<String,SourceCodeLocation>();
    
    /**
     * source terms.
     */
    private Map<String,Term> sourceTerms_ = new TreeMap<String,Term>();
    
    /**
     * key is class name, value is generated java code.
     */
    private Map<String,JavaFileObject> inputs_ = new TreeMap<String,JavaFileObject>();
    
    /**
     * key is class name, value is bytecode (without SMAP file)
     */
    private Map<String,BytecodeOutputFileObject> outputs_ = new TreeMap<String,BytecodeOutputFileObject>();
    
}
