package ua.gradsoft.termware;

import java.io.PrintWriter;
import java.math.BigInteger;
import ua.gradsoft.termware.exceptions.*;

                           
/**
 * Term, which represend short value with java 'short'
 * semantics.
 **/ 
public final class ShortTerm extends AbstractPrimitiveTerm
{

 /**
  * create new instanse of ShortTerm
  */
 public ShortTerm(short v)
  { v_ = v; }


 /**
  * @return PrimaryTypes.SHORT
  *@see PrimaryTypes
  */ 
 public final int  getPrimaryType0() 
     { return PrimaryTypes.SHORT; }

 
 /**
  *is this term represents Number ?
  *@return true
  */
 public final boolean isNumber()
 { return true; }
 
 /**
  *return number, which holded in this term.
  *@return Short object, with the same value as in term.
  */
 public final Number getNumber()
 { return new Short(v_); }
 
 /**
  * is this term represets short ?
  *@return true
  */
 public final boolean  isShort()
     { return true; }

 /**
  *@return short value, which holded in this term.
  */
 public final short   getShort()
     { return v_; }

 /**
  * name of term
  *@return printable representation of value.
  */
 public final String   getName()
    { return new Short(v_).toString(); }

 
 /**
  * are term logically equal to <code> t </code> ?
  *@see TypeConversion
  */
 public boolean  eq(Term t)
    { 
      int x = PrimaryTypes.NUMBER - t.getPrimaryType1();
      if (x!=0) return false;
      switch(t.getPrimaryType0()) {
          case PrimaryTypes.BIG_DECIMAL:
              if (t.getBigDecimal().scale()==0) {
                  BigInteger bi=t.getBigDecimal().unscaledValue();
                  if (bi.bitLength() < 15) {
                      return v_==bi.shortValue();
                  }
              }
              return false;
          case PrimaryTypes.BIG_INTEGER:
              if (t.getBigInteger().bitLength() < 15) {
                  return v_==t.getBigInteger().shortValue();
              }else{
                  return false;
              }
          case PrimaryTypes.BYTE:
              return v_==(short)t.getByte();
          case PrimaryTypes.SHORT:
              return v_==t.getShort();
          case PrimaryTypes.INT:
              return ((int)v_) == t.getInt();
          case PrimaryTypes.LONG:
              return ((long)v_) == t.getLong();
          case PrimaryTypes.DOUBLE:
              return ((double)v_) == t.getDouble();
          case PrimaryTypes.FLOAT:
              return ((float)v_) == t.getFloat();
          default:
              // impossible
              return false;              
      }
    }

 /**
  * since short is immutable - return this.
  */
 public  Term    termClone()
   { return this; }

 
 /**
  *@see Term#termCompare
  */
 public final int termCompare(Term t)
    {
      int x = PrimaryTypes.NUMBER - t.getPrimaryType1();
      if (x!=0) return x;
      switch(t.getPrimaryType0()) {
          case PrimaryTypes.BIG_DECIMAL:
              if (t.getBigDecimal().scale()==0) {
                  BigInteger bi=t.getBigDecimal().unscaledValue();
                  if (bi.bitLength()<=15) {
                      return v_ - bi.shortValue();
                  }else{
                      return - bi.signum();
                  }
              }else{
                  double cmp = ((double)v_) - t.getBigDecimal().doubleValue();
                  if (cmp < 0) return -1;
                  if (cmp > 0) return 1;
                  return 0;
              }
          case PrimaryTypes.BIG_INTEGER:
              if (t.getBigInteger().bitLength() > 15) {
                  return - t.getBigInteger().signum();
              }else{
                  return v_ - t.getBigInteger().shortValue();
              }
          case PrimaryTypes.BYTE:
              return v_-(short)t.getByte();
          case PrimaryTypes.SHORT:
              return v_-t.getShort();
          case PrimaryTypes.INT:
              return ((int)v_) - t.getInt();
          case PrimaryTypes.LONG:
          {
              long cmp = ((long)v_) - t.getLong();
              if (cmp < 0) return -1;
              if (cmp > 0) return 1;
              return 0;
          }
          case PrimaryTypes.DOUBLE:
          {
              double cmp = ((double)v_) - t.getDouble();
              if (cmp < 0) return -1;
              if (cmp > 0) return 1;
              return 0;
          }
          case PrimaryTypes.FLOAT:
          {
              float cmp = ((float)v_) - t.getFloat();
              if (cmp < 0) return -1;
              if (cmp > 0) return 1;
              return 0;              
          }
          default:
              // impossible
              throw new RuntimeAssertException("unknown number type",t);
      }
    } 

 /**
  * print term to <code> out </code>
  *@param out - where to output term.
  */
 public final void print(PrintWriter out)
  { out.print(v_); }

 
 private short v_;

}
