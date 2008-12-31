package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.TermWareException;

/**
 * InvalidFactsNameException is throwed,
 * when facts database with name <code> name </code> 
 * is not registered in TermWare instance. 
 **/
public class InvalidFactsNameException extends TermWareException
{
 public InvalidFactsNameException(String name)
 { super("No such facts domain:"+name); }
}
