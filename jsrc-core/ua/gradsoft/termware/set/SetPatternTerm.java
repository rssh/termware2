package ua.gradsoft.termware.set;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2004
 * $Id: SetPatternTerm.java,v 1.7 2007-08-04 08:54:44 rssh Exp $
 */

import java.io.*;
import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.util.*;

                           
/**
 * Set pattern: term for expression  { $x: $Y }.
 * unify ({ frs, snd }, S) is succesfull if
 *  frs is unified with some element of S, and snd
 *  is unified with rest of set.
 **/
public class SetPatternTerm extends AbstractComplexTerm
{

    
 private SetPatternTerm(Term t1, Term t2) 
   {
    frs_=t1;
    snd_=t2;
   }

 /**
  * return new set pattern.
  *   frs_ - pattern
  *   snd_ - rest of set.
  **/
 public static Term createSetPattern(Term frs, Term snd) 
                                          throws TermWareException
 {
   //System.err.println("create set_patter:"+frs.getName()+","+snd.getName());  
   if (frs.minFv()==-1 && snd.minFv()==-1) {
     if (snd.isComplexTerm() && snd.getNameIndex().equals(TermWareSymbols.SET_INDEX)) {
       Term[] ra = new Term[snd.getArity()+1];
       for(int i=0; i<snd.getArity(); ++i) {
         ra[i]=snd.getSubtermAt(i).getTerm();
       }
       ra[snd.getArity()]=frs;
       return new SetTerm(ra);
     }
   } 
   return new SetPatternTerm(frs,snd);
 }


 public final String   getName()  
   { return TermWareSymbols.SET_PATTERN_STRING; }
 
 public final Object   getNameIndex()
 { return TermWareSymbols.SET_PATTERN_INDEX; }

 public final String   getPatternName()
   { return TermWareSymbols.SET_STRING; }

 public final Object   getPatternNameIndex()
 { return TermWareSymbols.SET_INDEX; }


 public final int      getArity()
   { return 2; }

 public Term    getSubtermAt(int i) 
   { if (i==0) return frs_; 
     else if (i==1) return snd_;
     else throw new UnsupportedOperationException();
   }

 public void   setSubtermAt(int i, Term t) 
   { if (i == 0) frs_=t;
     else if (i == 1) snd_=t; 
     else throw new TermIndexOutOfBoundsException(this,i);
   }
 
 public boolean boundUnify(Term t, Substitution s) throws TermWareException 
 {
   if(t.isNil()) return false;
   else if(PrimaryTypes.isPrimitive(t.getPrimaryType0())) return false;
   else if(t.isX()) {
     s.put(t,this);
     return true;
   }else {
     if (t.getPatternNameIndex().equals(TermWareSymbols.SET_INDEX)) {
       if (t.getArity()==0) return false;
       t=t.getTerm();
       AbstractSetTerm t1=null;
       if (t instanceof AttributedTerm) {
          t=((AttributedTerm)t).unAttribute();
       }
       if (t instanceof AbstractSetTerm) {
         t1=(AbstractSetTerm)t;
       }
       if (frs_.isX() && (snd_.isX() || snd_.getNameIndex().equals(TermWareSymbols.SET_INDEX)) ) {
          if (!frs_.boundUnify(t.getSubtermAt(0), s)) return false;
          AbstractSetTerm sndCandidate;
          if (t1!=null) {
              sndCandidate=new SetTermWithoutElement((AbstractSetTerm)t1, 0);
          }else{
              // impossible ?
              int newArity=t.getArity()-1;
              Term[] sndBody=new Term[newArity];
              for(int i=0; i<newArity; ++i) {
                sndBody[i]=t.getSubtermAt(i+1);
              }
              sndCandidate=new SetTerm(sndBody);
          }
          
          if (!snd_.boundUnify(sndCandidate, s)) return false;
          return true;
       }else{
          if (t1!=null) {
            int ui=0;   
            try {
              ui=t1.findBoundUnifyIndex(frs_,s);
            }catch(MatchingFailure ex){
              return false;
            }
            //
            //int newArity=t1.getArity()-1;
            //ITerm[] sndBody=new ITerm[newArity];
            //for(int i=0; i<t1.getArity(); ++i){
            //  if (i<ui) sndBody[i]=t.getSubtermAt(i);
            //  if (i==ui) continue;
            //  if (i>ui) sndBody[i-1]=t.getSubtermAt(i); 
            //}
            //SetTerm sndCandidate=new SetTerm(sndBody);
            //
            SetTermWithoutElement sndCandidate=new SetTermWithoutElement(t1, ui);
            
            if (!snd_.boundUnify(sndCandidate, s)) return false;
            return true;
          }else if(t.getNameIndex().equals(TermWareSymbols.SET_PATTERN_INDEX)) {
            SetPatternTerm t11=((SetPatternTerm)t.getTerm());
            if (t11==null) {
              return false;
            }
            if (!frs_.boundUnify(t11.frs_,s)) {
                return false;
            }
            if (!snd_.boundUnify(t11.snd_,s)) {
                return false;
            }
            return true;
         }
       }
     }
   }
   return false;     
 }
 
 


    

 /**
  * apply substitution <code> s </code> to current term.
  *@return true, if substitution change term, otherwise false
  **/
 public boolean    substInside(Substitution s) throws TermWareException
 {
  boolean fr = frs_.substInside(s);
  boolean sr = snd_.substInside(s);
  return fr||sr;
 }
  


 public  Term subst(Substitution s) throws TermWareException
 {
   Term newFrs=frs_.subst(s);
   Term newSnd=snd_.subst(s);
   return createSetPattern(newFrs,newSnd);
 }


  /**
   * Equality when all propositional variables are equal
   **/
 public boolean  freeEquals(Term t) throws TermWareException
 {
  if (t.getArity()!=2 || !t.getNameIndex().equals(getNameIndex())) 
    return false;
  if (!frs_.freeEquals(t.getSubtermAt(0))) return false;
  if (!snd_.freeEquals(t.getSubtermAt(1))) return false;
  return true;  
 }


 public boolean boundEquals(Term t) throws TermWareException
 {
  if (t.getArity()!=2 || !t.getNameIndex().equals(getNameIndex())) 
    return false;
  if (!frs_.boundEquals(t.getSubtermAt(0))) return false;
  if (!snd_.boundEquals(t.getSubtermAt(1))) return false;
  return true;  
 }

 public PartialOrderingResult concreteOrder(Term x, Substitution s) throws TermWareException
 {
     if (x.isX()) {
         Term xt = s.get(x.minFv());
         if (xt!=null) {
             return concreteOrder(xt,s);
         }else{
             s.put(x,this);
             return PartialOrderingResult.LESS;
         }
     }else if (!x.isComplexTerm()) {
         return PartialOrderingResult.NOT_COMPARABLE;
     }else if (x.getNameIndex().equals(TermWareSymbols.SET_PATTERN_INDEX)) {
         Term tfrs = x.getSubtermAt(0);
         Term tsnd = x.getSubtermAt(1);
         PartialOrderingResult r1 = frs_.concreteOrder(tfrs,s);
         PartialOrderingResult r2 = null;
         PartialOrderingResult retval = null;
         switch(r1) {
             case LESS:
                r2 = snd_.concreteOrder(tsnd,s);
                switch(r2) {
                    case LESS:
                    case EQ:
                        retval = PartialOrderingResult.LESS;
                        break;
                    default:
                        retval= PartialOrderingResult.NOT_COMPARABLE;
                        break;
                }           
                break;
             case MORE:                 
                 r2 = snd_.concreteOrder(tsnd,s);
                 switch(r2) {
                     case MORE:
                     case EQ:
                         retval=PartialOrderingResult.MORE;
                         break;
                     default:
                         retval=PartialOrderingResult.NOT_COMPARABLE;
                         break;
                 }            
                 break;
             case EQ:
                 retval=snd_.concreteOrder(tsnd,s);             
                 break;
             default:
                 retval=PartialOrderingResult.NOT_COMPARABLE;
         }
         return retval;
     }else if(x.getNameIndex().equals(TermWareSymbols.SET_INDEX)) {
         if (frs_.isX()) {
             if (snd_.isX()) {
                 return PartialOrderingResult.MORE;
             }else if (snd_.isNil()){
                 if (x.getArity()==1) {
                     return PartialOrderingResult.MORE;
                 }else{
                     return PartialOrderingResult.NOT_COMPARABLE;
                 }
             }else if (x.getTerm() instanceof AbstractSetTerm ) {
                 AbstractSetTerm xx=(AbstractSetTerm)x.getTerm();
                 for(int i=0; i<xx.getArity(); ++i) {
                     Term ei = new SetTermWithoutElement(xx,i);
                     PartialOrderingResult r = snd_.concreteOrder(ei,s);
                     switch(r) {
                         case MORE:
                         case EQ:
                             return PartialOrderingResult.MORE;
                         default:
                             break;
                     }
                 }
                 return PartialOrderingResult.NOT_COMPARABLE;
             }else{
                 // impossible, but ..
                 // So, let's recreate term 
                 return concreteOrder(SetTerm.recreateWithBodyAs(x),s);        
             }    
         }else{
             for(int i=0; i<x.getArity(); ++i) {
                 PartialOrderingResult r1 = frs_.concreteOrder(x.getSubtermAt(i),s);
                 switch(r1) {
                     case LESS:
                       PartialOrderingResult r2 = snd_.concreteOrder(AbstractSetTerm.createWithBodyExclude(x,i),s);
                       switch(r2) {
                           case LESS:
                           case EQ:    
                               return PartialOrderingResult.LESS;
                           case MORE:
                               return PartialOrderingResult.EQ;
                           default:
                               break;
                       }
                       break;
                     case EQ:
                         r2=snd_.concreteOrder(AbstractSetTerm.createWithBodyExclude(x,i),s);
                         switch(r2) {
                             case LESS:
                             case EQ:
                             case MORE:
                                 return r2;
                             default:
                                 break;
                         }
                         break;
                     case MORE:
                         r2 = snd_.concreteOrder(AbstractSetTerm.createWithBodyExclude(x,i),s);
                         switch(r2) {
                             case LESS:
                                 return PartialOrderingResult.EQ;
                             case EQ:
                             case MORE:
                                 return PartialOrderingResult.MORE;
                             default:
                                 break;
                         } 
                         break;
                     default:  // PartialOrderingResult.NOT_COMPARABLE
                         break;
                 }
             }
             return PartialOrderingResult.NOT_COMPARABLE;
         }
     }else{
         return PartialOrderingResult.NOT_COMPARABLE;
     }
 }
 

 public Term termClone()  throws TermWareException
 {
  return new SetPatternTerm(frs_.termClone(),snd_.termClone());
 }

 public boolean  emptyFv() 
 {
   return frs_.emptyFv() && snd_.emptyFv(); 
 }

 public int      minFv() throws TermWareException
  { if (frs_.minFv()==-1) return snd_.minFv(); 
    if (snd_.minFv()==-1) return frs_.minFv();
    if (frs_.minFv() < snd_.minFv()) return frs_.minFv();
    return snd_.minFv();
  }

 public int      maxFv()  throws TermWareException
  { if (frs_.maxFv()>snd_.maxFv()) return frs_.maxFv(); 
    return snd_.maxFv();
  }

 public void     shiftFv(int newMin)  throws TermWareException
  { if (frs_.minFv()==-1) snd_.shiftFv(newMin);
    else if (snd_.minFv()==-1) frs_.shiftFv(newMin);
    else { 
      FVSet fvSet=new FVSet(this);
      frs_.shiftFv(newMin);
    }
  }

 public   void print(PrintStream out) 
 {
  out.print("{");
  frs_.print(out);
  out.print(":");
  snd_.print(out);
  out.print("}");
 }


 
 public  Term createSame(Term[] newBody) throws TermWareException
 {
  if (newBody.length==2) {
    return createSetPattern(newBody[0],newBody[1]);
  }else{
    throw new AssertException("SetPattern.createSame with array of arity!=2");
  }
 }

 
//
// Internal variables.
//

 private  Term frs_;
 private  Term snd_;

}
