/*
 * SetTermWithoutElement.java
 *
 */

package ua.gradsoft.termware.set;

import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;

/**
 *Set, which is a holder of (SetTerm withous some removed element).
 *This class is used for optimization of set-patterns - we must be
 *able create temporary instances of this class without copying of undelaying
 *set.
 * @author  Ruslan Shevchenko
 */
class SetTermWithoutElement extends AbstractSetTerm {
    
    
    SetTermWithoutElement(AbstractSetTerm origin, int removedIndex)
    {
      proxySetTerm_=null;  
      originalSetTerm_=origin;  
      indexOfAbsentElement_=removedIndex;  
      arity_=origin.getArity()-1;
      indirectionLevel_=origin.indirectionLevel()+1;
      if (origin.getSubtermAt(removedIndex).emptyFv()) {
          emptyFv_=origin.emptyFv();
      }else{
          if (!origin.emptyFv()) recheckEmptyFv();
      }
      // note - we must have '>' here and '>=' in get methods.
      //   why - becouse we create temporary SetTermWithoutElement 
      //   during unsuccesfull matches.
      //  So, to minimize useless createProxy operations we will introduce
      //   createProxy call on getSubtermAt and getTerm methods, where we know,
      //    that term is constructed.
      //    And calling createProxy in get will reduce indirectionLevel
      //    and prevent us from call of next two string.
      //   (but this two strings is necessory, where we have only matching
      //    operations without call of getSubtermAt().
      if (indirectionLevel_ > AbstractSetTerm.MAX_INDIRECTION_LEVEL_) {
          createProxy();
      }
    }
    
    public Term getTerm()
    {
      if (indirectionLevel_ >= AbstractSetTerm.MAX_INDIRECTION_LEVEL_) {
            createProxy();
      }
      if (proxySetTerm_!=null) return proxySetTerm_;
      return this;
    }
    
    
    public Term createSame(Term[] newBody) throws TermWareException {
        return new SetTerm(newBody);
    }
    
    public int getArity() {
        if (proxySetTerm_!=null) return proxySetTerm_.getArity();
        return arity_;
    }
    
    public Term getSubtermAt(int i) {
        if (proxySetTerm_==null) {
          if (indirectionLevel_ >= AbstractSetTerm.MAX_INDIRECTION_LEVEL_) {
              createProxy();
          }
          if (proxySetTerm_!=null) return proxySetTerm_.getSubtermAt(i);
        }else{
          return proxySetTerm_.getSubtermAt(i);
        }
        if (i<indexOfAbsentElement_) {
            return originalSetTerm_.getSubtermAt(i);
        }else{
            return originalSetTerm_.getSubtermAt(i+1);
        }
    }
    
    public int index(Term t) throws TermWareException {
        if (proxySetTerm_!=null) {
            return proxySetTerm_.index(t);
        }else{
            int originalIndex=originalSetTerm_.index(t);
            if (originalIndex==indexOfAbsentElement_) {
                throw new SubtermNotFoundException(t,this);
            }else if(originalIndex < indexOfAbsentElement_) {
                return originalIndex;
            }else{
                return originalIndex-1;
            }
        }
    }
    
    public int indirectionLevel() {
        if (proxySetTerm_!=null) return proxySetTerm_.indirectionLevel();
        return indirectionLevel_;
    }
    
    public void insert(Term t) throws TermWareException {
        createProxy();
        proxySetTerm_.insert(t);
    }
    
    public boolean isEmpty() {
        if (proxySetTerm_!=null) {
            return proxySetTerm_.isEmpty();
        }else{
            return arity_==0;
        }
    }
    
    public void setSubtermAt(int i, Term t) throws TermWareException {
        if (proxySetTerm_==null) createProxy();
        proxySetTerm_.setSubtermAt(i,t);
    }
    
    public boolean substInside(Substitution s) throws TermWareException
    {
        if (proxySetTerm_==null) createProxy();
        return proxySetTerm_.substInside(s);
    }
    
   
    private void createProxy() 
    {
      if (proxySetTerm_!=null) return;
      try {
        SetOfTerms proxySubterms=new SetOfTerms();
        boolean proxyEmptyFv=true;
        for(int i=0; i<indexOfAbsentElement_; ++i) {
          Term subterm=originalSetTerm_.getSubtermAt(i);
          proxyEmptyFv = proxyEmptyFv && subterm.emptyFv();
          proxySubterms.addAlreadySorted(subterm);
        }
        for(int i=indexOfAbsentElement_+1; i<originalSetTerm_.getArity(); ++i) {
          Term subterm=originalSetTerm_.getSubtermAt(i);
          proxyEmptyFv = proxyEmptyFv && subterm.emptyFv();
          proxySubterms.addAlreadySorted(subterm);
        }
        proxySetTerm_=new SetTerm(proxySubterms,proxyEmptyFv,false);
        originalSetTerm_=null;
      }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }
    }
    
    
   private AbstractSetTerm originalSetTerm_;
   private SetTerm proxySetTerm_;
   
   private int indexOfAbsentElement_;
   private int indirectionLevel_;
   
   private int arity_;
  
}
