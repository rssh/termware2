/*
 * SetFactsDebugStubJavaSourceObject.java
 *
 */

package ua.gradsoft.termware.debug;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Collections;
import ua.gradsoft.termware.Term;

/**
 *Source object for applying action.
 * @author rssh
 */
public class SetFactsDebugStubJavaSourceObject extends DebugStubJavaSourceObject
{
    
    
    public SetFactsDebugStubJavaSourceObject(String javaFname, String packageName, String shortClassName, SourceCodeLocation location) throws URISyntaxException
    {
      super(javaFname,packageName,shortClassName);  
      location_=location;
      generateContent();
    }
    
    @Override
    protected void generateContent()
    {
        StringWriter javaStringWriter = new StringWriter();
        StringWriter smapStringWriter = new StringWriter();
        
        PrintWriter  javaPrintWriter = new PrintWriter(javaStringWriter);
        PrintWriter  smapPrintWriter = new PrintWriter(smapStringWriter);
        
        String shortJavaFname = javaFname_.substring(javaFname_.lastIndexOf("/")+1);
        SMAPHelper.generateHeader(smapPrintWriter,shortJavaFname);
        SMAPHelper.generateStratumSection(smapPrintWriter,"TermWare");
        SourceCodeLocation location=location_;
        SMAPHelper.generateFileSection(smapPrintWriter,1,location.getFileName());
        SMAPHelper.startLineSection(smapPrintWriter);
        int lineNumber=0;
        lineNumber+=JavaGeneratorHelper.generatePackage(javaPrintWriter,packageName_);
        SMAPHelper.generateLineInfo(smapPrintWriter, 1,location,lineNumber);        
        lineNumber+=JavaGeneratorHelper.generateImport(javaPrintWriter,"ua.gradsoft.termware.*");
        lineNumber+=JavaGeneratorHelper.generateImport(javaPrintWriter,"ua.gradsoft.termware.debug.*");
        lineNumber+=JavaGeneratorHelper.generateClassEntry(javaPrintWriter,name_,"SetFactsDebugStub",Collections.<String>emptyList());
        SMAPHelper.generateLineInfo(smapPrintWriter, 1,location,lineNumber);        
        javaPrintWriter.println("{");        
        ++lineNumber;
        SMAPHelper.generateLineInfo(smapPrintWriter, 1,location,lineNumber);        
        javaPrintWriter.print(
                "public "
                );
        javaPrintWriter.print(name_);
        javaPrintWriter.println(
                "(TermSystem ts, Term a, TransformationContext ctx) throws TermWareException {"
                );      
        ++lineNumber;
        SMAPHelper.generateLineInfo(smapPrintWriter,1,location,lineNumber);
        javaPrintWriter.println(
                "super(ts,a,ctx);"
                );   
        ++lineNumber;
        SMAPHelper.generateLineInfo(smapPrintWriter,1,location,lineNumber);        
        javaPrintWriter.println("}");
        ++lineNumber;
        SMAPHelper.generateLineInfo(smapPrintWriter,1,location,lineNumber);        
        javaPrintWriter.println("public boolean getResult() { return true; }");
        ++lineNumber;
        SMAPHelper.generateLineInfo(smapPrintWriter,1,location,lineNumber);        
        javaPrintWriter.println("}");                
        javaPrintWriter.flush();        
        SMAPHelper.generateEndSection(smapPrintWriter);
        smapPrintWriter.flush();
        
        contents_=javaStringWriter.toString();
        smap_=smapStringWriter.toString();
        //System.err.println("Content is:"+contents_);
    }
    
        
    private SourceCodeLocation location_;
    
    
}
