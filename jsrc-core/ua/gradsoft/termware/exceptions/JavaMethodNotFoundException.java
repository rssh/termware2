/*
 * JavaMethodNotFoundException.java
 *
 * Created: 12, 08, 2005, 9:51
 *
 * Copyright (c) 2002-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.TermWareException;

/**
 * Exception which throws when TermWare can't find class method with given name and arity.
 */
public class JavaMethodNotFoundException extends TermWareException
{
    
    
    public JavaMethodNotFoundException(Class objectClass, String name) {
        super("can't find matching method '"+name+"' in class '"+objectClass.getName()+"'");
    }
    
}
