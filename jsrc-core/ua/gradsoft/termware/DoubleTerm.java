package ua.gradsoft.termware;

import java.io.PrintWriter;

                           
/**
 * Term, which represend floating point value with java 'double'
 * semantics.
 **/ 
public final class DoubleTerm extends AbstractPrimitiveTerm
{

 /**
  * create new instanse of DoubleTerm
  */
 DoubleTerm(double v)
  { v_ = v; }


 /**
  * @return PrimaryTypes.DOUBLE
  *@see PrimaryTypes
  */ 
 public final int  getPrimaryType0() 
     { return PrimaryTypes.DOUBLE; }

 
 /**
  *is this term represents Number ?
  *@return true
  */
 public final boolean isNumber()
 { return true; }
 
 /**
  *return number, which holded in this term.
  *@return Double object, with the same value as in term.
  */
 public final Number getNumber()
 { return new Double(v_); }
 
 /**
  * is this term represets Double ?
  *@return true
  */
 public final boolean  isDouble()
     { return true; }

 /**
  *@return double value, which holded in this term.
  */
 public final double   getDouble()
     { return v_; }

 /**
  * name of term
  *@return printable representation of value.
  */
 public final String   getName()
    { return new Double(v_).toString(); }

 
 /**
  * are term logically equal to <code> x </code>
  */
 public boolean  eq(Term t) 
    { 
      int x = PrimaryTypes.NUMBER - t.getPrimaryType1();
      if (x!=0) return false;
      switch(t.getPrimaryType0()) {
          case PrimaryTypes.BIG_DECIMAL:
          case PrimaryTypes.BIG_INTEGER:
              return v_ == t.getNumber().doubleValue();
          case PrimaryTypes.BYTE:
              return v_ == t.getByte();
          case PrimaryTypes.SHORT:
              return v_ == t.getShort();
          case PrimaryTypes.INT:
              return v_ == t.getInt();
          case PrimaryTypes.LONG:
              return v_ ==  t.getLong();
          case PrimaryTypes.DOUBLE:
              return v_ == t.getDouble();
          case PrimaryTypes.FLOAT:
              return v_ == t.getFloat();
          default:
              // impossible
      }
      return false;
    }

 /**
  * since double is immutable - return this.
  */
 public Term termClone()
   { return this; }

 
 /**
  *@see Term#termCompare
  */
 public final int termCompare(Term t) 
    { 
      int x = PrimaryTypes.NUMBER - t.getPrimaryType1();
      if (x!=0) return x;
      double cmp=0;
      switch(t.getPrimaryType0()) {
          case PrimaryTypes.BIG_DECIMAL:
          case PrimaryTypes.BIG_INTEGER:
              cmp = v_ - t.getNumber().doubleValue();
              break;
          case PrimaryTypes.BYTE:
              cmp = v_ - t.getByte();
              break;
          case PrimaryTypes.SHORT:
              cmp = v_ - t.getShort();
              break;
          case PrimaryTypes.INT:
              cmp = v_ - t.getInt();
              break;
          case PrimaryTypes.LONG:
              cmp = v_ - t.getLong();
              break;
          case PrimaryTypes.DOUBLE:
              cmp = v_ - t.getDouble();
              break;
          case PrimaryTypes.FLOAT:
              cmp = v_ - t.getDouble();
              break;
          default:
              // impossible
      }
      if (cmp < 0) return -1;
      if (cmp > 0) return  1;
      return 0;
    }


 /**
  * print term to <code> out </code>
  *@param out - PrintWriter to output term.
  */
 public final void print(PrintWriter out)
  { out.print(v_); }

 
 private double v_;

}
