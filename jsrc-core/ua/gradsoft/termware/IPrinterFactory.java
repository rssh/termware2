package ua.gradsoft.termware;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002
 * $Id: IPrinterFactory.java,v 1.3 2008-01-05 14:50:04 rssh Exp $
 */

import java.io.*;

import ua.gradsoft.termware.exceptions.IllegalPrinterNameException;

/**
 * IPrinterFactory: way of creating pluggable interfaces to custom
 * term outers (printers).
 *  user must register class, which implements this interface via
 *  call of TermWareSingleton.addPrinter(languageName, className). 
 * After this in TermWare language term printFile(t,languageName,[filename]) 
 * will be reduce to <code> t </code> and term 
 * <code> t </code>
 * will be printed
 * by instance of IPrinter created by factory <code> className </code>
 **/                           
public interface IPrinterFactory
{     
   
 /**
  * create printer object.
  **/                            
 public IPrinter  createPrinter(PrintWriter out, 
                                String outTag, 
                                TermSystem sys, 
                                Term arg)  
                                         throws TermWareException;

}
