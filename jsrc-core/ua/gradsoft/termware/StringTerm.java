package ua.gradsoft.termware;


import java.io.PrintWriter;

                           
/**
 * Term which contains string.
 *@see Term
 **/
public class StringTerm extends AbstractPrimitiveTerm
{

 /**
  * constructor
  */
 public StringTerm(String s)
  { v_ = s; }


 /**
  *@return PrimaryTypes.STRING
  *@see PrimaryTypes
  */
 public final int   getPrimaryType0() 
     { return PrimaryTypes.STRING; }

 
 /**
  *@return true
  */
 public final boolean  isString()
     { return true; }

 /**
  *return incapsulated string.
  *@return string
  */
 public final String   getString()
     { return v_; }

 /**
  *@return false
  */
 public final boolean isNumber()
 { return false;}
 
 /**
  * in this class just throws UnsupportedOperationException
  */
 public  final Number getNumber()
 { throw new UnsupportedOperationException(); }
 
 
  /**
   *@see Term#termCompare
   */
  public final int termCompare(Term t) 
    { 
     int x = PrimaryTypes.STRING - t.getPrimaryType0();
     if (x!=0) return 0;
     return v_.compareTo(t.getString());
    } 
                                     

 /**
  *@return incapsulated string
  */
 public final String   getName()
    { return v_; }

 /**
  * are term logically equals to <code> x </code> ?
  *@param x  - term to compare
  *@return true, if values are equal
  */
 public boolean  eq(Term x)
    { if (!x.isString()) return false;
      return x.getString().equals(v_);
    }

 /**
  * String is immutable, so return this.
  */
 public  Term    termClone()
   { return this; }


 /**
  * print term to <code> out </code> 
  *@param out PrintWriter where to print.
  */
 public void print(PrintWriter out)
   { 
     if (v_==null) {
       out.print("null");
     } else { out.print("\"");
       for(int i=0; i<v_.length(); ++i) {
         char ch = v_.charAt(i);
         if (ch=='\\') out.print("\\\\");
         else if (ch=='"') out.print("\\\"");
         else if (ch=='\n') out.print("\\n");
         else if (ch=='\r') out.print("\\r");
         else if (ch=='\t') out.print("\\t");
         else out.print(ch);
       }
       out.print("\"");
     }
   }


 private String v_;

}
