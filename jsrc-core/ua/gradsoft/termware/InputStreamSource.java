/*
 * InputStreamSource.java
 *
 * Created 9, 05, 2005, 7:30
 */

package ua.gradsoft.termware;

import java.io.InputStream;
import ua.gradsoft.termware.exceptions.ExternalException;
import ua.gradsoft.termware.exceptions.ResourceNotFoundException;

/**
 *Source for input stream.
 *(File or ClassPath reference or RDB access scheme)
 * @author Ruslan Shevchenko
 */
public interface InputStreamSource 
{
    
    /**
     * get input stream for target resource.
     */
   public InputStream  getInputStream() throws ExternalException;    
   
   /**
    * get name of resource (such as absolute file name, or so on)
    */
   public String       getResourceName();
   
}
