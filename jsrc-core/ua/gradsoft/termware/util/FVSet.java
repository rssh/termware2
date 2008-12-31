package ua.gradsoft.termware.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.XTerm;
import ua.gradsoft.termware.exceptions.FVLimitReachedException;
import ua.gradsoft.termware.exceptions.IncorrectTermException;

/*
 * part of TermWare.
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA>, 2002-2008
 * (C) Grad-Soft Ltd, Kiev, Ukraine, 2002-2008
 * $Id: FVSet.java,v 1.4 2008-01-13 01:09:35 rssh Exp $
 */




/**
 * set of free variables.
 **/
public final class FVSet
{

 public FVSet(Term term)  throws IncorrectTermException 
 { 
  minX_=-1;
  maxX_=-1;
  v_ = null;
  if (!term.emptyFv()) {
      addSubterm(term);
  }
 }

 public boolean empty()
   { return v_==null || v_.isEmpty(); }

 public int getMin()  
   { return minX_; }

 public int getMax()
   { return maxX_; }

 
 public void shift(int newMin)  throws FVLimitReachedException,
                                       TermWareException
   { 
       if (minX_==-1) return;
       int delta = newMin - minX_;
       if (v_!=null) {
         for(int i=0; i<v_.size(); ++i) {
             
           //XTerm x = (XTerm)v_.get(i);
           Term x = v_.get(i);  
           int newXMin=x.minFv()+delta;
           if (newXMin < 0) { // compensate
             for(int j=i-1; j>=0; --j) {
               Term y = v_.get(j);
               y.shiftFv(y.minFv()-delta);
             }
             throw new FVLimitReachedException();
           }
           x.shiftFv(newXMin);
         }
       }
       minX_ = newMin;
       maxX_ += delta;
     
   }

 public void normalize(int newMin) throws TermWareException
  {
   if (v_==null) return;
   HashMap<XTerm,Integer> indexes = new HashMap<XTerm,Integer>();
   int s=newMin;
   for(int i=0; i<v_.size(); ++i) {
     XTerm x = (XTerm)v_.get(i);
     Integer xIndex = indexes.get(x);
     if (xIndex==null) {
       indexes.put(x,new Integer(s));
       ++s;
     }
   }

   Set se=indexes.entrySet();
   Iterator sei=se.iterator();
   while(sei.hasNext()) {
     Map.Entry entry=(Map.Entry)sei.next();
     XTerm x = (XTerm)entry.getKey();
     Integer i=(Integer)entry.getValue();
     sei.remove();
     x.shiftFv(i.intValue());
   }
   
  }

 private void addSubterm(Term term) 
 {
   if (term.isX()) {
     if (v_==null) v_=new ArrayList<Term>();
     v_.add(term.getTerm());
     int curX = term.getXIndex();
     if (minX_==-1 || minX_ > curX) {
       minX_ = curX;
     }
     if (maxX_==-1 || maxX_ < curX) {
       maxX_ = curX;
     }
   }else if (term.getArity()!=0) {
     if (term.emptyFv()) {
         return;
     }
     for(int i=0; i<term.getArity(); ++i) {
       Term current=term.getSubtermAt(i);
       if (!current.emptyFv()) {
         addSubterm(term.getSubtermAt(i));
       }
     }
   }
 }

 private int minX_;
 private int maxX_;
 private ArrayList<Term> v_;

}