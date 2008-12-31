package ua.gradsoft.termware.set;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002,2003,2004
 * $Id: SetTerm.java,v 1.4 2007-05-16 12:04:22 rssh Exp $
 */

import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.util.*;

                           
/**
 * set term, - subterms are elements of set, ordered in our natural order
 **/
public class SetTerm extends AbstractSetTerm
{

 public SetTerm(Term[] body) 
   {
    subterms_=new SetOfTerms();
    subterms_.insert(body);
    recheckEmptyFv();
   }
 
 
 public SetTerm() throws TermWareException
 {
   subterms_=new SetOfTerms();
   recheckEmptyFv();
 }

  public SetTerm(SetOfTerms subterms) throws TermWareException
  {
    subterms_=subterms;
    recheckEmptyFv();
  }
 
  SetTerm(SetOfTerms subterms, boolean newEmptyFv, boolean newSubterms) throws TermWareException
  {
   if (newSubterms) {
    subterms_=new SetOfTerms();
    subterms_.insert(subterms);
   }else{
    subterms_=subterms;
   }
   emptyFv_=newEmptyFv;
  }

  

 public Term createSame(Term[] newBody) throws TermWareException
  {
    return new SetTerm(newBody);
  }

  
 
 /**
  *return arity of set.
  */
 public final int      getArity()
   { return subterms_.getSize(); }

 /**
  * get i-th subbterm.
  *@param i - index of term in set
  *@return i-th subterm of set.
  */
 public Term getSubtermAt(int i)
   { if (i > getArity() || i < 0) {
       throw new TermIndexOutOfBoundsException(this,i);
     }
     return subterms_.getAt(i);
   }

 public void   setSubtermAt(int i, Term t) throws TermWareException
   { if (i > getArity() || i < 0) {
       throw new TermIndexOutOfBoundsException(this,i);
     }
     Term prev=subterms_.getAt(i);
     subterms_.remove(i);
     subterms_.insert(t);
     if (emptyFv_) {
         if (!t.emptyFv()) {
             emptyFv_=false;
             resetFV();
         }else{
             emptyFv_=true;
         }
     }else{
         if (t.emptyFv()) {
             recheckEmptyFv();
         }
         resetFV();
     }
     
   }

   

 /**
  * apply substitution <code> s </code> to current term.
  *@return true, if substitution change term, otherwise false
  **/
 public boolean    substInside(Substitution s) throws TermWareException
 {
  boolean result = false;
  emptyFv_=true;
  for(int i=0; i<getArity(); ++i) {  
    Term current=getSubtermAt(i);
    if (current.substInside(s)) {
       result=true;
    }
    if (emptyFv_ && !current.emptyFv()) {
        emptyFv_=false;
    }
  }
  if (result) {
    resetFV();
    SetOfTerms newSubterms = new SetOfTerms();
    newSubterms.insert(subterms_);
    subterms_ = newSubterms;
  }
  return result;
 }



//
//  RR primitives
//

 public  void  insert(Term t) 
 {
   subterms_.insert(t);
   if (emptyFv_) {
       if (!t.emptyFv()) {
           emptyFv_=false;
       }
   }
   resetFV(); // TODO - keep fv_;
 }
 
 
 public final boolean isEmpty()
 {
  return subterms_.isEmpty();   
 }
 
 public int index(Term t) throws TermWareException {
     return subterms_.index(t);
 } 
 
 public int indirectionLevel() {
     return 0;
 }
  
 
 //public void adoptName(SymbolTable symbolTable, boolean addNames) {
 //}
 
 static  SetTerm  recreateWithBodyAs(Term t)
 {
    Term[] newBody = new Term[t.getArity()];
    for(int i=0; i<t.getArity(); ++i) {
        newBody[i]=t.getSubtermAt(i);
    }
    return new SetTerm(newBody);
 }
 
//
// Internal variables.
//

 private  SetOfTerms subterms_;

}
