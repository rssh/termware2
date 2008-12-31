package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.TermWareException;


/**
 * Throwed, when some term is internally incorrect.
 **/
public class IncorrectTermException extends TermWareException
{

 public IncorrectTermException(String message)
 { super(message); }

}
