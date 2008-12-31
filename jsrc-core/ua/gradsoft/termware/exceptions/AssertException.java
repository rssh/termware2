/*
 * AssertException.java
 *
 */

package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 * Exception, thrown in 'incorrect' situation.
 *@see RuntimeAssertException
 **/
public class AssertException extends TermWareException 
{

    /**
     * Constructs an instance of <code>AssertException</code> with the specified detail message.
     * @param message the detail message.
     */
    public AssertException(String message)
      { super("assert:"+message); }

    /**
     * Constructs an instance of <code>AssertException</code> with the specified detail message.
     * @param message the detail message.
     */
    public AssertException(String message, Exception ex)
      { super("assert:"+message,ex); }
    
    
    /**
     * Constructs an instance of <code>AssertException</code> with the specified detail message.
     * @param message the detail message.
     */
    public AssertException(String message, Term t)
      { super("assert:"+message); 
        t_=t;
      }

    
    Term t_=null;
}
    
