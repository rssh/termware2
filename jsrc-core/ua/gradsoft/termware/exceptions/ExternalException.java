/*
 * ExternalException.java
 *
 */

package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.TermWareException;

/**
 *Wrapper for external exception
 */
public class ExternalException extends TermWareException {
    
    /**
     * Creates a new instance of <code>ExternalException</code>.
     */
    public ExternalException(Exception ex) {
        super(ex.getMessage(),ex);
        if (ex instanceof ExternalException) {
            ex_=((ExternalException)ex).getException();
        }else{
            ex_=ex;
        }
    }
    
    /**
     * Creates a new instance of <code>ExternalException</code>.
     */
    public ExternalException(String message,Exception ex) {
        super(message,ex);
        if (ex instanceof ExternalException) {
            ex_=((ExternalException)ex).getException();
        }else{
            ex_=ex;
        }
    }    
    
    public  Exception getException()
    { return ex_; }
    
    public  void rethrow() throws Exception
    { throw ex_; }
    
    private Exception ex_;
    
}
