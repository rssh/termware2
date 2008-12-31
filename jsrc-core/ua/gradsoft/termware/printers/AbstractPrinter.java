package ua.gradsoft.termware.printers;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002
 * $Id: AbstractPrinter.java,v 1.2 2007-08-04 08:54:43 rssh Exp $
 */

import java.io.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;

/**
 * abstract class for custom term output.
 * @see IPrinterFactory
 **/                           
public abstract class AbstractPrinter implements IPrinter
{     

 /**
  * create AbstractPrinter
  *@param out - output stream.
  *@param outTag - tag of output, usially - filename of output file.
  */
 protected AbstractPrinter(PrintWriter out,String outTag)
 {
  out_=out;
  outTag_ = outTag;
 }
                              
 /**
  * write term
  */
 public abstract void writeTerm(Term t)  throws TermWareException;

 /**
  * get output PrinWriter
  */
 public final PrintWriter getOut()
  { return out_; }

 /**
  *get tag of output stream
  */
 public final String getTag()
  { return outTag_; }

 /**
  * print whitespace n times
  **/
 public final void printWs(int n)
 {
   while(n > 0) {
       out_.print(WS);
   }
 }
 
 /**
  * whitespace.
  **/
 public static final String WS = " ";

 protected PrintWriter out_;
 protected String outTag_;

}
