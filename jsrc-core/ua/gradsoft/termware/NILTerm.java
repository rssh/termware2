package ua.gradsoft.termware;


/*
 * (C) Rusla Shevchen <Ruslan@Shevchenko.Kiev.UA> 2002-2004
 * $Id: NILTerm.java,v 1.2 2007-07-13 20:50:17 rssh Exp $
 */

import java.io.*;

                           
/**
 * Term, which represents NIL
 **/
public final class NILTerm extends AbstractPrimitiveTerm
{

 /**
  * return Nil singleton
  */   
 static public final Term getNILTerm()
  { return nilTerm_; }

 /**
  *@return PrimaryTypes.NIL
  *@see PrimaryTypes
  */
 public final int getPrimaryType0()
  { return PrimaryTypes.NIL; }

 /**
  *@return true
  */
 public final boolean  isNil()
  { return true; }

 /**
  *@return false
  */
 public final boolean  isNumber()
 { return false; }
 
 /**
  *already throws UnsupportedOperationException
  *@exception  UnsupportedOperationException
  */
 public final Number  getNumber() throws  UnsupportedOperationException
 { throw new  UnsupportedOperationException(); }
 
 
 /**
  *return name of term.
  *@return "NIL"
  */
 public final String   getName()
  { return TermWareSymbols.NIL_STRING; }
 
 public final Object   getNameIndex()
 { return TermWareSymbols.NIL_INDEX; }

 public final boolean  eq(Term x)
   { return x.isNil(); }

 
 public final int termCompare(Term x)
   { 
     return PrimaryTypes.NIL - x.getPrimaryType1();
   }

 /**
  * since term is immutable - do nothing
  */
 public final Term    termClone()
   { return this; }


 public final void print(PrintWriter out)
   { out.print(TermWareSymbols.NIL_STRING); }

 private static transient NILTerm nilTerm_ = new NILTerm();

}
