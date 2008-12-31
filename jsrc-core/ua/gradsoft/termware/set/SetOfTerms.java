package ua.gradsoft.termware.set;

import java.util.ArrayList;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.SubtermNotFoundException;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2007
 * $Id: SetOfTerms.java,v 1.5 2007-05-16 12:04:22 rssh Exp $
 */


                           

/**
 * Sorted set of terms.
 **/
public class SetOfTerms       
{

 public SetOfTerms()
 { v_=new ArrayList<Term>(); }

 public int    getSize()
 {
   return v_.size();
 }

 public Term  getAt(int i)
 {
   return v_.get(i);
 }

 public int  index(Term t) throws SubtermNotFoundException
 {
   return indexInRange(t,0,getSize());
 }

 public void insert(Term t)
 {
   insertInRange(t,0,getSize());
 }

 public void remove(Term t) 
 {
  int x=0;
  try {                 
    x=index(t);
  }catch(SubtermNotFoundException ex){
    return;
  }
  remove(x);
 }


 void remove(int index) 
 {
  v_.remove(index);
 }


 void  insert(Term[] terms)  
 {
  for(int i=0; i<terms.length; ++i) {
   insert(terms[i]);
  }
 }

 void  insert(SetOfTerms terms)
 {
  for(int i=0; i<terms.getSize(); ++i) {
   insert(terms.getAt(i));
  }
 }

 /**
  *precondition: assumed, that we t is greater, than any of terms in v_.
  **/
 public void  addAlreadySorted(Term t)
 {
   v_.add(t);
 }

 public final boolean isEmpty() 
 {
   return v_.isEmpty();  
 }
 
 
 private void insertInRange(Term t, int l, int h) 
 {
  if (l==h) {
    v_.add(l,t);
  }else{
    int m=(l+h)/2;
    int x = ((Term)(v_.get(m))).termCompare(t);
    if (x==0) return;
    if (x<0) {
      insertInRange(t,l,m);
    }else{
      insertInRange(t,m+1,h);
    }
  }
 }


 private int indexInRange(Term t, int l, int h)  throws SubtermNotFoundException
 {
  if (l==h) {
    int x = ((Term)(v_.get(l))).termCompare(t);
    if (x==0) return l;
    throw new SubtermNotFoundException(t,this);
  }else{
    int m=(l+h)/2;
    int x = ((Term)(v_.get(m))).termCompare(t);
    if (x==0) return x;
    if (x<0) {
      return indexInRange(t,l,m);
    }else{
      return indexInRange(t,m+1,h);
    }                          
  }
 }


 private  ArrayList<Term>  v_;

}
