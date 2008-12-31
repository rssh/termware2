/*
 * ListInArrayTerm.java
 *
 *
 * Copyright (c) 2006-2008 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware;

import ua.gradsoft.termware.exceptions.AssertException;
import ua.gradsoft.termware.exceptions.TermIndexOutOfBoundsException;

/**
 *List, which look's like cons(x,y) but point to array of terms.
 * @author Ruslan Shevchenko
 */
public final class ListInArrayTerm extends AbstractComplexTerm
{
    
    ListInArrayTerm(Term[] array, int index)
    {
      array_=array;
      index_=index;
    }
    
    public String getName()
    {
       return TermWareSymbols.CONS_STRING;   
    }
    
    public Object getNameIndex()
    {
        return TermWareSymbols.CONS_INDEX;
    }
    
    
    public int getArity() {
        return 2;
    }

       
    public Term createSame(Term[] newBody) throws TermWareException {
      switch(newBody.length) {
          case 0:
              return TermFactory.createNIL();
          case 1:
              return new ListTerm(newBody[0], TermFactory.createNIL());
          case 2:
              return new ListTerm(newBody[0], newBody[1]);
          default:
              throw new AssertException("invalid length of array for list constructor");              
      }
    }

    public Term getSubtermAt(int i) {
        switch(i) {
            case 0: return array_[index_];
            case 1: {
                if (snd_!=null) {
                    return snd_;
                }else if (index_==array_.length-1) {
                    return NILTerm.getNILTerm();
                }else{
                    return new ListInArrayTerm(array_,index_+1);
                }
            }
            default:
                throw new TermIndexOutOfBoundsException(this,i);
        }
    }

    public void setSubtermAt(int i, Term t) {
        switch(i) {
            case 0: 
                array_[index_]=t;
                if (emptyFv_) {
                    emptyFv_ &= t.emptyFv();
                }
                break;
            case 1:
                snd_=t;
                if (emptyFv_) {
                    emptyFv_ &= snd_.emptyFv();
                }
                break;
            default:
                throw new TermIndexOutOfBoundsException(this,i);
        }
    }
 
    
  /**
  * recheck existance of propositional variables in subterms.
  */
 protected  void   recheckEmptyFv()
 {
     emptyFv_=true;
     if (snd_!=null) {
        emptyFv_ &= array_[index_].emptyFv();
        emptyFv_ &= snd_.emptyFv();        
     }else{
       for(int i=index_; i<array_.length; ++i) {
         if (!array_[i].emptyFv()) {
           emptyFv_=false;
           break;
         }
       }
     }
 }

    public Term termClone() throws TermWareException
    {
      return new ListTerm(array_[index_].termClone(),getSubtermAt(1).termClone());  
    }
    
    public PartialOrderingResult concreteOrder(Term x, Substitution s) throws TermWareException
    {  
        return termClone().concreteOrder(x,s);
    }
    
    private Term[] array_;

    private int index_;
    private Term  snd_=null;
    
}
