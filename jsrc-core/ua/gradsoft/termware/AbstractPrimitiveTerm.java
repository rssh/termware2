package ua.gradsoft.termware;
import java.io.*;
import java.math.*;


import ua.gradsoft.termware.exceptions.MatchingFailure;
import ua.gradsoft.termware.exceptions.SubtermNotFoundException;
                           

/**
 * this is abstract class for primitive term (i. e. term, which
 * contains primitive value, like int or String)
 **/                           
public abstract class AbstractPrimitiveTerm extends Term
{

    /**
     *@return false
     */
 public boolean  isNil()
 { return false; }
 
 public boolean  isAtom()
 { return false; }

 public boolean  isBoolean()
 { return false; }

 public          boolean  getBoolean() 
   { throw new UnsupportedOperationException(); }

 public boolean isByte()
 { return false; }
 
 public byte  getByte() 
   { throw new UnsupportedOperationException(); }

 public boolean isShort()
 { return false; }
 
 public short  getShort()  
   { throw new UnsupportedOperationException(); }
 
 public  boolean  isInt()
 { return false; }

 public int  getInt()   
   { throw new UnsupportedOperationException(); }

 public  boolean  isLong()
 { return false; }

 public long  getLong()  
   { throw new UnsupportedOperationException(); }

 public  boolean  isBigDecimal()
 { return false; }

 public BigDecimal  getBigDecimal()   
   { throw new UnsupportedOperationException();  }

 public  boolean  isBigInteger()
 { return false; }

 public BigInteger  getBigInteger() 
   { throw new UnsupportedOperationException();  }

 public boolean  isFloat()
  {  return false; }

 
 public float   getFloat()  
   { throw new UnsupportedOperationException(); }

 
 public boolean  isDouble()
  {  return false; }

 
 public double   getDouble()   
   { throw new UnsupportedOperationException(); }


 public boolean  isString()
 { return false; }

 public String   getString() 
   { throw new UnsupportedOperationException(); }

 public boolean  isChar()
 { return false; }

 public char    getChar()
   { throw new UnsupportedOperationException(); }


 public final boolean  isX()
 { return false; }


 public Term  getTerm()
  { return this; }

 public final boolean  isComplexTerm() 
   { return false; }
 
 public final boolean  isJavaObject()
 { return false; }

 public final Object getJavaObject() 
 { throw new UnsupportedOperationException(); }
 
 public abstract String   getName();  
 
 
 public Object getNameIndex() 
 { throw new UnsupportedOperationException(); }
 
 public final String   getPatternName()
   { return getName(); }
 
 public final Object getPatternNameIndex()
 { return getNameIndex(); }

 public final int getXIndex() 
 { throw new UnsupportedOperationException(); }
 
 /**
  *@return 0
  */
 public final int      getArity() { return 0; }

 /**
  *throws UnsupporedOperationException
  *@exception UnsupportedOperationException
  *@see Term#getSubtermAt(int i)
  */
 public final Term  getSubtermAt(int i)
   { throw new UnsupportedOperationException(); }


 public final void  setSubtermAt(int i, Term t)  
   { throw new UnsupportedOperationException(); }


 
 
 /**
  * free unification
  */
 public final boolean freeUnify(Term t, Substitution s) throws TermWareException 
 {
   return boundUnify(t,s); 
 } 

 /** 
  * bound unification.
  */
 public boolean boundUnify(Term t, Substitution s) throws TermWareException 
 {
  boolean retval=false;
  if (eq(t)) {
     retval=true;
   }else{
     if (t.isX()) {
        try {
          s.put(t,this); 
          retval=true;
        }catch(MatchingFailure x){
          return retval;
        }
     }
   }
   return retval;     
 }
 
 
 
 /**
  * substitution
  */ 
 public final Term subst(Substitution s)
   { return this; }


 public final boolean substInside(Substitution s)
   { return false; }


 
 public final boolean freeEquals(Term t) throws TermWareException
   { return eq(t); }



 public final boolean boundEquals(Term t) throws TermWareException
   { return eq(t); }

 
 public abstract boolean  eq(Term x) throws TermWareException;

 /**
  * clone
  */
 public abstract Term    termClone();
 
 public final Object clone()
 { return termClone(); }
                      
 
 public abstract int termCompare(Term x);

 public PartialOrderingResult    concreteOrder(Term x, Substitution s) throws TermWareException
 {
   PartialOrderingResult result=null;
   if (x.isX()) {
       Term t=s.get(x.minFv());
       if (t!=null) {
           result=concreteOrder(t,s);
       }else{
           s.put(x,this);
           result=PartialOrderingResult.LESS;
       }
   }else{
       result=(termCompare(x)==0 ? PartialOrderingResult.EQ: PartialOrderingResult.NOT_COMPARABLE);
   }
   return result;
 }
 
 
 public final int  findSubtermIndexBoundEqualsTo(Term x) throws SubtermNotFoundException
 { throw new SubtermNotFoundException(x,this); }
 
 
 public final Term    createSame(Term[] newBody)
   { throw new UnsupportedOperationException(); }
                         
                            
 /**
  * print term to out. does not expect recursion.
  **/   
 public abstract void print(PrintWriter out);



 public final boolean emptyFv() { return true; }
 public final int minFv()  { return -1; }
 public final int maxFv()  { return -1; }

 public final void shiftFv(int newMin) {}

  
 
 
}
