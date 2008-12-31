package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.TermWareException;

/**
 * Exception, throwed in situation, depends from environment.
 * (i. e. analog of java.io.Exception )
 **/
public class EnvException extends TermWareException 
{

 public EnvException(String message)
 { super("env:"+message); }

}
