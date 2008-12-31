package ua.gradsoft.termware;

import java.io.PrintWriter;

import java.math.*;
import ua.gradsoft.termware.exceptions.*;

                           
/**
 * Term, which represend  BigDecimal value.
 **/ 
public final class BigDecimalTerm extends AbstractPrimitiveTerm
{

 /**
  * create new instanse of BigDecimalTerm
  */
 public BigDecimalTerm(BigDecimal v)
  { v_ = v; }


 /**
  * @return PrimaryTypes.BIG_DECIMAL
  *@see PrimaryTypes
  */ 
 public final int  getPrimaryType0() 
     { return PrimaryTypes.BIG_DECIMAL; }

 
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
  * is this term represents BigDecimal ?
  *@return true
  */
 public final boolean  isBigDecimal()
     { return true; }

 /**
  *@return BigDecimal value, which holded in this term.
  */
 public final BigDecimal   getBigDecimal()
     { return v_; }

 /**
  * name of term
  *@return printable representation of value.
  */
 public final String   getName()
    { return v_.toString(); }

 
 /**
  * are term logically stricly equal to <code> x </code> ?
  *@see TypeConversion
  */
 public boolean  eq(Term t) 
    { 
      if (!t.isNumber()) return false;
      switch(t.getPrimaryType0()) {
          case PrimaryTypes.BIG_DECIMAL:
              return v_.equals(t.getBigDecimal());
          case PrimaryTypes.BIG_INTEGER:
              if (v_.scale()==0) {
                  return v_.unscaledValue().equals(t.getBigInteger());
              }
              return false;
          case PrimaryTypes.BYTE:
              if (v_.scale()!=0) return false;
              if (v_.unscaledValue().bitLength() > 7) return false;
              return v_.byteValue()==t.getByte();
          case PrimaryTypes.SHORT:
              if (v_.scale()!=0) return false;
              if (v_.unscaledValue().bitLength() > 15) return false;
              return v_.shortValue()==t.getShort();              
          case PrimaryTypes.INT:
              if (v_.scale()!=0) return false;
              if (v_.unscaledValue().bitLength() > 31) return false;
              return v_.intValue()==t.getInt();
          case PrimaryTypes.LONG:
              if (v_.scale()!=0) return false;
              if (v_.unscaledValue().bitLength() > 63) return false;
              return v_.longValue()==t.getLong();
          case PrimaryTypes.DOUBLE:
              return v_.doubleValue()==t.getDouble();
          case PrimaryTypes.FLOAT:
              return v_.floatValue()==t.getFloat();
          default:
              // impossible
              throw new RuntimeAssertException("impossible number type",t);              
      }
    }

 /**
  * since BigDecimal is immutable - return this.
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
      switch(t.getPrimaryType0()){
          case PrimaryTypes.BIG_DECIMAL:
              return v_.compareTo(t.getBigDecimal());
          case PrimaryTypes.BIG_INTEGER:
              if (v_.scale()==0) {
                  return v_.unscaledValue().compareTo(t.getBigInteger());    
              }else{
                  double cmp=v_.doubleValue() - t.getBigInteger().doubleValue();
                  if (cmp < 0) return -1;
                  if (cmp > 0) return 1;
                  return 0;
              }              
          case PrimaryTypes.BYTE:
              if (v_.scale()!=0) return 1;
              {
               BigInteger bi=v_.unscaledValue();
               if (bi.bitLength()>7) return bi.signum();
               return bi.byteValue() - t.getByte();
              }
          case PrimaryTypes.SHORT:
              if (v_.scale()!=0) return 1;
              {
                BigInteger bi=v_.unscaledValue();
                if (bi.bitLength()>15) return bi.signum();
                return bi.shortValue() - t.getShort();
              }
          case PrimaryTypes.INT:
              if (v_.scale()!=0) return 1;
              {
                BigInteger bi=v_.unscaledValue();
                if (bi.bitLength() > 31) return bi.signum();
                return bi.intValue() - t.getInt();
              }
          case PrimaryTypes.LONG:
              if (v_.scale()!=0) return 1;
              {
                BigInteger bi=v_.unscaledValue();
                if (bi.bitLength() > 63) return bi.signum();
                long cmp = bi.longValue() - t.getLong();
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
              float cmp = v_.floatValue() - t.getFloat();
              if (cmp < 0) return -1;
              if (cmp > 0) return 1;
              return 0;
             }
          default:
             throw new RuntimeAssertException("unknown number type",t);              
      }
    } 

 
 /**
  * print term to <code> out </code>
  *@param out - PrintWriter to output term.
  */
 public final void print(PrintWriter out)
  { out.print(v_); }

 
     private BigDecimal v_;
    
}
