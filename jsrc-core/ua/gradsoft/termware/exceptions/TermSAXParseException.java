/*
 * TermSAXParseException.java
 *
 * Created 5, 08, 2005, 12:35
 *
 * Copyright (c) 2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.exceptions;

import org.xml.sax.SAXException;
import ua.gradsoft.termware.TermWareException;

/**
 *Exception which is throwed 
 * @author Ruslan Shevchenko
 */
public class TermSAXParseException extends TermWareException
{
    
    /** Creates a new instance of TermSAXParseException */
    public TermSAXParseException(SAXException ex) {
        super("exception during parsing xml:"+ex.getMessage(),ex);
    }
        
}
