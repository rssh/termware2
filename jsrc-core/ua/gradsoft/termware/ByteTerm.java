package ua.gradsoft.termware;

import java.io.PrintWriter;
import java.math.BigInteger;

                           
/**
 * Term, which represend byte value with java 'byte'
 * semantics.
 **/ 
public final class ByteTerm extends AbstractPrimitiveTerm
{

 /**
  * create new instanse of ByteTerm
  */
 ByteTerm(byte v)
  { v_ = v; }


 /**
  * @return PrimaryTypes.BYTE
  *@see PrimaryTypes
  */ 
 public final int  getPrimaryType0() 
     { return PrimaryTypes.BYTE; }

 
 /**
  *is this term represents Number ?
  *@return true
  */
 public final boolean isNumber()
 { return true; }
 
 /**
  *return number, which holded in this term.
  *@return Byte object, with the same value as in term.
  */
 public final Number getNumber()
 { return new Byte(v_); }
 
 /**
  * is this term represets byte ?
  *@return true
  */
 public final boolean  isByte()
     { return true; }

 /**
  *@return byte value, which holded in this term.
  */
 public final byte   getByte()
     { return v_; }

 /**
  * name of term
  *@return printable representation of value.
  */
 public final String   getName()
    { return new Byte(v_).toString(); }

 
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
                  if (bi.bitLength() > 7) return false;
                  return v_ == bi.byteValue();
              }else{
                  return false;
              }              
          case PrimaryTypes.BIG_INTEGER:
              if (t.getBigInteger().bitLength() > 7) {
                  return false;
              }else{
                  return v_ == t.getBigInteger().byteValue();
              }
          case PrimaryTypes.BYTE:
              return v_ == t.getByte();
          case PrimaryTypes.SHORT:
              return (short)v_ == t.getShort();
          case PrimaryTypes.INT:
              return (int)v_ == t.getInt();
          case PrimaryTypes.LONG:
              return (long)v_ == t.getLong();
          case PrimaryTypes.DOUBLE:
              return (double)v_ == t.getDouble();
          case PrimaryTypes.FLOAT:
              return (float)v_==t.getFloat();
          default:
              // impossible
              return false;
      }
    }

 /**
  * since byte is immutable - return this.
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
                  if (bi.bitLength() < 7) {
                      return v_ - bi.byteValue();
                  }else{
                      return -bi.signum();
                  }
              }else{
                  double cmp = (double)v_ - t.getBigDecimal().doubleValue();
                  if (cmp < 0) return -1;
                  if (cmp > 0) return 1;
                  return 0;
              }
          case PrimaryTypes.BIG_INTEGER:
              if (t.getBigInteger().bitLength() < 7) {
                  return v_ - t.getBigInteger().byteValue();
              }else{
                  return -t.getBigInteger().signum();
              }
          case PrimaryTypes.BYTE:
              return v_ - t.getByte();
          case PrimaryTypes.SHORT:
              return (short)v_ - t.getShort();
          case PrimaryTypes.INT:
              return (int)v_ - t.getInt();
          case PrimaryTypes.LONG:
          {
              long cmp = (long)v_ - t.getLong();
              if (cmp < 0) return -1;
              if (cmp > 0) return 1;
              return 0;
          }
          case PrimaryTypes.DOUBLE:
          {
              double cmp = (double)v_ - t.getDouble();
              if (cmp < 0) return -1;
              if (cmp > 0) return 1;
              return 0;              
          }
          case PrimaryTypes.FLOAT:
          {
              float cmp = (float)v_ - t.getFloat();
              if (cmp < 0) return -1;
              if (cmp > 0) return 1;
              return 0;
          }
          default:
              // impossible
              return 0;
      }
    } 

 /**
  * print term to <code> out </code>
  *@param out - PrintWriter to output term.
  */
 public final void print(PrintWriter out)
  { out.print(v_); }

 
 private byte v_;

}
