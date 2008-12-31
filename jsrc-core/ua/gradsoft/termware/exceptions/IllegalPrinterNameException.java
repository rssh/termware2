/*
 * IllegalPrinterNameException.java
 *
 */

package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.ITermWareException;

/**
 *
 * @author  Ruslan Shevchenko
 */
public class IllegalPrinterNameException extends IllegalArgumentException implements ITermWareException
{
    
    /** Creates a new instance of IllegalPrinterNameException */
    public IllegalPrinterNameException(String languageName) {
        super("No printer for language:"+languageName);        
    }
    
}
