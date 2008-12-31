package ua.gradsoft.termware;


import java.io.PrintWriter;
                 
/**
 * Term for representation of boolean value.
 */
public final class BooleanTerm extends AbstractPrimitiveTerm
{

    
 BooleanTerm(boolean b)
  { v_ = b; }

 static public BooleanTerm  getBooleanTerm(boolean b)
  {
    if (b) return trueTerm_; else return falseTerm_;
  }

 public final int  getPrimaryType0() 
     { return PrimaryTypes.BOOLEAN; }

 /**
  * is this term is boolean ?
  *@return true
  */
 public final boolean  isBoolean()
     { return true; }

 public  final boolean  getBoolean()
    { return v_; }         


 /**
  * is this term is number ?
  *@return false
  */ 
 public  final boolean  isNumber()
 { return false; }
 
 /**
  * get number value, if one exists.
  *In our case - throw <code> UnsupportedOperationException </code>
  */
 public  final Number getNumber() 
 { throw new UnsupportedOperationException(); }
 
 /**
  * return string representation
  */
 public String   getName()
    { return new Boolean(v_).toString(); }

 public final boolean  eq(Term x) 
    { if (!x.isBoolean()) return false;
      return x.getBoolean()==v_;
    }

 public  final int termCompare(Term t)
    { 
      int x=PrimaryTypes.BOOLEAN - t.getPrimaryType0();
      if (x!=0) return x;
      if (v_==false && t.getBoolean()==false) return 0;
      if (v_==false && t.getBoolean()==true) return -1;
      if (v_==true && t.getBoolean()==false) return 1;
      /* if (v_==true && t.getBoolean()==true) */  return 0;
     }


 
 /**
  * since boolean is immutable, return this.
  */
 public  final Term    termClone()
   { return this; }

 public  final void     print(PrintWriter out)
   { out.print(v_); }

 private boolean v_;

 static  BooleanTerm trueTerm_ = new BooleanTerm(true);
 static  BooleanTerm falseTerm_ = new BooleanTerm(false);

}
