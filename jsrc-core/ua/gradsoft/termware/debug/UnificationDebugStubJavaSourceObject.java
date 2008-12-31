/*
 * UnificationDebugStubJavaSourceObject.java
 *
 * Created on July 2, 2007, 1:41 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.termware.debug;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import ua.gradsoft.termware.Term;

/**
 *Source object for java debug stubs.
 *Here we generate class with next mapping:
 *<ul>
 * <li>name of class is the same, as name of system</li>
 * <li>for each substituion top-level symbol X (i.e. ray of ITermTransformersStar)
 *    two methods are generated:
 *    <ul>
 *       <li>&lt;X&gt;_checkCondition; - called when unification with pattern is ok,
 *        but we yet not check conditions.
 *       </li>
 *       <li>
 *        &lt;X&gt; -- called when condition is ok, and now we do substitution and action call.
 *       </li>
 *    </ul>
 * </li>
 *</ul>
 * @author Ruslan Shevchenko <it> Ruslan@Shevchenko.Kiev.UA> </it>
 */
public class UnificationDebugStubJavaSourceObject extends DebugStubJavaSourceObject
{
    
    /**
     * Creates a new instance of UnificationDebugStubJavaSourceObject
     */
    public UnificationDebugStubJavaSourceObject(String javaFname, String packageName, String shortClassName, SourceCodeLocation termLocation) 
                throws URISyntaxException
    {       
        super(javaFname,packageName,shortClassName);
        inPatternLocation_=termLocation;
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
        SMAPHelper.generateFileSection(smapPrintWriter,1,inPatternLocation_.getFileName());
        SMAPHelper.startLineSection(smapPrintWriter);
        int lineNumber=0;
        lineNumber+=JavaGeneratorHelper.generatePackage(javaPrintWriter,packageName_);
        SMAPHelper.generateLineInfo(
                smapPrintWriter, 
                1,
                inPatternLocation_,
                lineNumber
                );        
        lineNumber+=JavaGeneratorHelper.generateImport(javaPrintWriter,"ua.gradsoft.termware.*");
      //  SMAPHelper.generateLineInfo(
      //          smapPrintWriter, 
      //          1,
      //          inPatternLocation,
      //          lineNumber
      //          );        
        lineNumber+=JavaGeneratorHelper.generateImport(javaPrintWriter,"ua.gradsoft.termware.debug.*");
      //  SMAPHelper.generateLineInfo(
      //          smapPrintWriter, 
      //          1,
      //          inPatternLocation,
      //          lineNumber
      //          );
        lineNumber+=JavaGeneratorHelper.generateClassEntry(javaPrintWriter,name_,"UnificationDebugStub",Collections.<String>emptyList());
        SMAPHelper.generateLineInfo(smapPrintWriter, 1,inPatternLocation_,lineNumber);        
        javaPrintWriter.println("{");        
        ++lineNumber;
        SMAPHelper.generateLineInfo(smapPrintWriter, 1,inPatternLocation_,lineNumber);        
        javaPrintWriter.print(
                "public "
                );
        javaPrintWriter.print(name_);
        javaPrintWriter.println(
                "(Term t, Term p, Substitution s) throws TermWareException {"
                );      
        ++lineNumber;
        SMAPHelper.generateLineInfo(smapPrintWriter,1,inPatternLocation_,lineNumber);
        javaPrintWriter.println(
                "super(t,p,s);"
                );   
        ++lineNumber;
        SMAPHelper.generateLineInfo(smapPrintWriter,1,inPatternLocation_,lineNumber);        
        javaPrintWriter.println("}");
        ++lineNumber;
        SMAPHelper.generateLineInfo(smapPrintWriter,1,inPatternLocation_,lineNumber);        
        javaPrintWriter.println("public boolean  getResult() { return result; }");
        ++lineNumber;
        SMAPHelper.generateLineInfo(smapPrintWriter,1,inPatternLocation_,lineNumber);        
        javaPrintWriter.println("}");                
        javaPrintWriter.flush();        
        SMAPHelper.generateEndSection(smapPrintWriter);
        smapPrintWriter.flush();
        
        contents_=javaStringWriter.toString();
        smap_=smapStringWriter.toString();
        //System.err.println("Content is:"+contents_);
    }
    
              
    private SourceCodeLocation inPatternLocation_;
    
}
