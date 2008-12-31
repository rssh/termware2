package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;

/**
 * InvalidDomainNameException is throwed,
 * when we call try to resolve syntaxly invalid (or in some cases - unexistent) domain name
 **/
public class InvalidDomainNameException extends TermWareException
{
 public InvalidDomainNameException(String name)
 { super("Invalid domain:"+name); }
 public InvalidDomainNameException(Term name)
 {
     this("");
     Throwable cause;
     cause = new Throwable("Invalid domain name:"+TermHelper.termToString(name),
                              fillInStackTrace());
     initCause(cause);
 }
}
