package ua.gradsoft.termware.exceptions;


import ua.gradsoft.termware.TermWareException;

/**
 * InvalidStrategyNameException is throwed,
 * when we try to call strategy by name, which
 * is not registered in TermWare instance. 
 **/
public class InvalidStrategyNameException extends TermWareException
{
 public InvalidStrategyNameException(String name)
 { super("No such strategy:"+name); }
}
