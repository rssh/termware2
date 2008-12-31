package ua.gradsoft.termware;

/*
 * part of TermWare library.
 *(C) Grad-Soft Ltd, Kiev, Ukraine.
 *http://www.gradsoft.kiev.ua
 */

import java.io.*;
import java.util.*;
import ua.gradsoft.termware.exceptions.*;

/**
 * Substitution of propositional variables.
 **/
public class Substitution implements Cloneable
{

 /**
  * constructor
  */
 public Substitution() 
  { v_ = new TreeMap<Integer,Term>(); }

 private Substitution(TreeMap<Integer,Term> v, boolean copy)
 {
   if (copy) {
       v_ = new TreeMap<Integer,Term>();
       v_.putAll(v);
   }else{
       v_=v;
   }
 }
 
 /**
  * put to substitution <code> frs -> snd </code>. One of terms must be propositional variable
  *concretizeOnly means, that  <code> snd </code> must be not propositional variable, but
  *concrete term.
  *@param frs - term to substitute
  *@param snd - term, which we substitute instead snd.
  */
 public void put(Term frs, Term snd) throws TermWareException, MatchingFailure
  { 
     if (frs.isX()) {
         put1(new Integer(frs.getXIndex()),snd);
     } else if (snd.isX()) {
         put1(new Integer(snd.getXIndex()),frs);
     } else {
         /* impossible */
         throw new MatchingFailure(frs,snd);
     }
  }

 
 /**
  * merge current substitution with <code> x </code>
  */
 public void merge(Substitution x) throws TermWareException
 {  
  for(Map.Entry<Integer,Term> entry : x.v_.entrySet()) {  
    put1(entry.getKey(),entry.getValue());
  }     
 }

 
 /**
  * return substitution of propositional variable with index <code> i </code>
  *If such variable does not exists - return null
  */
  public Term get(int s)
  {
     return (Term)v_.get(new Integer(s)); 
  }
  
  /**
   * return true if substitution is empty.
   */
  public  boolean isEmpty()
  {
     return v_.isEmpty(); 
  }

  /**
   * print substitution to <code> out </code>
   **/
  public  void print(PrintStream out) 
  {
     PrintWriter writer = new PrintWriter(out);
     print(writer);
     writer.flush();
  }
  
  /**
   * print substitution to <code> out </code>
   **/
  public  void print(PrintWriter out) 
  {
     out.print("[");
     for(Map.Entry<Integer,Term> e: v_.entrySet()) {       
       out.print("(");
       out.print(e.getKey());
       out.print(":");
       e.getValue().print(out);
       out.print(")");
     }
     out.print("]");
  }  
  
  
  @Override
  public String toString()
  {
     StringWriter swr = new StringWriter();
     PrintWriter pwr = new PrintWriter(swr);
     print(pwr);
     pwr.flush();
     return swr.toString();     
  }

  public void clear()
  {
   v_.clear();
  }  
  
  @Override
  public Object clone()
  {
    return new Substitution(v_,true);  
  }
  
  public Substitution cloneSubstitution()
  {
    return (Substitution)clone();  
  }

  private void put1(Integer frsXindex,Term snd) throws TermWareException, MatchingFailure
  {        
     if (v_.containsKey(frsXindex)) {
        Term oldSnd = (Term)v_.get(frsXindex);
        Substitution s = new Substitution();
        if (!snd.boundUnify(oldSnd,s)) {
            throw new MatchingFailure(snd,oldSnd);
        }
        Term unifiedSnd=snd.subst(s);
        put2(frsXindex,unifiedSnd);
     }else{
        put2(frsXindex,snd);
     }
  }

  private void put2(Integer frsIndex,Term snd) {
      v_.put(frsIndex,snd);
  }
     
  private TreeMap<Integer,Term> v_;

}
