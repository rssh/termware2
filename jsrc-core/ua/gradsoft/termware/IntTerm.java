package ua.gradsoft.termware;


import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import ua.gradsoft.termware.exceptions.*;

                        
/**
 * Term which represent integer value.
 *@see Term
 */
public class IntTerm extends AbstractPrimitiveTerm
{

    
 IntTerm(int v)
  { v_ = v; }

 static public IntTerm  getIntTerm(int v)
  {
    return new IntTerm(v); 
  }

 /**
  *@return PrimaryTypes.INT
  *@see PrimaryTypes
  */
 public final int  getPrimaryType0() 
     { return PrimaryTypes.INT; }

 
 /**
  *@return true
  *@see Term#isNumber()
  */
 public final boolean isNumber()
 { return true; }
 
 /**
  *@see Term#getNumber()
  */
 public final Number getNumber()
 { return new Integer(v_); }
 
 public final boolean  isInt()
     { return true; }

 public final int      getInt()
     { return v_; }

 
 public final String   getName()
    { return new Integer(v_).toString(); }

 
 public final boolean  eq(Term t) 
    { 
       if (t.isNumber()) {
           switch(t.getPrimaryType0()) {
               case PrimaryTypes.BIG_DECIMAL:
               {
                   BigDecimal bd=t.getBigDecimal();
                   if (bd.scale()==0) {
                       BigInteger bi = bd.unscaledValue();
                       if (bi.bitLength() > 31) {
                           return false;
                       }
                       return ( v_ == bi.intValue() );
                   }else{
                       return false;
                   }
               }
               case PrimaryTypes.BIG_INTEGER:
               {
                   BigInteger bi=t.getBigInteger();
                   if (bi.bitLength() < 32) {
                       return v_==bi.intValue();
                   }else{
                       return false;
                   }
               }
               case PrimaryTypes.DOUBLE:
                   return (double)v_ == t.getDouble();
               case PrimaryTypes.FLOAT:
                   return (float)v_ == t.getFloat();
               case PrimaryTypes.INT:
                   return v_ == t.getInt();
               case PrimaryTypes.LONG:
                   return (long)v_ == t.getLong();
               case PrimaryTypes.SHORT:
                   return v_ == t.getShort();
               case PrimaryTypes.BYTE:
                   return v_ == t.getByte();
               default:
                   return false;
           }
       }
       return false;
    }

 /**
  * int is immutable, so return this.
  */
 public Term termClone()
   { return this; }

 
 /**
  *@see Term#termCompare
  */
 public final int termCompare(Term t)
    { 
      int x=PrimaryTypes.NUMBER - t.getPrimaryType1();
      if (x!=0) return x;
      switch(t.getPrimaryType0()) {
          case PrimaryTypes.BIG_DECIMAL:
              if (t.getBigDecimal().scale()==0) {
                  BigInteger bi=t.getBigDecimal().unscaledValue();
                  if (bi.bitLength()>31) {
                      return - bi.signum();
                  }else{
                      return v_ - bi.intValue();
                  }
              }else{
                  double cmp = (double)v_ - t.getBigDecimal().doubleValue();
                  if (cmp < 0) return -1;
                  if (cmp > 0) return 1;
                  return 0;
              }
          case PrimaryTypes.BIG_INTEGER:
              if (t.getBigInteger().bitLength() > 31) {
                  return - t.getBigInteger().signum();
              }else{
                  return v_-t.getBigInteger().intValue();
              }
          case PrimaryTypes.BYTE:
              return v_-t.getByte();
          case PrimaryTypes.SHORT:
              return v_-t.getShort();
          case PrimaryTypes.DOUBLE:
          {
              double cmp=(double)v_ - t.getDouble();
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
          case PrimaryTypes.INT:
              return v_-t.getInt();
          case PrimaryTypes.LONG:
              long cmp = (long)v_ - t.getLong();
              if (cmp < 0) return -1;
              if (cmp > 0) return 1;
              return 0;
          default:
              throw new RuntimeAssertException("Unknown primitive type of term",t);              
      }
//      throw new RuntimeAssertException("impossible",t);              
    } 

 public final void print(PrintWriter out)
   { out.print(v_); }

 
// public  final void setInt(int v)
//   { v_ = v; }

 private int v_;

}
