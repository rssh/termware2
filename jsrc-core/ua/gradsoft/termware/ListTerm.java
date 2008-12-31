/*
 * ListTerm.java
 *
 */

package ua.gradsoft.termware;

import ua.gradsoft.termware.exceptions.*;

/**
 *Optimized representation for List term. (i. e. cons(x,cons(y)...) )
 * @author  Ruslan Shevchenko
 */
public final class ListTerm extends AbstractComplexTerm 
{
    
    
    public ListTerm(Term frs,Term snd)
    {
      frs_=frs;
      snd_=snd;
      emptyFv_=(frs.emptyFv() && snd.emptyFv());
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
    
    public int getArity() {
        return 2;
    }
    
    public String getName() {
        return TermWareSymbols.CONS_STRING;
    }
    
    public Object getNameIndex()
    {
        return TermWareSymbols.CONS_INDEX;
    }
    
    
    public Term getSubtermAt(int i) {
        switch(i) {
            case 0: return frs_;
            case 1: return snd_;
            default:
                throw new TermIndexOutOfBoundsException(this,i);
        }
    }
    
    public void setSubtermAt(int i, Term t) {
        switch(i) {
            case 0: frs_=t;
                    break;
            case 1: snd_=t;
                    break;
            default:
                throw new TermIndexOutOfBoundsException(this,i);
        }
        emptyFv_=(frs_.emptyFv() && snd_.emptyFv());        
    }
    
    public Term termClone() throws TermWareException
    {
        return new ListTerm(frs_.termClone(),snd_.termClone());
    }
    
    public  PartialOrderingResult concreteOrder(Term x, Substitution s) throws TermWareException
    {
       if (x.isX()) {
           Term xt = s.get(x.minFv());
           if (xt!=null) {
               return concreteOrder(xt,s);
           }else{
               s.put(x,this);
               return PartialOrderingResult.LESS;
           }
       } 
       if (x.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
           PartialOrderingResult r1 = frs_.concreteOrder(x.getSubtermAt(0),s);
           switch(r1) {
               case LESS:
                   PartialOrderingResult r2 = snd_.concreteOrder(x.getSubtermAt(1),s);
                   switch(r2) {
                       case LESS:
                       case EQ:
                           return PartialOrderingResult.LESS;
                       case MORE:
                           return PartialOrderingResult.EQ;
                       default:
                           return PartialOrderingResult.NOT_COMPARABLE;
                   }
               case EQ:
                   return snd_.concreteOrder(x.getSubtermAt(1),s);
               case MORE:
                   r2=snd_.concreteOrder(x.getSubtermAt(1),s);
                   switch(r2) {
                       case LESS:
                           return PartialOrderingResult.EQ;
                       case EQ:
                       case MORE:
                           return PartialOrderingResult.MORE;
                       default:
                           return PartialOrderingResult.NOT_COMPARABLE;
                   }
               default:
                   return PartialOrderingResult.NOT_COMPARABLE;
           }         
       }else{
           return PartialOrderingResult.NOT_COMPARABLE;
       }
    }
  
         
    private Term frs_;
    private Term snd_;

    
}
