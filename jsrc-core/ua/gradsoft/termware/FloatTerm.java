package ua.gradsoft.termware;

import java.io.PrintWriter;
import ua.gradsoft.termware.exceptions.*;

                           
/**
 * Term, which represend floating point value with java 'float'
 * semantics.
 **/ 
public final class FloatTerm extends AbstractPrimitiveTerm
{

 /**
  * create new instanse of FloatTerm
  */
 public FloatTerm(float v)
  { v_ = v; }


 /**
  * @return PrimaryTypes.FLOAT
  *@see PrimaryTypes
  */ 
 public final int  getPrimaryType0() 
     { return PrimaryTypes.FLOAT; }

 
 /**
  *is this term represents Number ?
  *@return true
  */
 public final boolean isNumber()
 { return true; }
 
 /**
  *return number, which holded in this term.
  *@return Float object, with the same value as in term.
  */
 public final Number getNumber()
 { return new Float(v_); }
 
 /**
  * is this term represets Float ?
  *@return true
  */
 public final boolean  isFloat()
     { return true; }

 /**
  *@return float value, which holded in this term.
  */
 public final float   getFloat()
     { return v_; }

 /**
  * name of term
  *@return printable representation of value.
  */
 public final String   getName()
    { return new Double(v_).toString(); }

 
 /**
  * are term logically  equal to <code> t </code> ?
  *@see TypeConversion
  */
 public boolean  eq(Term t) 
    { 
      int x=PrimaryTypes.FLOAT - t.getPrimaryType1();
      if (x!=0) return false;
      switch(t.getPrimaryType0()) {
          case PrimaryTypes.BIG_DECIMAL:
              return v_ == t.getBigDecimal().floatValue();
          case PrimaryTypes.BIG_INTEGER:
              return v_ == t.getBigInteger().floatValue();
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
              // impossible.
              throw new RuntimeAssertException("Unknown number type of term",t);
      }
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
      float cmp=0;
      switch(t.getPrimaryType0()) {
          case PrimaryTypes.BIG_DECIMAL:
              cmp = v_ - t.getBigDecimal().floatValue();
              break;
          case PrimaryTypes.BIG_INTEGER:
              cmp = v_ - t.getBigInteger().floatValue();
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
          {
              double cmp1 = (double)v_ - t.getDouble();
              if (cmp1 < 0) return -1;
              if (cmp1 > 0) return 1;
              return 0;
          }
          case PrimaryTypes.FLOAT:
              cmp = v_ - t.getFloat();
              break;
          default:
              throw new RuntimeAssertException("unknown number type");              
      }
      if (cmp < 0) return -1;
      if (cmp > 0) return 1;
      return 0;
    } 

 /**
  * print term to <code> out </code>
  *@param out - PrintWriter to output term.
  */
 public final void print(PrintWriter out)
  { out.print(v_); }

 
 private float v_;

}
