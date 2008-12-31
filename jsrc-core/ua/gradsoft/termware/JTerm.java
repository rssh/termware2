package ua.gradsoft.termware;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2005
 */

import java.io.*;
import java.util.*;
import java.math.*;

import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.util.*;

                           
/**
 * Term wich incapsulate opaque Java Object.
 *
 **/
public class JTerm extends Term
{

 /**
  * construct instance of JTerm from Java Object
  */   
 public JTerm(Object o)
 {
  o_ = ((o == null) ? NILTerm.getNILTerm() : o);
 }

 /**
  * get Primary Type
  */
 public int getPrimaryType0()
  { 
   if ( o_ instanceof Term )  return ((Term)o_).getPrimaryType0();   
   return PrimaryTypes.JAVA_OBJECT;
  } 

 /**
  * is this term is nil ?
  */
 public boolean  isNil()
 {
  if ( o_ instanceof Term )  return ((Term)o_).isNil();
  return false;
 }

 /**
  * if this is atom ?
  */
 public boolean  isAtom()
 { 
  if ( o_ instanceof Term )  return ((Term)o_).isAtom();
  return false;
 }

 public boolean  isBoolean()
 { 
  if ( o_ instanceof Term)  return ((Term)o_).isBoolean();
  return ( o_ instanceof Boolean );
 }

 public boolean  getBoolean()  
 {
  if ( o_ instanceof Term )  return ((Term)o_).getBoolean();
  else if (o_ instanceof Boolean) {
      return ((Boolean)o_).booleanValue();
  }else{
      throw new UnsupportedOperationException();
  }
 }

  /**
  * if this is BigDecimal
  *@return true if inderlaying object is BigDecimal term, otherwise - false
  */
 public boolean isBigDecimal() 
 {
    if ( o_ instanceof Term )  return ((Term)o_).isBigDecimal();
    return false;
 }

 /**
  * getBigDecimal
  *@return underlying BigDecimal Object, if one exists. Otherwise throws UnsupportedOperationException
  */
 public BigDecimal getBigDecimal() {
   if (o_ instanceof Term ) return ((Term)o_).getBigDecimal();
   throw new UnsupportedOperationException();
 }
 
/**
  * if this is BigInteger
  *@return true if inderlaying object is BigInteger term, otherwise - false
  */
 public boolean isBigInteger() {
    if ( o_ instanceof Term )  return ((Term)o_).isBigInteger();
    return false;
 }

 
 /**
  * get BigInteger value
  *@return value of underlaying BigInteger term object, if one exists. Otherwise throws UnsupportedOperationException
  */
 public BigInteger getBigInteger() {
   if (o_ instanceof Term ) return ((Term)o_).getBigInteger();
   throw new UnsupportedOperationException();
 }

 /**
  * if this is Byte
  *@return true if inderlaying object is byte term, otherwise - false
  */
 public boolean isByte() {
    if ( o_ instanceof Term )  return ((Term)o_).isByte();
    return false;
 }

 /**
  * get byte value
  *@return value underlying byte term object, if one exists. Otherwise throws UnsupportedOperationException
  */
 public byte getByte() {
   if (o_ instanceof Term ) return ((Term)o_).getByte();
   throw new UnsupportedOperationException();
 }

  /**
  * if this is Char
  *@return true if inderlaying object is character term, otherwise - false
  */
 public boolean isChar() {
    if ( o_ instanceof Term )  return ((Term)o_).isChar();
    return false;
 }

 /**
  * get character value
  *@return value of underlying character term object, if one exists. Otherwise throws UnsupportedOperationException
  */
 public char getChar() {
   if (o_ instanceof Term ) return ((Term)o_).getChar();
   throw new UnsupportedOperationException();
 }

 /**
  * if this is Float
  *@return true if inderlaying object is Float or FloatTerm, otherwise - false
  */
 public boolean isFloat() {
    if ( o_ instanceof Term )  return ((Term)o_).isFloat();
    return false;
 }

 /**
  * get float value
  *@return underlying character Object, if one exists. Otherwise throws InvalifTypeException
  */
 public float getFloat() {
   if (o_ instanceof Term ) return ((Term)o_).getFloat();
   throw new UnsupportedOperationException();
 }

  public boolean  isDouble()
 {
  if ( o_ instanceof Term )  return ((Term)o_).isDouble();
  return false;
 }

 public double   getDouble()  
 {
  if ( o_ instanceof Term )  return ((Term)o_).getDouble();
  throw new UnsupportedOperationException();
 }

 public boolean isShort() 
 {
  if  (o_ instanceof Term ) return((Term)o_).isShort();   
  return false;  
 }
 
 
 public short getShort() 
 {
   if  (o_ instanceof Term ) return((Term)o_).getShort();   
   throw new UnsupportedOperationException();   
 }
 
 public boolean  isInt()
 {
  if ( o_ instanceof Term )  return ((Term)o_).isInt();
  return false;
 }

 public int      getInt() 
 {
  if ( o_ instanceof Term )  return ((Term)o_).getInt();
  throw new UnsupportedOperationException();
 }

  /**
  * if this is long
  *@return true if inderlaying object is LongTerm, otherwise - false
  */
 public boolean isLong() {
    if ( o_ instanceof Term )  return ((Term)o_).isLong();
    return false;
 }

 /**
  * get long value
  *@return long value of underlying term, if we hold non-term Object - throws UnsupportedOperationException
  *@exception UnsupportedOperationException
  */
 public long getLong(){
   if (o_ instanceof Term ) return ((Term)o_).getLong();
   throw new UnsupportedOperationException();
 }

 

 public boolean  isString()
 {
  if ( o_ instanceof Term )  return ((Term)o_).isString();
  return false;
 }

 public String   getString()  
 {
  if ( o_ instanceof Term )  return ((Term)o_).getString();
  throw new UnsupportedOperationException();
 }

 /**
  * if this is x ?
  */
 public boolean  isX()
 {
  if ( o_ instanceof Term )  return ((Term)o_).isX();
  return false;
 }

 /**
  * get x index.
  */
 public int getXIndex()  {
   if  (o_ instanceof Term ) return((Term)o_).getXIndex();   
   throw new UnsupportedOperationException();        
 }
 
 
 public boolean  isComplexTerm()
 {
  if ( o_ instanceof Term )  return ((Term)o_).isComplexTerm();
  return false;
 }

 public String   getName()
 {
  if ( o_ instanceof Term )  return ((Term)o_).getName();
  return "jobject";
 }

 public Object getNameIndex() {
     return TermWareSymbols.JOBJECT_INDEX;
 }
 
 
 public int      getArity()
 {
  if ( o_ instanceof Term )  return ((Term)o_).getArity();
  return 0;
 }

 // @class("ua.gradsoft.termaware.term",x.getName().getList()==3)
 
 public final boolean  isJavaObject()
 {
  if (o_ instanceof Term ) return ((Term)o_).isJavaObject();
  return true;
 }

 public final Object   getJavaObject() 
 {
  if (o_ instanceof Term ) return ((Term)o_).getJavaObject();   
  return o_;
 }

 public Term getTerm()
 {
  if ( o_ instanceof Term )  return ((Term)o_).getTerm();
  return this;
 }

 /**
  * get subterm of current term.
  **/
 public Term getSubtermAt(int i)
 {
  if ( o_ instanceof Term )  return ((Term)o_).getSubtermAt(i);
  throw new UnsupportedOperationException();
 }

 public void     setSubtermAt(int i, Term t) throws TermWareException
 {
  if ( o_ instanceof Term )  ((Term)o_).setSubtermAt(i,t);
  throw new UnsupportedOperationException();
 }



 /**
  * return name of pattern, for which unification is applicable.
  *  (i. e. if  x.getPatternName()="q", than unification is applicable
  * to pattern-name.
  **/
 public String   getPatternName()
 {
  if ( o_ instanceof Term )  return ((Term)o_).getPatternName();
  return getName();
 }



  /**
  * do unification of <code> this </code> and <code> t </code>
  * and store in <code> s </code> substitution.
  **/
 public boolean freeUnify(Term t, Substitution s) throws TermWareException 
 {
  if ( o_ instanceof Term )  
        return ((Term)o_).freeUnify(t,s);
  return boundUnify(t,s); 
 } 

 
 
 /**
  * unification when we already have <code> s </code>
  * and when same propositional variables means same things
  **/
 public boolean boundUnify(Term t, Substitution s) throws TermWareException 
 {
  if ( o_ instanceof Term )  
        return ((Term)o_).boundUnify(t,s);
  if (t.isJavaObject()) {
        return o_.equals(t.getJavaObject());
  }else{
     if (t.isX()) {
        try {
          s.put(t,this); 
          return true;
        }catch(MatchingFailure x){
          return false;
        }
     }else if (t.isComplexTerm() && t.getNameIndex().equals(TermWareSymbols.CLASS_PATTERN_INDEX)) {         
         return t.boundUnify(this,s);                  
     }
  }
  return false;
 }
 
 
 


 /**
  * apply substitution <code> s </code> to current term.
  **/
 public boolean   substInside(Substitution s) throws TermWareException
 {
  if ( o_ instanceof Term )  return ((Term)o_).substInside(s);
  return false;
 }


  /**
   * receive new term, which is sibstution of current term and <code> s </code>
   **/
 public Term subst(Substitution s) throws TermWareException
 {
  if ( o_ instanceof Term )  return ((Term)o_).subst(s);
  return this;
 }



  /**
   * Equality when all propositional variables are equal
   **/
 public boolean  freeEquals(Term x) throws TermWareException
 {
  if ( o_ instanceof Term )  return ((Term)o_).freeEquals(x);
  if (x.isJavaObject()) {
    return o_.equals(x.getJavaObject());
  }
  return false;
 }



 /**
  * equality, when propositional variables are already bounded. 
  *@param x - term to compare.
  *@return true, if terms are equals.
  **/ 
 public boolean boundEquals(Term x) throws TermWareException
 {
  if ( o_ instanceof Term )  return ((Term)o_).boundEquals(x);
  if (x.isJavaObject()) {
    return o_.equals(x.getJavaObject());
  }
  return false;
 }

 

 public Term termClone() throws TermWareException
 {
  if ( o_ instanceof Term )  return ((Term)o_).termClone();
  return this;
 }

 public int      termCompare(Term x) 
 {
  if ( o_ instanceof Term )  return ((Term)o_).termCompare(x);
  // TODO
  if (x==this) return 0;
  if (x.isNil()) return -1;
  if (x.isJavaObject()) {      
      Object jx = x.getJavaObject();           
      if (o_.getClass().equals(jx.getClass())) {      
         int cmp = (o_.hashCode() - jx.hashCode());
         if (cmp!=0) {        
           return cmp;
         }else{
             if (o_==jx || o_.equals(jx)) {                
                 return 0;
             }else{              
                 return 1;
             }
         }
      }else{
          return o_.getClass().getName().compareTo(x.getClass().getName());
      }
  }
  return 1; 
 }

 public PartialOrderingResult concreteOrder(Term x, Substitution s) throws TermWareException
 {
     if (o_ instanceof Term) {
         return ((Term)o_).concreteOrder(x,s);
     }else{
         if (x.isX()) {
             Term xt = s.get(x.minFv());
             if (xt!=null) {
                 return concreteOrder(xt,s);
             }else{
                s.put(x,this);
                return PartialOrderingResult.LESS;
             }
         }else if (x.isJavaObject()) {
             if (o_.equals(x.getJavaObject())) {
                 return PartialOrderingResult.EQ;
             }else{
                 return PartialOrderingResult.NOT_COMPARABLE;
             }
         }else{
             return PartialOrderingResult.NOT_COMPARABLE;
         }
     }
 }
 
 public int findSubtermIndexBoundEqualsTo(Term x) throws TermWareException {
     if ( o_ instanceof Term) return ((Term)o_).findSubtermIndexBoundEqualsTo(x);
     throw new SubtermNotFoundException(x,this);
 }

 
 public int      minFv() throws TermWareException
 {
  if ( o_ instanceof Term )  return ((Term)o_).minFv();
  return -1;
 }

 public int      maxFv() throws TermWareException
 {
  if ( o_ instanceof Term )  return ((Term)o_).maxFv();
  return -1;
 }

 public void     shiftFv(int newMinFv) throws TermWareException
 {
  if ( o_ instanceof Term )  ((Term)o_).shiftFv(newMinFv);
 }

 public final boolean  emptyFv()
 {
  if ( o_ instanceof Term )  return ((Term)o_).emptyFv();
  return true;
 }

 public final Term  createSame(Term[] newBody) throws TermWareException 
 {
  if ( o_ instanceof Term )  return ((Term)o_).createSame(newBody);
  //TODO: call public object constructor, if one exists
  throw new UnsupportedOperationException();
 }

   
 public boolean isNumber()
 {
    if  (o_ instanceof Term ) return((Term)o_).isNumber();
    return false; 
 }
 
 
 /**
  * return Number value of underlaying number term, if one exists.
  */
 public Number getNumber() {
    if  (o_ instanceof Term ) return((Term)o_).getNumber();
    throw new UnsupportedOperationException(); 
 }
 
 public Object getPatternNameIndex() 
 {
  if  (o_ instanceof Term ) return((Term)o_).getPatternNameIndex();   
  return getNameIndex();  
 }
 
 
 public void print(PrintWriter out)
 {
  out.print("jobject("+o_.toString()+")");   
 }
 
 
 private Object o_;

  
  
}
