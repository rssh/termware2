package ua.gradsoft.termware;

import java.io.PrintWriter;
import java.math.BigInteger;
import ua.gradsoft.termware.exceptions.*;

                           
/**
 * Term, which represend  value with java 'long'
 * semantics.
 **/ 
public final class LongTerm extends AbstractPrimitiveTerm
{

 /**
  * create new instanse of LongTerm
  */
 public LongTerm(long v)
  { v_ = v; }


 /**
  * @return PrimaryTypes.LONG
  *@see PrimaryTypes
  */ 
 public final int  getPrimaryType0() 
     { return PrimaryTypes.LONG; }

 
 /**
  *is this term represents Number ?
  *@return true
  */
 public final boolean isNumber()
 { return true; }
 
 /**
  *return number, which holded in this term.
  *@return Long object, with the same value as in term.
  */
 public final Number getNumber()
 { return new Long(v_); }
 
 /**
  * is this term represets long ?
  *@return true
  */
 public final boolean  isLong()
     { return true; }

 /**
  *@return long value, which holded in this term.
  */
 public final long   getLong()
     { return v_; }

 /**
  * name of term
  *@return printable representation of value.
  */
 public final String   getName()
    { return new Long(v_).toString(); }

 
 /**
  * are term logically  equal to <code> t </code> ?
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
                  if (bi.bitLength() > 63) return false;
                  return v_ == bi.longValue();
              }else{
                  return false;
              }
          case PrimaryTypes.BIG_INTEGER:
              if (t.getBigInteger().bitLength() > 63) return false;
              return v_ == t.getBigInteger().longValue();
          case PrimaryTypes.BYTE:
              return v_ == t.getByte();
          case PrimaryTypes.SHORT:
              return v_ == t.getShort();
          case PrimaryTypes.INT:
              return v_ == t.getInt();
          case PrimaryTypes.LONG:
              return v_ == t.getLong();
          case PrimaryTypes.DOUBLE:
              return v_ == t.getDouble();
          case PrimaryTypes.FLOAT:
              return v_ == t.getFloat();
          default:
              // impossible
              return false;
      }
    }

 /**
  * since long is immutable - return this.
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
      switch(t.getPrimaryType0()) {
          case PrimaryTypes.BIG_DECIMAL:
              if (t.getBigDecimal().scale()==0) {
                  BigInteger bi = t.getBigDecimal().unscaledValue();
                  if (bi.bitLength() > 63) {
                      return - bi.signum();
                  }else{
                      long cmp = v_ - bi.longValue();
                      if (cmp < 0) return -1;
                      if (cmp > 0) return 1;
                      return 0;
                  }
              }else{
                  double cmp = v_ - t.getBigDecimal().doubleValue();
                  if (cmp < 0) return -1;
                  if (cmp > 0) return 1;
                  return 0;
              }
          case PrimaryTypes.BIG_INTEGER:
              if (t.getBigInteger().bitLength() > 63) {
                  return - t.getBigInteger().signum();
              }else{
                  long cmp = v_ - t.getBigInteger().longValue();
                  if (cmp < 0) return -1;
                  if (cmp > 0) return 1;
                  return 0;
              }
          case PrimaryTypes.BYTE:
          {
              long cmp=v_-t.getByte();
              if (cmp < 0) return -1;
              if (cmp > 0) return 1;
              return 0;
          }
          case PrimaryTypes.SHORT:
          {
              long cmp=v_-t.getShort();
              if (cmp < 0) return -1;
              if (cmp > 0) return 1;
              return 0;
          }
          case PrimaryTypes.INT:
          {
              long cmp=v_-t.getInt();
              if (cmp < 0) return -1;
              if (cmp > 0) return 1;
              return 0;
          }
          case PrimaryTypes.LONG:
          {
              long cmp=v_-t.getLong();
              if (cmp < 0) return -1;
              if (cmp > 0) return 1;
              return 0;
          }
          case PrimaryTypes.DOUBLE:
          {
              double cmp=v_-t.getDouble();
              if (cmp < 0) return -1;
              if (cmp > 0) return 1;
              return 0;
          }
          case PrimaryTypes.FLOAT:
          {
              float cmp=v_-t.getFloat();
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

 
 private long v_;
 
 private static final long serialVersionUID = 20080112;

}
