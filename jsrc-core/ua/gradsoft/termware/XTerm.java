package ua.gradsoft.termware;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 
 * (C) Grad-Soft Ltd, Kiev, Ukraine
 * 2002-2004
 */

import java.io.*;
import java.math.*;

import java.util.Collections;
import java.util.Map;
import ua.gradsoft.termware.exceptions.*;

/**
 * Term, which represent propositional variable.
 *
 *  when propositional variable is free, isX is true and minFv - number of this variable.
 *  when we bind propositional variable with some value - it's act as proxy.
 *    (during unifications, we do 'unproxying' to avoid memory leaks)
 **/                           
public final class XTerm extends Term
{

 XTerm()
  { v_ = 1; }

 XTerm(int v)
  { v_ = v; }

 public final int getPrimaryType0()
 {
     if (isProxy()) return proxy_.getPrimaryType0();
     return PrimaryTypes.X;
 }

 public final boolean  isNil()
   { if (isProxy()) return proxy_.isNil();
     return false; }

 public final boolean  isAtom()
   { if (isProxy()) return proxy_.isAtom();
     return false; }

 public final boolean  isBoolean()
   { if (isProxy()) return proxy_.isBoolean();
     return false; }

 public final boolean  getBoolean() 
   { if (isProxy()) return proxy_.getBoolean();
     throw new UnsupportedOperationException(); }

 public final boolean  isNumber()
 { if (isProxy()) return proxy_.isNumber(); 
   return false; }
 
 public final Number getNumber() 
   { if (isProxy()) return proxy_.getNumber();
     throw new UnsupportedOperationException(); }

 public final boolean  isByte()
   { if (isProxy()) return proxy_.isByte();
     return false; }

 public final byte getByte() 
   { if (isProxy()) return proxy_.getByte();
     throw new UnsupportedOperationException(); }

  public final boolean  isBigDecimal()
   { if (isProxy()) return proxy_.isBigDecimal();
     return false; }

 public final BigDecimal getBigDecimal()
   { if (isProxy()) return proxy_.getBigDecimal();
     throw new UnsupportedOperationException(); }
  
 public final boolean  isBigInteger()
   { if (isProxy()) return proxy_.isBigInteger();
     return false; }

 public final BigInteger getBigInteger() 
   { if (isProxy()) return proxy_.getBigInteger();
     throw new UnsupportedOperationException(); }
 
 public final boolean  isShort()
   { if (isProxy()) return proxy_.isShort();
     return false; }

 public final short getShort() 
   { if (isProxy()) return proxy_.getShort();
     throw new UnsupportedOperationException(); }
 

 public final boolean  isInt()
   { if (isProxy()) return proxy_.isInt();
     return false; }

 public final int  getInt() 
   { if (isProxy()) return proxy_.getInt();
     throw new UnsupportedOperationException(); }

 public final boolean  isLong()
   { if (isProxy()) return proxy_.isLong();
     return false; }

 public final long  getLong() 
   { if (isProxy()) return proxy_.getLong();
     throw new UnsupportedOperationException(); }

 
 public final boolean  isDouble()
   { if (isProxy()) return proxy_.isDouble();
      return false; }

 public final double getDouble() 
   { if (isProxy()) return proxy_.getDouble();
     throw new UnsupportedOperationException(); }

 public final boolean  isFloat()
   { if (isProxy()) return proxy_.isFloat();
      return false; }

 public final float getFloat() 
   { if (isProxy()) return proxy_.getFloat();
     throw new UnsupportedOperationException(); }

 
 public final boolean  isString()
   { if (isProxy()) return proxy_.isString();
     return false; }

 public final String getString() 
   { if (isProxy()) return proxy_.getString();
     throw new UnsupportedOperationException(); }

 public final boolean  isChar()
   { if (isProxy()) return proxy_.isChar();
     return false; }

 public final char getChar()
   { if (isProxy()) return proxy_.getChar();
     throw new UnsupportedOperationException(); }


 public final boolean  isX()
   { if (isProxy()) return proxy_.isX();
    return true; }

 
 
 public final boolean  isComplexTerm()
   { if (isProxy()) return proxy_.isComplexTerm();
     return false; }

 public final boolean  isJavaObject()
   { if (isProxy()) return proxy_.isJavaObject();
     return false; }

 
 public final Object   getJavaObject() 
   { if (isProxy()) return proxy_.getJavaObject();
     throw new UnsupportedOperationException();
   }

 public final Term   getTerm()
   { if (isProxy()) return proxy_.getTerm();
     return this;
   }


 public final String   getName()
   { if (isProxy()) return proxy_.getName(); 
     if (name_==null) name_ = "x"+new Integer(v_).toString(); 
     return name_;
   }

 public final Object  getNameIndex() 
 {
    if (isProxy()) return proxy_.getNameIndex();
    return getName();
 }
 
 public final String   getPatternName()
   { if (isProxy()) return proxy_.getPatternName(); 
     return getName(); 
   }

 public final Object   getPatternNameIndex() 
   { if (isProxy()) return proxy_.getPatternNameIndex(); 
     return getNameIndex();
   }


 public int      getArity()
  { if (isProxy()) return proxy_.getArity(); return 0; }

 public Term    getSubtermAt(int i) 
  { if (isProxy()) return proxy_.getSubtermAt(i);
    throw new UnsupportedOperationException(); }


 public void   setSubtermAt(int i, Term t) throws TermWareException
  { if (isProxy()) proxy_.setSubtermAt(i, t);
    throw new UnsupportedOperationException(); }


 
  /**
  * return unification of <code> this </code> and <code> t </code>
  * and store in <code> s </code> substitution.
  **/
  public boolean freeUnify(Term t, Substitution s) throws TermWareException 
  {
    if (isProxy()) return proxy_.freeUnify(t,s);
    if (t.minFv() <= v_)
        t.shiftFv(v_+1);
    return boundUnify(t,s);      
  } 

 
 
 /**
  * unification when we already have <code> s </code>
  **/
  public boolean boundUnify(Term t, Substitution s) throws TermWareException 
 {
  if (isProxy()) return proxy_.boundUnify(t,s);
  try {
   if (!t.isX()) {
      s.put(this,t);
      return true;
   }else{
      if (t.minFv()==v_) return true; 
      s.put(this,t);
      return true;
   }
  }catch(MatchingFailure ex){
    return false;
  }
 }
 



 /**
  * apply substitution s to current term.
  **/                
 public boolean    substInside(Substitution s) throws TermWareException
 {
  if (isProxy()) return proxy_.substInside(s);
  Term v = s.get(v_); 
  if (v!=null) {
    if (v.isX()) {
      v_ = v.minFv();
    }else{
      proxy_ = v;
    }
    return true;    
  }else{
    return false;
  }   
 }



 public Term subst(Substitution s)   throws TermWareException
 {
  if (isProxy()) return proxy_.subst(s);
  Term v = s.get(v_); 
  if (v!=null) {
     return v;
  }else{
     return termClone();
  }
 }




 public boolean  freeEquals(Term t) throws TermWareException
 {
  if (isProxy()) return proxy_.freeEquals(t);
  return t.isX();
 }




 public boolean boundEquals(Term t) throws TermWareException
 {
  if (isProxy()) return proxy_.boundEquals(t);
  return t.isX() && t.minFv() == v_ ; 
 }

  public int findSubtermIndexBoundEqualsTo(Term x) throws TermWareException {
      if (isProxy()) return proxy_.findSubtermIndexBoundEqualsTo(x);
      throw new SubtermNotFoundException(x, this);
  }

 
 
  public  final int termCompare(Term t)
    { 
        if (isProxy()) return proxy_.termCompare(t); 
        int x = PrimaryTypes.X - t.getPrimaryType0();
        if (x!=0) return x;
        // all x-ses are equal.
        return 0;
    } 

  
  
 public Term    termClone() throws TermWareException
 {
  if (isProxy()) return proxy_.termClone();
  return new XTerm(v_);
 }

 public final    Term createSame(Term[] newBody) throws TermWareException
 {
  if (isProxy()) return proxy_.createSame(newBody);
  throw new UnsupportedOperationException();
 }


 public final boolean emptyFv() 
 {
  if (isProxy()) return proxy_.emptyFv();
  return false;
 }

 /**
  *return result of 'concreteOrder' comparison with x.  
  *TODO: rethink.
  */
 public PartialOrderingResult concreteOrder(Term x, Substitution s) throws TermWareException
 {
   if (isProxy()) return proxy_.concreteOrder(x,s);  
   if (x.isX()) {
       Term xt = s.get(x.minFv());
       if (xt!=null) {
           if (xt.isX()) {
               if (xt.minFv()==minFv()) {
                   return PartialOrderingResult.EQ;
               }else{
                   
                   return PartialOrderingResult.MORE;
               }
           }else{
               return PartialOrderingResult.MORE;
           }      
       }else{
           s.put(this,x);
           return PartialOrderingResult.MORE;
       }
   }else{       
      return PartialOrderingResult.MORE;
   }
 }

 public Map<String,Term> getAttributes()
 {
     if (isProxy()) {
         if (proxy_ instanceof Attributed) {
             return ((Attributed)proxy_).getAttributes();
         }else{
             return Collections.emptyMap();
         }
     }else{
         return Collections.emptyMap();
     }
 }
 
 public Term getAttribute(String attrName)
 {
     if (isProxy()) {
         if (proxy_ instanceof Attributed) {
             return ((Attributed)proxy_).getAttribute(attrName);
         }else{
             return NILTerm.getNILTerm();
         }
     }else{
         return NILTerm.getNILTerm();
     }     
 }

 public void setAttribute(String name, Term value)
 {
     if (isProxy()) {
         if (proxy_ instanceof Attributed) {
             ((Attributed)proxy_).setAttribute(name, value);
         }else{
             proxy_ = TermHelper.setAttribute(proxy_, name, value);
         }
     }else{
         throw new RuntimeAssertException("Can't set attribute to free variable");
     }
 }

 /*
  */
 public final int      minFv()  throws TermWareException
  {
   if (isProxy()) return proxy_.minFv();
   return v_;
  }

 public final int      maxFv()  throws TermWareException 
  {
   if (isProxy()) return proxy_.maxFv();
   return v_;
  }

 public final void shiftFv(int newMinFv) throws TermWareException
  {
   if (isProxy()) proxy_.shiftFv(newMinFv);
   else
      v_ = newMinFv;
  }

 public final int getXIndex()
 {
     return v_;
 }
 

 public    final void print(PrintWriter out) 
  { if (isProxy()) proxy_.print(out);
    else out.print("$"+v_); }


 
 private  final boolean isProxy()  { return proxy_ != null; }
 
 private  Term proxy_;
 private  int   v_;
 private  transient String name_;



}
