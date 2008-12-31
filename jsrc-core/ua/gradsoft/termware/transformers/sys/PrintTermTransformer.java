package ua.gradsoft.termware.transformers.sys;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002 - 2005
 * $Id: PrintTermTransformer.java,v 1.2 2007-07-13 20:50:21 rssh Exp $
 */

import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.util.*;
import ua.gradsoft.termware.exceptions.*;

                           
/**
 *Transformer for expresion:
 * printTerm(t,[lang,[outtag]])
 *
 * printTerm(t) - transform to t, with effect of printing term to output stream of current enviorment
 * printTerm(t,lang) - as previous, but using printer for language <code> lang </code>
 *@see IPrinterFactory 
 **/
public class PrintTermTransformer extends AbstractBuildinTransformer
{

    /**
     *@return false
     */
    public boolean internalsAtFirst() {
        return false;
    }

 public  Term  transform(Term t, TermSystem sys, TransformationContext ctx) throws TermWareException
  { return static_transform(t,sys,ctx); }


 static public  Term  static_transform(Term t, TermSystem sys, TransformationContext ctx) throws TermWareException
 {
  if (!t.getName().equals("printTerm")) return t;
  if (sys.isLoggingMode()) {
   sys.getEnv().getLog().print("native:");
   t.print(sys.getEnv().getLog());
   sys.getEnv().getLog().println();
  }
  Term retval=t;
  if (t.getArity()==1) {
    t.getSubtermAt(0).print(sys.getEnv().getOutput());
    retval=t.getSubtermAt(0);
  }else if(t.getArity()==2 || t.getArity()==3) {
    // TODO:
    //    1.  check that name and language is string
    //    2.  outTag can be parameter.
    String printerName =  t.getSubtermAt(1).getName();
    IPrinterFactory printerFactory = sys.getInstance().getPrinterFactory(printerName);
    IPrinter printer;
    Term arg = ((t.getArity()==2) ? TermFactory.createNIL()
                                   : t.getSubtermAt(2)
                );
    printer=printerFactory.createPrinter(sys.getEnv().getOutput(),
                                         "unknown", sys, arg
                                         );
    printer.writeTerm(t.getSubtermAt(0));
    retval=t.getSubtermAt(0);
  }else{
    if (sys.isLoggingMode()) {
      sys.getEnv().getLog().print("native:");
      t.print(sys.getEnv().getLog());
      sys.getEnv().getLog().println("- not changed.");
    }
    return t;
  }
  ctx.setChanged(true);
  if (sys.isLoggingMode()) {
    sys.getEnv().getLog().print("native:");
    t.print(sys.getEnv().getLog());
    sys.getEnv().getLog().println("- printed.");
  }
  return retval;
 }

 public String getDescription() {
     return staticDescription_;
 }
 
 public String getName() {
     return "printTerm";
 }
 
 private final static String staticDescription_=
 " printTerm(t) - transform to t, with effect of printing term to output stream of current enviorment <br>"+
 " printTerm(t,lang) - as previous, but using printer for languagr <code> lang </code> <br>"
 ;
 
 
}
 
