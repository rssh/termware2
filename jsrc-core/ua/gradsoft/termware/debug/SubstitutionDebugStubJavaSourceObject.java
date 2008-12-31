/*
 * SubstitutionDebugStubJavaSourceObject.java
 *
 */

package ua.gradsoft.termware.debug;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Collections;
import ua.gradsoft.termware.Term;

/**
 *Debug stub for substitutions.
 * @author rssh
 */
public class SubstitutionDebugStubJavaSourceObject extends DebugStubJavaSourceObject
{
    
    /** Creates a new instance of SubstitutionDebugStubJavaSourceObject */
    public SubstitutionDebugStubJavaSourceObject(String javaFname, String packageName, String shortClassName, SourceCodeLocation termLocation) throws URISyntaxException
    {
      super(javaFname,packageName,shortClassName);  
      //term_=term;
      location_=termLocation;
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
        lineNumber+=JavaGeneratorHelper.generateClassEntry(javaPrintWriter,name_,"SubstitutionDebugStub",Collections.<String>emptyList());
        SMAPHelper.generateLineInfo(smapPrintWriter, 1,location,lineNumber);        
        javaPrintWriter.println("{");        
        ++lineNumber;
        SMAPHelper.generateLineInfo(smapPrintWriter, 1,location,lineNumber);        
        javaPrintWriter.print(
                "public "
                );
        javaPrintWriter.print(name_);
        javaPrintWriter.println(
                "(Term t, Substitution s) throws TermWareException {"
                );      
        ++lineNumber;
        SMAPHelper.generateLineInfo(smapPrintWriter,1,location,lineNumber);
        javaPrintWriter.println(
                "super(t,s);"
                );   
        ++lineNumber;
        SMAPHelper.generateLineInfo(smapPrintWriter,1,location,lineNumber);        
        javaPrintWriter.println("}");
        ++lineNumber;
        SMAPHelper.generateLineInfo(smapPrintWriter,1,location,lineNumber);        
        javaPrintWriter.println("public Term  getResult() { return result; }");
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
    
    
    //private Term term_;
    private SourceCodeLocation location_;
}
