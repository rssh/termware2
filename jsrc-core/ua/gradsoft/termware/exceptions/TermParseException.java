package ua.gradsoft.termware.exceptions;


import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.parsers.terms.ParseException;


public class TermParseException extends TermWareException
{
 public TermParseException(String message)
  { super("parsing:"+message); }

 public TermParseException(String message, Exception ex)
  { super(message,ex); }
 
 
}