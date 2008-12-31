/*
 * RuntimeAssertException.java
 *
 */

package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 * runtime assert exception - runtime wrapper arround AssertException
 */
public class RuntimeAssertException extends TermWareRuntimeException {
    
    
    /**
     * Creates a new instance of <code>RuntimeAssertException</code>
     * @param msg the detail message.
     */
    public RuntimeAssertException(String msg) {
        super(new AssertException(msg));
    }
    
    
    /**
     * Constructs an instance of <code>RuntimeAssertException</code> with the specified detail message
     *and term as argument.
     * @param msg the detail message.
     * @param t   the term
     */
    public RuntimeAssertException(String msg, Term t) {
        super(new AssertException(msg, t));
    }
        
}
