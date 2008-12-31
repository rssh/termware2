/*
 * InvalidListTermException.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.transformers.list;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Exception is throwed, when we receive incorrect list term
 *in context, where one is required.
 * @author Ruslan Shevchenko
 */
public class InvalidListTermException extends TermWareException
{
    
    public InvalidListTermException(Term t)
    {
      super("invalid list term");
      t_=t;  
    }
    
    private Term t_;
}
