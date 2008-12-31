/*
 * DebugStubJavaSourceObject.java
 *
 * Created on July 4, 2007, 1:38 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.termware.debug;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;

/**
 *Base class for debug stubs.
 * @author rssh
 */
public abstract class DebugStubJavaSourceObject extends SimpleJavaFileObject
{
    
    /** Creates a new instance of DebugStubJavaSourceObject */
    public DebugStubJavaSourceObject(String javaFname, String packageName, String shortClassName) throws URISyntaxException
    {
        super(new URI("file://"+javaFname),Kind.SOURCE);      
        name_=shortClassName;
        packageName_=packageName;
        javaFname_=javaFname;
    }
    
    /**
     *return generated Java source code.
     */
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
    		return contents_;
    }    

    protected abstract void generateContent();
    
    /**
     *get SMAP file, defined in JSR-45
     */ 
    public CharSequence getSMap()
    {
        return smap_;
    }
    

    /**
     * Java source, which we generate.
     */
    protected String     contents_;
    
    /**
     *smap file
     */
    protected String     smap_;
    
    /**
     * short name of class U<N>
     */
    protected String name_;
    
    /**
     * name of pavkage. (usially - domain name of system)
     */
    protected String packageName_;
            
    protected String javaFname_;
    
    
}
