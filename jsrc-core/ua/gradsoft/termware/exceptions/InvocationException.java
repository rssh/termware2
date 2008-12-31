package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.TermWareException;

/**
 * InvocationException is throwed from JavaLangReflectHelper,
 * when we can't wrap foreign class into one of our interfaces.
 **/
public class InvocationException extends TermWareException
{
 public InvocationException(String message)
 { super(message); }
 
 public InvocationException(Throwable cause)
 {
   super("invocation exception", cause);  
 }
 
 public InvocationException(String message, Exception cause)
 {
   super(message,cause);  
 }
 
}
