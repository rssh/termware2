/*
 * GenericInputStreamSource.java
 *
 * Created 9, 05, 2005, 15:20
 */

package ua.gradsoft.termware.termloaders;

import java.io.InputStream;
import ua.gradsoft.termware.InputStreamSource;

/**
 *Generic InputStreamSource, which keep inside name and input stream.
 * @author Ruslan Shevchenko
 */
public class GenericInputStreamSource implements InputStreamSource
{
    
    /** Creates a new instance of GenericInputStreamSource */
    public GenericInputStreamSource(InputStream inputStream,String resourceName) {
        inputStream_=inputStream;
        resourceName_=resourceName;
    }
    
    public InputStream getInputStream()
    { return inputStream_; }
    
    public String getResourceName()
    { return resourceName_; }
    
    private InputStream inputStream_;
    private String      resourceName_;
    
}
