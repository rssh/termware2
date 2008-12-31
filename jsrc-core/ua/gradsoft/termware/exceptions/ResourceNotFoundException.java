/*
 * ResourceNotFoundException.java
 *
 * Created  9, 05, 2005, 8:08
 */

package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.TermWareException;

/**
 *Throwed by TermLoader, when we can't find specified
 * resource with appropriative name.
 * @author Ruslan Shevchenko
 */
public class ResourceNotFoundException extends TermWareException
{
    
    private String resourceName_;
    
    /** Creates a new instance of ResourceNotFoundException */
    public ResourceNotFoundException(String resourceName) {
        super("resource not found:"+resourceName);
        resourceName_=resourceName;
    }
    
    /**
     * get name of resource, which was not found.
     */
    public String getResourceName()
    { return resourceName_; }
    
}
