/*
 * XMLTermParserFactory.java
 *
 * Created 24, 07, 2005, 7:19
 *
 * $Id: XMLTermParserFactory.java,v 1.2 2006-07-13 20:38:41 rssh Exp $
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.parsers.xml.terms;
import java.io.Reader;
import ua.gradsoft.termware.IParser;
import ua.gradsoft.termware.IParserFactory;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareInstance;

/**
 *Implementation of ITermParserFactory for XML terms representation.
 *@see IParserFactory
 *@see XMLTermParser
 */
public class XMLTermParserFactory implements IParserFactory
{
    
  /**
   * create parser object.
   **/                            
   public IParser  createParser(Reader in, String inFname, Term arg, TermWareInstance instance)  throws TermWareException
   {
      return new XMLTermParser(in,inFname,instance); 
   }

    
    
}
