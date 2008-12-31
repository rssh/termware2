/*
 * Part of GradSoft TermWare.
 * (C) GradSoft, 2008
 * http://www.gradsoft.ua
 */

package ua.gradsoft.termware;

import java.io.PrintWriter;

/**
 *Factory for pretty printers.
 * pretty printers are registered in termware instance.
 * @author rssh
 */
public interface IPrettyPrinterFactory extends IPrinterFactory
{

 /**
  * create printer object.
  **/                            
 public IPrettyPrinter  createPrettyPrinter(PrintWriter out, 
                                            String outTag, 
                                            TermSystem sys, 
                                            Term arg)  
                                         throws TermWareException;
    
    
}
