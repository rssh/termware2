package ua.gradsoft.termware;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2004
 * $Id: IParser.java,v 1.1.1.1 2004-12-11 19:41:27 rssh Exp $
 */

import java.io.*;

import ua.gradsoft.termware.exceptions.*;

/**
 * generic interface for custom parsers.
 * @see IParserFactory
 **/                           
public interface IParser
{     
                              
 public Term  readTerm()  throws TermWareException;

 public boolean  eof();

}
