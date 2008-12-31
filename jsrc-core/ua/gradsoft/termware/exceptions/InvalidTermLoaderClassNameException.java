/*
 * InvalidTermLoaderClassNameException.java
 *
 */

package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.TermWareException;

/**
 * Throws during initia;ization or setting own termloader,
 *when
 * @author Ruslan Shevchenko
 */
public class InvalidTermLoaderClassNameException extends TermWareException
{
    
    /** Creates a new instance of InvalidTermLoaderClassNameException */
    public InvalidTermLoaderClassNameException(String name) {
        super("Can't find termloader class:"+name);
    }
    
    
}
