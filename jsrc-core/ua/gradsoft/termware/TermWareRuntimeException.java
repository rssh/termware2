/*
 * RuntimeTermWareException.java
 *
 * Created  10, 03, 2004, 1:17
 */

package ua.gradsoft.termware;

import ua.gradsoft.termware.exceptions.ExternalException;

/**
 *Runtime holder for TermWare exceptions.
 * @author  Ruslan Shevchenko
 */
public class TermWareRuntimeException extends RuntimeException implements ITermWareException
{
    
    
    public TermWareRuntimeException(Exception ex) {
        super(ex.getMessage(),ex);
        if (ex instanceof TermWareException) {
          ex_=(TermWareException)ex;
        }else{
          ex_=new ExternalException(ex);
        }
    }
    
    /**
     * rethrow holded exception
     */
    public void rethrow() throws TermWareException
    { throw ex_; }
    
    public TermWareException getTermWareException() 
    { return ex_; }
    
    private TermWareException ex_;
    
}
