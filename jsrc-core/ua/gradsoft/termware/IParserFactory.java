package ua.gradsoft.termware;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002
 * $Id: IParserFactory.java,v 1.4 2006-07-13 20:38:41 rssh Exp $
 */

import java.io.*;

import ua.gradsoft.termware.exceptions.*;

/**
 * IParserFactory: way of creating pluggable interfaces to parsers.
 *  user must register class, which implements this interface via
 *  call of TermWareSingleton.addParser(languageName, className). 
 * After this in TermWare language term loadFile(languageName,filename) 
 * will be reduce to result of processing file <code> filename </code>
 * by parser, created by factory <code> className </code>
 **/                           
public interface IParserFactory
{     
   
 /**
  * create parser object.
  **/                            
 public IParser  createParser(Reader in, String inFname, Term arg, TermWareInstance instance)  throws TermWareException;

}
