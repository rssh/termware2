package ua.gradsoft.termware;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2008
 * $Id: IPrinter.java,v 1.2 2008-01-05 14:50:04 rssh Exp $
 */

import java.io.*;


/**
 * generic interface for custom term output.
 * @see IPrinterFactory
 **/                           
public interface IPrinter
{     
                              
   /**
    * output term <code> t </code>
    */
   public void     writeTerm(Term t)  throws TermWareException;
   
   /**
    * flush all internal IPrinter structures.
    */
   public void     flush();

}
