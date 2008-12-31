package ua.gradsoft.termware;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2007
 * $Id: Term.java,v 1.8 2007-07-13 20:50:17 rssh Exp $
 */

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;

import ua.gradsoft.termware.exceptions.*;

                           
/**
 * Basic class for Term - universal representation of term (elementary TermWare object).
 **/
public abstract class Term implements Serializable, Cloneable
{

    
 /**
  *return code of level-0 primary type 
  *@see PrimaryTypes
  *@return code of primary type
  */   
 public abstract int getPrimaryType0();

 /**
  * return code of level-1 primary type
  */
 public final int getPrimaryType1()
 { return getPrimaryType0() & PrimaryTypes.UNIFICATION_MASK; }
 
 /**
  * is term is nil ?
  *@return true if this term is Nil
  **/
 public abstract boolean  isNil( );

 
 /**
  * is term is atom ?
  *@return true if this term is atom
  **/
 public abstract boolean  isAtom();

 
 
 /**
  * is term is booleam ?
  *@return true if this term is boolean
  **/
 public abstract boolean  isBoolean();

 /**
  * get boolean value, if this term represent boolean, otherwise throw UnsupportedOperationException
  *@return boolean value
  *@exception UnsupportedOperationException raised if this term does not represent boolean value.
  **/
 public abstract boolean  getBoolean();

 /**
  *get boolean value, if this term can be converted to boolean
  */
 public boolean  getAsBoolean(TermWareInstance instance) throws ConversionException
 {
     return instance.getTypeConversion().getAsBoolean(this);
 }
 
 /**
  * is term is char ?
  *@return true if this term is char
  **/
 public abstract boolean  isChar();

 /**
  * get char value, if this term represent char, otherwise throw UnsupportedOperationException
  *@return char value
  *@exception UnsupportedOperationException raised if this term does not represent boolean value.
  **/
 public abstract char  getChar();

 /**
  * get char value. If this term does not represet char then try to transform one
  *to char use <code> instance </code> type conversion routines.
  */
 public char getAsChar(TermWareInstance instance) throws ConversionException
 {
     return instance.getTypeConversion().getAsChar(this);
 }
 
 /**
  * is term is Number ?
  *@return true if term represents number value.
  */
 public abstract boolean  isNumber();

 /**
  * get numeric value, if this term represent number, otherwise throw UnsupportedOperationException
  *@return number value
  *@exception UnsupportedOperationException raised if this term does not represent numeric value.
  **/
 public abstract Number   getNumber();

 public Number getAsNumber(TermWareInstance instance) throws ConversionException
 {
     return instance.getTypeConversion().getAsNumber(this);
 }

  /**
   *true, if this term is BigDecimal
   */
 public abstract boolean  isBigDecimal();
 
 
 /**
  * get BigDecimal value, if this term represent BigDecimal, otherwise throw UnsupportedOperationException
  *@return number value
  *@exception UnsupportedOperationException raised if this term does not represent BigDecimal value.
  */
 public abstract BigDecimal  getBigDecimal() throws UnsupportedOperationException;

 /**
  * get BigDecimal value, if this term represent some Numeric value, otherwise throw UnsupportedOperationException
  *@return number value
  *@exception UnsupportedOperationException raised if this term does not represent Number value.
  *@exception ConversionException is conversion is not possible.
  */
 public BigDecimal  getAsBigDecimal(TermWareInstance instance) throws ConversionException 
 {
   return instance.getTypeConversion().getAsBigDecimal(this);  
 }
 

 /**
   *true, if this term is BigInteger
   */
 public abstract boolean  isBigInteger();

  
 
 /**
  * get BigDecimal value, if this term represent BigInteger, otherwise throw UnsupportedOperationException
  *@return  value
  *@exception UnsupportedOperationException raised if this term does not represent BigInteger value.
  */
 public abstract BigInteger  getBigInteger() throws UnsupportedOperationException;

 /**
  * get BigInteger value, if this term represent some Numeric value, otherwise throw UnsupportedOperationException
  *@return number value
  *@exception ConversionException raised if conversion is impossible.
  *@exception UnsupportedOperationException raised if this term does not represent Number value.
  */
 public BigInteger  getAsBigInteger(TermWareInstance instance) throws ConversionException
 {
  return instance.getTypeConversion().getAsBigInteger(this);   
 }
 
 
 
 
 /**
   *true, if this term is Byte
   */
 public abstract boolean  isByte();
 
 /**
  * get byte value, if this term represent byte, otherwise throw UnsupportedOperationException
  *@return  value
  *@exception UnsupportedOperationException raised if this term does not represent byte value.
  */
 public abstract byte  getByte();

 /**
  * get Byte value, if this term represent some Numeric value, otherwise throw UnsupportedOperationException
  *@return number value
  *@exception ConversionException if conversion to byte is impossible.
  */
 public byte  getAsByte(TermWareInstance instance) throws ConversionException
 {
   return instance.getTypeConversion().getAsByte(this);  
 }
 
 
 
 
 /**
  * is term is Int ?
  *@return true if term represents integer value.
  */
 public abstract boolean  isInt();

 /**
  * get integer value, if this term represent integer, otherwise throw UnsupportedOperationException
  *@return int value
  *@exception InvalidPrimitiveTypeException raised if this term does not represent integer value.
  **/
 public abstract int      getInt() throws UnsupportedOperationException;

 
 /**
  * get integer value, if this term represent some number, otherwise throw ConversionException
  *@return int value
  *@exception ConversionException if conversion is impossible.
  **/
 public int      getAsInt(TermWareInstance instance) throws ConversionException
 {
     return instance.getTypeConversion().getAsInt(this);
 }

 
  /**
   * is Term is float ?
   *@return true if term represents float value.
   */
 public abstract boolean  isFloat();

/**
  * get float value, if this term represent float, otherwise throw UnsupportedOperationException.
  * Note, that getFloat does not provide conversion of other numeric types (such as double)
 ** to float.
 ** For this use getAsFloat(ITerm t);
  *@return float value
  *@exception UnsupportedOperationException raised if this term does not represent double value.
  **/
 public abstract float   getFloat()  throws UnsupportedOperationException;

  /**
  * get float value, if this term represent some number, otherwise throw ConversionException
  *@return float value
  *@exception ConversionException if conversion is impossible.
  **/
 public float      getAsFloat(TermWareInstance instance) throws ConversionException
 {
     return instance.getTypeConversion().getAsFloat(this);
 }

 
 
  /**
   * is Term is double ?
   *@return true if term represents double value.
   */
 public abstract boolean  isDouble();


 /**
  * get double value, if this term represent double, otherwise throw UnsupportedOperationException
  *Note, that this method does not provide conversion of other numeric types to double.
  *@return double value
  *@exception UnsupportedOperationException raised if this term does not represent double value.
  **/
 public abstract double   getDouble()  throws UnsupportedOperationException;

 /**
  * get double value, if this term represent some number, otherwise throw ConversionException
  *@return double value
  *@exception ConversionException if conversion is impossible.
  **/
 public double      getAsDouble(TermWareInstance instance) throws ConversionException
 {
     return instance.getTypeConversion().getAsDouble(this);
 }


 
 /**
   * is Term is long ?
   *@return true if term represents long value.  
  */
 public abstract boolean  isLong();
 
 /**
  * get long value, if this term represent long, otherwise throw UnsupportedOperationException
  *Note, that this method does not provide conversion of other numeric types to long.
  *Use getAsLong(TermWareInstance instance) for this purpose.
  *@return short value
  *@exception UnsupportedOperationException raised if this term does not represent double value.
  **/
 public abstract long   getLong()  throws UnsupportedOperationException;

 /**
  * get long value, if this term represent some number, otherwise throw ConversionException
  *@return long value
  *@exception ConversionException if conversion is impossible.
  **/
 public long      getAsLong(TermWareInstance instance) throws ConversionException
 {
     return instance.getTypeConversion().getAsLong(this);
 }
 
 
 /**
   * is Term is short ?
   *@return true if term represents short value.  
  */
 public abstract boolean  isShort();
 
 /**
  * get short value, if this term represent short, otherwise throw UnsupportedOperationException
  *Note, that this method does not provide conversion of other numeric types to short.
  *Use ITermHelper.getAsShort(ITerm t, TermWareInstance instance) for this purpose.
  *@return short value
  *@exception UnsupportedOperationException raised if this term does not represent double value.
  **/
 public abstract short   getShort()  throws UnsupportedOperationException;

 /**
  * get short value, if this term represent some number, otherwise throw ConversionException
  *@return short value
  *@param instance instance of runtime environment.
  *@exception ConversionException if conversion is impossible.
  **/
 public short     getAsShort(TermWareInstance instance) throws ConversionException
 {
     return instance.getTypeConversion().getAsShort(this);
 }
 
 

  /**
   * is Term is string ?
   *@return true if term represents string value.
   */
 public abstract boolean  isString();

 /**
  * get string value, if this term represent string, otherwise throw InvalidPrimitiveTypeException
  *@return string value
  *@exception UnsupportedOperationException raised if this term does not represent string value.
  **/
 public abstract String   getString();

  /**
  * get string value, if this term represent sring, otherwise throw ConversionException
  *@return string value
  *@param instance instance of runtime environment.
  *@exception ConversionException if conversion is impossible.
  **/
 public String  getAsString(TermWareInstance instance) throws ConversionException
 {
     return instance.getTypeConversion().getAsString(this);
 }
 

 
 /**
  * true if this term is propositional variable.
  *@return true if this term is propositional variable
  **/
 public abstract boolean  isX();

 /**
  * if this variable is a propositional variable, return index of one. Otherwise 
  *throw UnsupportedOperationException
  */
 public abstract int  getXIndex();

 /**
  * true, if this term have subterms
  *@return true if this term have subterms.
  **/
 public abstract boolean  isComplexTerm();

 
 
 /**
  * get name of functional symbol.
  *@return name
  **/
 public abstract String   getName();  


 
 /**
  * get index of name in instance symbol table. If name is not inde
  *(during unifications names are compared by indexes. This allows to speedup
  *unification process)
  */
 public abstract Object  getNameIndex();
 
 

 /**
  * if this term is Java Object ?
  */
 public abstract boolean  isJavaObject();
 

 /**
  *@return Java Object, if this term holds opaque Java Object, otherwise throw UnsupportedOperationException
  */
 public abstract Object   getJavaObject();
 
 
 public Object getAsJavaObject(TermWareInstance instance) throws ConversionException
 {
     return instance.getTypeConversion().getAsObject(this);
 }
 
  /**
  * get arity (i. e. number of subterms)
  *@return arity of term.
  **/
 public abstract int      getArity();

 
 /**
  * get term. Semantics is "return this term".
  * Non-trivial implementations are used for deallocating proxy terms.
  */
 public abstract Term    getTerm();

 /**
  * get subterm of current term.
  * when i < arity then throw TermIndexOutOfBoundsException
  *@param i - index of subterm, starting from 0
  *@return i-th subterm of current term.
  **/
 public abstract Term    getSubtermAt(int i);

 /**
  * set i-th subterm to t
  * when i < arity then throw IndexOutOfBoundsException
  *@param i - index of subterm to set.
  *@param t - subterm to set.
  **/
 public abstract void     setSubtermAt(int i, Term t) throws TermWareException;


 /**
  * return name of pattern, for which unification is applicable.
  *  (i. e. if  x.getPatternName()="q", than unification is applicable
  * to pattern-name.
  *  used for implementing of pattern mathing: for example <code> { x : y } </code>
  *  is a pattern for set, where <code> x </code> - element from set, <code> y </code> -
  *  rest of set). Name of this term is <code> set_pattern </code>, but pattern name is
  *  <code> set </code>
  */
 public String   getPatternName()
 { return getName(); }


 /**
  * return index of pattern name.
  *  (If term is adopted to current instance - index in instance symbol table,
  *   or just name)
  */
 public Object   getPatternNameIndex()
 { return getPatternName(); }
 
 /**
  *do unification of <code> this </code> and <code> t </code>
  * and store in <code> s </code> substitution.
  *@return true, if unification was succesfull.
  **/
 public abstract boolean   freeUnify(Term t, Substitution s)
                                                   throws TermWareException;   

 
 
 /**
  * unification when we already have <code> s </code>
  * and when same propositional variables means same things
  **/
 public abstract boolean    boundUnify(Term t, Substitution s) 
                                                   throws TermWareException;    
 
 
 
 
 /**
  * apply substitution <code> s </code> to current term.
  **/
 public abstract boolean   substInside(Substitution s) throws TermWareException;    


  /**
   * receive new term, which is sibstution of current term and <code> s </code>
   **/
 public abstract Term   subst(Substitution s) throws TermWareException;    


  /**
   * Equality when all propositional variables are equal
  *@param x - term to compare.
  *@return true, if terms are free-equals.
  **/
 public abstract boolean  freeEquals(Term x) throws TermWareException;


 /**
  * equality, when propositional variables are already bounded. 
  *@param x - term to compare.
  *@return true, if terms are bound-equals.
  **/ 
 public abstract boolean  boundEquals(Term x) throws TermWareException;


 public boolean  containsSubtermBoundEqualsTo(Term x) throws TermWareException
 { 
   try {  
      int i = findSubtermIndexBoundEqualsTo(x);
      return true;
   }catch(SubtermNotFoundException ex){
       return false;
   }
 }
 
 
 public abstract int findSubtermIndexBoundEqualsTo(Term x) throws TermWareException;
 

 /**
  *deep clone of term.
  *  (only immunitable terms are not cloned)
  */
 public abstract Term    termClone() throws TermWareException;
 

 /**
  *  compare functions. 
  * define ordering on set of terms. Note, that ordering is not the same 
  * as Java build-in compare.
  *@param x - term to compare
  **/
 public abstract int      termCompare(Term x );


 /**
  * compare for 'more concrete' relation.
  *  i. e. <code> x.<(concrete)<(y) </code> means, that for each substitution
  *  of free variables <code> sx </code> exists substituion <code> sy </code> : 
  *  <code> x[sx] = y[sy] </code>. <br>
  * Substitution <code> s </code> store previously matched variables.
  */
 public abstract PartialOrderingResult   concreteOrder(Term x, Substitution s) throws TermWareException;

 /**
  * create term, with same name but new body.
  *If this term have arity 0, or other than original - throws exception.
  */
 public abstract Term    createSame(Term[] newBody)   throws TermWareException;

 /**
  * get minimal index of free propositional variable in term.
  *@return index of propositional variable with minimal index.
  **/ 
 public abstract int      minFv() throws TermWareException;

 /**
  * get maximum index of free propositional variable in term
  *@return index of propositional variable with maximal index.
  **/ 
 public abstract int      maxFv() throws TermWareException;

 /**
  * renumerate propositional variables in such case, that 
  *  <code> minFv(shiftFv(t))==newMinFv </code>
  **/
 public abstract void     shiftFv(int newMinFv) throws TermWareException;

 /**
  * true, if term does not contains free propositional variables.
  *   (fv-set for this term is empty.)
  *@return true, if fv_set does not contains free proporsitional variables.
  **/
 public abstract boolean  emptyFv();

 
 //
 // util:
 //

 /**
  * print term on <code> out </code>
  *@param out - PrintStream, where to print term.
  */
 public  void     print(PrintStream out)
 {
   PrintWriter writer = new PrintWriter(out);
   print(writer);
   writer.flush();
 }

 /**
  * print term on <code> out </code>
  *@param out - PrintWriter, where to print term.
  */
 public  abstract void     print(PrintWriter out);
 
 

 /**
  * println term on <code> out </code>.
  *(i. e. effect of t.println(out) is equals to:
  *<pre>
  * t.print(out);
  * out.println();
  *</pre>
  */
 public  final void println(PrintStream out)
 { print(out); out.println(); }
 
/**
  * println term on <code> out </code>.
  *(i. e. effect of t.println(out) is equals to:
  *<pre>
  * t.print(out);
  * out.println();
  *</pre>
  */
 public  final void println(PrintWriter out)
 { print(out); out.println(); }
  
 
}
