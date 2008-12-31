package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.TermWareException;

/**
 * Throwed, when limit of free variables enumeration is reached.
 **/
public class FVLimitReachedException extends TermWareException
{
 public FVLimitReachedException()
 { super("Too many free variables to renumerate"); }
}
