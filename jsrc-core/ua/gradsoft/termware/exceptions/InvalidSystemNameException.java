package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.TermWareException;

/**
 * InvalidSystemNameException is throwed,
 * when we call resolveSystem mon unexistend name.
 **/
public class InvalidSystemNameException extends TermWareException
{
 public InvalidSystemNameException(String name)
 { super("No such term system:"+name); }
}
