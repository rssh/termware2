/*
 * JavaClassNotFoundException.java
 *
 */

package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.TermWareException;

/**
 *Exception is thrown, when TermWare need some Java class, but it can't
 *be found in current classloader. 
 */
public class JavaClassNotFoundException extends TermWareException
{
    
    /** Creates a new instance of JavaClassNotFoundException */
    public JavaClassNotFoundException(String className) 
    {
      super("java class '"+className+"' is needed, but can't be found.");  
    }
    
}
