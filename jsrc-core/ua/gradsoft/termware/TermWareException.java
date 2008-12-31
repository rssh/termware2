package ua.gradsoft.termware;

/**
 * Base class for all TermWare exceptions.
 **/
public class TermWareException extends Exception implements ITermWareException
{

 public TermWareException()
 {}
    
 public TermWareException(String message)
 { super(message); }

 public TermWareException(String message, Throwable cause)
 {
   super(message,cause);
 }
 
}
