/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2009
 * (C) Grad-Soft Ltd, Kiev, Ukraine.
 * http://www.gradsoft.ua
 */

package ua.gradsoft.termware;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import ua.gradsoft.termware.exceptions.FVLimitReachedException;
import ua.gradsoft.termware.exceptions.IncorrectTermException;
import ua.gradsoft.termware.exceptions.SubtermNotFoundException;
import ua.gradsoft.termware.util.FVSet;

                           
/**
 * Abstract complex term. 
 * This is term like <code> f(x1,.. xN). </code> 
 * Contains some subclass-specific storage for subterms.
 **/
public abstract class AbstractComplexTerm extends Term
{
    
 /**
  *get primary type of the term.
  *@return PrimaryTypes.COMPLEX_TERM
  */
    public final int getPrimaryType0()
 { return PrimaryTypes.COMPLEX_TERM; }

 /**
  * get name of term. (i. e. functional symbold)
  */   
 public abstract String   getName();

 /**
  * get name index in bounded symbol table or sust string, if name in symbol
  *table does not exists.
  */
 public abstract Object getNameIndex();

 /**
  * get name, for which we can forse unfication.
  */
 @Override
 public String  getPatternName()
       { return getName(); }

 /**
  * get index for pattern name.
  */
 @Override
 public Object getPatternNameIndex()
       { return getNameIndex(); }


 /**
  *get arity of term.
  *@return arity of term
  */
 public abstract int      getArity();

 /**
  * get i-th subterm.
  *@param i - index of subterm to get.
  *@return subterm, which located by index <code> i </code>
  *@exception TermIndexOutOfBoundsException when <code> i>getArity() </code>
  *@exception InvalidArgumentException when <code> i < 0 </code>
  */
 public abstract Term    getSubtermAt(int i);

 /**
  * set i-th subterm.
  * 
  * @param i - indext of subterm to set.
  * @param t - subterm to set.
  * @throws ua.gradsoft.termware.TermWareException
  */
 public abstract void     setSubtermAt(int i, Term t) throws TermWareException;

 public abstract Term    createSame(Term[] newBody)
                                              throws TermWareException;

 /**
  * do 'deep-clone' of object.
  * i. e. after calling termClone we sure, that call of setSubtermAt() 
  * in any subtree of deepClone of original term does not affect
  * original term.
  **/
 public abstract Term    termClone()  throws TermWareException;

 @Override
 public final Object clone()
 {
   try {
     return termClone();  
   }catch(TermWareException ex){
       throw new TermWareRuntimeException(ex);
   }
 }


 /**
  * is this term is nil ?
  *@return false
  */
 public final boolean  isNil()
   { return false; }

 /**
  * is this term is Atom ?
  *@return false
  */
 public final boolean  isAtom()
   { return false; }

 /**
  * is this term is Boolean ?
  *@return false
  */
 public final boolean  isBoolean()
   { return false; }

 /**
  * in this class throws UnsupportedOperationException
  */
 public final boolean  getBoolean() 
   { throw new UnsupportedOperationException(); }

 
 /**
  * is this term is Number ?
  *@return false
  */
 public final boolean isNumber()
 { return false; }
 
 
 /**
  * in this class throws UnsupportedOperationException
  */
 public final Number  getNumber() 
   { throw new UnsupportedOperationException(); }


  /**
  * is this term is Byte ?
  *@return false
  */
 public final boolean  isByte()
   { return false; }
 
 /**
  * in this interface throws UnsupportedOperationException
  */
 public final byte  getByte() 
   { throw new UnsupportedOperationException(); }

 
 /**
  * is this term is Short ?
  *@return false
  */
 public final boolean  isShort()
   { return false; }
 
 /**
  * in this class throws UnsupportedOperationException
  */
 public final short  getShort()
   { throw new UnsupportedOperationException(); }

 
 /**
  * is this term is Int ?
  *@return false
  */
 public final boolean  isInt()
   { return false; }

 /**
  * in this interface throws UnsupportedOperationException
  */
 public final int  getInt() throws UnsupportedOperationException
   { throw new UnsupportedOperationException(); }

 /**
  * is this term is Long ?
  *@return false
  */
 public final boolean  isLong()
   { return false; }

  /**
  * in this interface throws UnsupportedOperationException
  */
 public final long  getLong() throws UnsupportedOperationException
   { throw new UnsupportedOperationException(); }

 /**
  * is this term is Float ?
  *@return false
  */
 public final boolean  isFloat()
   { return false; }

 /**
  * in this interface throws UnsupportedOperationException
  */
 public final float  getFloat()
   { throw new UnsupportedOperationException(); }

 
 /**
  * is this tern is Double ?
  *@return false
  */
 public final boolean  isDouble()
   { return false; }

 /**
  *@see Term#getDouble()
  *here throws UnsupportedOperationException
  */
 public final double  getDouble() 
   { throw new UnsupportedOperationException(); }

 /**
  * is this tern is BigDecimal ?
  *@return false
  */
 public final boolean  isBigDecimal()
   { return false; }
 
 /**
  * in this interface throws UnsupportedOperationException
  */
 public final BigDecimal  getBigDecimal()
   { throw new UnsupportedOperationException(); }

 /**
  * is this tern is BigInteger ?
  *@return false
  */
 public final boolean  isBigInteger()
   { return false; }
 
 
 /**
  * in this interface throws UnsupportedOperationException
  */
 public final BigInteger  getBigInteger() throws UnsupportedOperationException
   { throw new UnsupportedOperationException(); }

 
 /**
  * is this term is Char ?
  *@return false
  */ 
 public final boolean  isChar()
   { return false; }

  /**
  * in this interface throws UnsupportedOperationException
  */
 public final char getChar() 
   { throw new UnsupportedOperationException(); }


 
 
 /**
  * is this tern is String ?
  *@return false
  */ 
 public final boolean  isString()
   { return false; }

  /**
  * in this interface throws UnsupportedOperationException
  */
 public final String getString()
   { throw new UnsupportedOperationException(); }

 
 /**
  * is this term is Java Object ?
  *@return false.
  */
 public final boolean isJavaObject()
 { return false; }
 
 
  /**
  * in this class throws UnsupportedOperationException
  */
 public final Object getJavaObject() 
  { throw new UnsupportedOperationException(); }


 /**
  * is this term is X ?
  *@return false.
  */
 public final boolean  isX()
   { return false; }

 
  /**
  * in this class throws UnsupportedOperationException
  */
 public final int getXIndex()
  { throw new UnsupportedOperationException(); }

 
 /**
  * is this term is ComplexTerm  ?
  *@return true.
  */
 public final boolean  isComplexTerm()
   { return true; }

 /**
  * return term
  */ 
 public Term getTerm()
  { return this; }

 
 /**                                              
  * return unification of <code> this </code> and <code> t </code>
  * and store in <code> s </code> substitution.
  **/
 public boolean freeUnify(Term t, Substitution s) throws TermWareException 
 {
  if (!emptyFv()||!t.emptyFv()) {
    if (t.minFv() <= maxFv()) {
      try {
       t.shiftFv(maxFv()+1);
      }catch(FVLimitReachedException ex){
       normalizeFv(1);
       t.shiftFv(maxFv()+1);
      }
    }
  }
  return boundUnify(t, s);
 }
 
 
 public boolean boundUnify(Term t, Substitution s) throws TermWareException 
 {
   if(t.isNil()) return false;
   else if(PrimaryTypes.isPrimitive(t.getPrimaryType0())) return false;
   else if(t.isX()) {
     //if (concretizeOnly) {
     //  if (!emptyFv()) return false;
     //}
     s.put(t,this);
     return true;
   }else {
     if (t.getArity()==getArity()) {
         if (TermHelper.compareNameIndexes(getNameIndex(),t.getNameIndex())!=0) return false;
         for(int i=0; i<getArity(); ++i) {
            Term st1=getSubtermAt(i);
            Term st2=t.getSubtermAt(i);
            if (!st1.boundUnify(st2,s)) {
              return false;
            }
         }         
         return true;
     }else{       
         return false;
     }
   }  
 }

 
 


 /**
  * apply substitution <code> s </code> to current term.
  **/ 
 public boolean substInside(Substitution s) throws TermWareException
 {
  boolean retval=false;
  emptyFv_=true;
  for(int i=0; i<getArity(); ++i) {  
    Term t=getSubtermAt(i);
    if (t.substInside(s)) {
      retval=true;
    }
    if (emptyFv_ && !t.emptyFv()) {
        emptyFv_=false;
    }
  }
  fv_=null;
  return retval;
 }


  /**
   * receive new term, which is sibstution of current term and <code> s </code>
   **/
 public Term subst(Substitution s) throws TermWareException
 {  
  Term[] newBody = new Term[getArity()];
  for(int i=0; i<getArity(); ++i) {
    newBody[i]=getSubtermAt(i).subst(s);
  }
  Term result=createSame(newBody);
  return result;
 }  



  /**
   * Equality when all propositional variables are equal
   **/
 public boolean freeEquals(Term x) throws TermWareException
 {
  if (x.getArity()!=getArity() || !x.getNameIndex().equals(getNameIndex())) 
    return false;
  for(int i=0; i<getArity(); ++i) {
    if (!getSubtermAt(i).freeEquals(x.getSubtermAt(i))) {
      return false;
    }
  }
  return true;  
 }



 public boolean boundEquals(Term t) throws TermWareException
 {
  if (t.getArity()!=getArity()) return false;
  if (!t.isComplexTerm()) return false;
  if (!getNameIndex().equals(t.getNameIndex())) return false;
  for(int i=0; i<getArity(); ++i) {
    if (!getSubtermAt(i).boundEquals(t.getSubtermAt(i))) {
      return false;
    }
  }
  return true;  
 }


 public int findSubtermIndexBoundEqualsTo(Term t) throws TermWareException
 {
   for(int i=0; i<getArity(); ++i) {
       if (getSubtermAt(i).boundEquals(t)) {
           return i;
       }
   }
   throw new SubtermNotFoundException(t,this);
 }
 

 /**
  *  compare function.
  **/
 public int termCompare(Term t) 
 {
   int x=PrimaryTypes.COMPLEX_TERM - t.getPrimaryType0();  
   if (x!=0) return x;
   x=getArity() - t.getArity();
   if (x!=0) return x;
   x=TermHelper.compareNameIndexes(getNameIndex(),t.getNameIndex());
   if (x!=0) return x;
   for(int i=0; i<getArity(); ++i) {
     x=getSubtermAt(i).termCompare(t.getSubtermAt(i));
     if (x!=0) return x;
   }
   return 0;
 }

 
 public boolean  emptyFv() 
  { return emptyFv_; }

 public int      minFv() throws TermWareException
  { if (emptyFv_) return -1;
    lazyInitFv(); return fv_.getMin(); 
   }

 public int      maxFv()  throws TermWareException
  { if (emptyFv_) return -1;
    lazyInitFv(); return fv_.getMax(); }

 
 /**
  * shift set of propositional variables to start from <code> newMinFv </code>
  *@param newMinFv  new minimal index of propositional variable in term
  */
 public void     shiftFv(int newMinFv) throws TermWareException
  { 
    if (emptyFv_) return;
    lazyInitFv(); 
    try{ 
     fv_.shift(newMinFv); 
    }catch(FVLimitReachedException e){
      throw new IncorrectTermException("Can't normalize FV: too many variables");
    }
  }

 public void     normalizeFv(int i) throws TermWareException
  { lazyInitFv(); 
    fv_.normalize(i); 
  }

 
 public  void     print(PrintWriter out)
 {
    out.print(getName());
    out.print("(");
    for(int i=0; i<getArity(); ++i) {
      Term xt=getSubtermAt(i);
      if (xt==null) out.print("NULL");
      else xt.print(out);
      if (i!=getArity()-1) {
        out.print(",");
      }
    }
    out.print(")");
 }


 
 
 /**
  * recheck existance of propositional variables in subterms.
  *  (called by childrens afer non-optimized structure-unsafe operations)
  */
 protected  void   recheckEmptyFv()
 {
     emptyFv_=true;
     for(int i=0; i<getArity(); ++i) {
       if (!getSubtermAt(i).emptyFv()) {
           emptyFv_=false;
           break;
       }
     }
 }
 
 protected final void   resetFV() 
 { fv_=null; }


 private  void lazyInitFv()  throws IncorrectTermException
 { if (!emptyFv_ && fv_==null) fv_ = new FVSet(this); }

 
 
 private  transient FVSet  fv_=null;
 
 protected  transient boolean emptyFv_=false;

   
}
