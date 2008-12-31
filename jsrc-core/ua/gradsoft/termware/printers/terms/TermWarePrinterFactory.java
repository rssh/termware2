/*
 * TermWarePrinterFactory.java
 *
 */

package ua.gradsoft.termware.printers.terms;

import java.io.*;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.IPrinter;
import ua.gradsoft.termware.IPrinterFactory;
import ua.gradsoft.termware.TermWareException;


/**
 *Factory for printers of termware expressions.
 */
public class TermWarePrinterFactory implements IPrinterFactory {
    
    /** Creates a new instance of TermWarePrinterFactory */
    public TermWarePrinterFactory() {
    }
    
    /**
     * create printer.
     */
    public IPrinter createPrinter(PrintWriter out, String outTag, TermSystem sys, Term arg) throws TermWareException {
        return new TermWarePrinter(out, outTag);
    }
    
}
