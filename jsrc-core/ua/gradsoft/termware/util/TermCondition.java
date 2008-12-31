/*
 * TermCondition.java
 *
 * Copyright (c) 2004-2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.util;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 * *Interface for callback of check function.
 * @author Ruslan Shevchenko
 */
public interface TermCondition {

    /**
     * if term <code> t </code> satisficy condition.
     *@return true, if check was succesfull, otherwise false.
     */
    public boolean check(Term t) throws TermWareException;

    
}
