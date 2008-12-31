package ua.gradsoft.termware;


import java.io.PrintWriter;
import java.io.StringWriter;

                           
/**
 * Term which contains char.
 *@see Term
 **/
public class CharTerm extends AbstractPrimitiveTerm
{

 /**
  * constructor
  */
 CharTerm(char v)
  { v_ = v; }


 /**
  *@return PrimaryTypes.CHAR
  *@see PrimaryTypes
  */
 public final int   getPrimaryType0() 
     { return PrimaryTypes.CHAR; }

 
 /**
  *@return true
  */
 public final boolean  isChar()
     { return true; }

 /**
  *return incapsulated character.
  *@return string
  */
 public final char   getChar()
     { return v_; }

 /**
  *@return false
  */
 public final boolean isNumber()
 { return false;}
 
 /**
  * in this class just throws UnsupportedOperationException
  */
 public  final Number getNumber() throws UnsupportedOperationException
 { throw new UnsupportedOperationException(); }
 
 
  /**
   *@see Term#termCompare
   */
  public final int termCompare(Term t) 
    { 
     int x = PrimaryTypes.CHAR - t.getPrimaryType0();
     if (x!=0) return x;
     return Character.getNumericValue(v_) - Character.getNumericValue(t.getChar());
    } 
                                     

 /**
  *@return incapsulated string
  */
 public final String   getName()
    { return Character.toString(v_); }

 /**
  * are term logically equals to <code> x </code> ?
  *@param x  - term to compare
  */
 public boolean  eq(Term x) 
    { if (!x.isChar()) return false;
      return x.getChar()==v_;
    }

 /**
  * Char is immutable, so return this.
  */
 public  Term    termClone()
   { return this; }


 /**
  * print term to <code> out </code> 
  *@param out -  where to print.
  */
 public void print(PrintWriter out)
   { out.print("'");
     if (v_=='\\') out.print("\\\\");
     else if (v_=='\n') out.print("\\n");
     else if (v_=='\r') out.print("\\r");
     else if (v_=='\t') out.print("\\t");
     else out.print(v_);
     out.print("'");
   }

 public String toString()
 { StringWriter swr=new StringWriter();
   PrintWriter pwr = new PrintWriter(swr);
   print(pwr);
   pwr.flush();
   return swr.toString();
 }

 private char v_;

}
