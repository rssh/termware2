package ua.gradsoft.termware;

import java.io.PrintWriter;

import java.math.*;
import ua.gradsoft.termware.exceptions.*;

                           
/**
 * Term, which represend  value with java.math.BigInteger
 * semantics.
 **/ 
public final class BigIntegerTerm extends AbstractPrimitiveTerm
{

 /**
  * create new instanse of BigIntegerTerm
  */
 BigIntegerTerm(BigInteger v)
  { v_ = v; }


 /**
  * @return PrimaryTypes.BIG_INTEGER
  *@see PrimaryTypes
  */ 
 public final int  getPrimaryType0() 
     { return PrimaryTypes.BIG_INTEGER; }

 
 /**
  *is this term represents Number ?
  *@return true
  */
 public final boolean isNumber()
 { return true; }
 
 /**
  *return number, which holded in this term.
  *@return BigDecimal object, with the same value as in term.
  */
 public final Number getNumber()
 { return v_; }
 
 /**
  * is this term represents BigInteger ?
  *@return true
  */
 public final boolean  isBigInteger()
     { return true; }

 /**
  *@return BigInteger value, which holded in this term.
  */
 public final BigInteger   getBigInteger()
     { return v_; }

 /**
  * name of term
  *@return printable representation of value.
  */
 public final String   getName()
    { return v_.toString(); }

 
 /**
  * are term logically stricly equal to <code> x </code> ?
  *Type conversions are not performed.
  *@see TypeConversion
  */
 public boolean eq(Term t) 
    { 
      int x = PrimaryTypes.NUMBER - t.getPrimaryType1();
      if (x!=0) return false;
      switch(t.getPrimaryType0()) {
          case PrimaryTypes.BIG_DECIMAL:
              if (t.getBigDecimal().scale()!=0) return false;
              return v_.equals(t.getBigDecimal().unscaledValue());
          case PrimaryTypes.BIG_INTEGER:
              return v_.equals(t.getBigInteger());
          case PrimaryTypes.BYTE:
              if (v_.bitLength() > 7) return false;
              return v_.byteValue()==t.getByte();
          case PrimaryTypes.SHORT:
              if (v_.bitLength() > 15) return false;
              return v_.shortValue()==t.getShort();
          case PrimaryTypes.INT:
              if (v_.bitLength() > 31) return false;
              return v_.intValue()==t.getInt();
          case PrimaryTypes.LONG:
              if (v_.bitLength() > 63) return false;
              return v_.longValue() == t.getLong();
          case PrimaryTypes.DOUBLE:
              return v_.doubleValue() == t.getDouble();
          case PrimaryTypes.FLOAT:
              return v_.floatValue() == t.getFloat();
          default:
              throw new RuntimeAssertException("unknown number type",t);
      
      }
    }

 /**
  * since BigInteger is immutable - return this.
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
          {
              BigDecimal dc=t.getBigDecimal();
              if (dc.scale()==0) return v_.compareTo(dc.unscaledValue());
              double cmp=v_.doubleValue() - dc.doubleValue();
              if (cmp < 0) return -1;
              if (cmp > 0) return 1;
              return 0;
          }
          case PrimaryTypes.BIG_INTEGER:
              return v_.compareTo(t.getBigInteger());
          case PrimaryTypes.BYTE:
              if (v_.bitLength() > 7) return v_.signum();
              return v_.byteValue() - t.getByte();
          case PrimaryTypes.SHORT:
              if (v_.bitLength() > 15) return v_.signum();
              return v_.shortValue() - t.getShort();
          case PrimaryTypes.INT:
              if (v_.bitLength() > 31) return v_.signum();
              return v_.intValue() - t.getInt();
          case PrimaryTypes.LONG:
              if (v_.bitLength() > 63) return v_.signum();
              {
                  long cmp=v_.longValue() - t.getLong();
                  if (cmp < 0) return -1;
                  if (cmp > 0) return 1;
                  return 0;
              }
          case PrimaryTypes.DOUBLE:
             {
               double cmp = v_.doubleValue() - t.getDouble();
               if (cmp < 0) return -1;
               if (cmp > 0) return 1;
               return 0;              
             }
          case PrimaryTypes.FLOAT:
             {
                 float cmp=v_.floatValue() - t.getFloat();
                 if (cmp < 0) return -1;
                 if (cmp > 0) return 1;
                 return 0;
             }
          default:
              throw new RuntimeAssertException("Unknown numer type",t);
      }
    } 

 /**
  * print term to <code> out </code>
  *@param out - PrintWriter to output term.
  */
 public final void print(PrintWriter out)
  { out.print(v_); }


     private BigInteger v_;
    
}
