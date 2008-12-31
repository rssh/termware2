package ua.gradsoft.termware.exceptions;
/*
 * (C) Grad-Soft Ltd, Kiev, Ukraine.
 * (C) Ruslan Shevchenko, Kiev, Ukraine.
 */

import ua.gradsoft.termware.TermWareException;


/**
 * Thrown, when we have invalid option in command line
 **/
public class InvalidOptionException extends TermWareException
{

 public InvalidOptionException(String message)
 { super("Invalid option:"+message); }

}
