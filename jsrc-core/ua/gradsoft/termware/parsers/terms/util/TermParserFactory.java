
package ua.gradsoft.termware.parsers.terms.util;

import java.io.*;
import java.util.*;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareInstance;
import ua.gradsoft.termware.IParserFactory;
import ua.gradsoft.termware.IParser;

import ua.gradsoft.termware.parsers.terms.TermReader;

/**
 *Parser factory for terms.  This is default parser factory for TermWare language.
 *@see IParserFactory
 *@see TermWareInstance#getParserFactory(String languageName)
 */
public class TermParserFactory implements IParserFactory
{

  public IParser createParser(Reader in, String inFname, Term arg, TermWareInstance instance)
  { 
   int line=0;   
   return new TermParser(new TermReader(in,inFname,line,instance));
  }
  

};