/*
 * IllegalParserNameException.java
 *
 */

package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.ITermWareException;

/**
 *
 * @author  Ruslan Shevchenko
 */
public class IllegalParserNameException extends IllegalArgumentException implements ITermWareException
{
    
    /**
     * Creates a new instance of <code>IllegalParserNameException</code> without detail message.
     */
    public IllegalParserNameException(String languageName) {
        super("no parser for name:"+languageName);
    }
    
    
}
