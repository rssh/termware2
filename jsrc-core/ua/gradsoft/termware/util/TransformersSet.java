package ua.gradsoft.termware.util;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2007
 * $Id: TransformersSet.java,v 1.7 2007-07-13 20:50:22 rssh Exp $
 */

import java.io.*;
import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;


                           
/**
 * Set of transformers, ordered by:
 *<li>
 * <ul> concrete order of pattern, for rule-based transformers. </ul>
 * <ul> unordered for built-in transformes </ul>
 *</li>
 **/
public class TransformersSet implements Iterable  
{

 public TransformersSet()
 {
  transformers_=new ArrayList<ITermTransformer>();
 }

 /**
  * add transformer to set
  */
 public  void  add(ITermTransformer transformer) throws TermWareException
 {
   int size = transformers_.size();
   AbstractRuleTransformer ruleTransformer = null;
   Term inPattern = null;
   if (transformer instanceof AbstractRuleTransformer) {
       ruleTransformer = (AbstractRuleTransformer)transformer;
       inPattern = ruleTransformer.getInPattern();
   }
   
    
   boolean inserted=false;
   for(int i=0; i<size && !inserted; ++i) {
       ITermTransformer current = transformers_.get(i);
       if (current instanceof AbstractRuleTransformer) {
          if (ruleTransformer!=null) {               
            AbstractRuleTransformer crt = (AbstractRuleTransformer)current;            
            Substitution s = new Substitution();
            Term currentInPattern = crt.getInPattern().termClone();
            if (!inPattern.emptyFv()||!currentInPattern.emptyFv()) {
              if (currentInPattern.minFv() <= inPattern.maxFv()) {
               try {
                  currentInPattern.shiftFv(inPattern.maxFv()+1);
                }catch(FVLimitReachedException ex){
                  inPattern.shiftFv(1);
                  currentInPattern.shiftFv(inPattern.maxFv()+1);
                }           
              }
            }                        
            PartialOrderingResult ordering = inPattern.concreteOrder(currentInPattern,s);                
            switch(ordering){
                case EQ:            
                case LESS:
                    // insert before current
                    transformers_.add(i,transformer);
                    inserted=true;
                    break;
                default:
                    // do nothing.
                    break;                                        
            }
          }else{
              // this is build-in transformer, so it must be on the end.
              // do nothing.
          } 
       }else{
           // we come to place, where only build-in transformers.
           if (ruleTransformer!=null) {
               transformers_.add(i,transformer);
               inserted=true;
           }else{
               // do nothing up to the end.   
           }          
       }
   }
   if (!inserted) {
       // put at the end.
      transformers_.add(transformer);
   }
 }
 
 /**
  * remove transformer from set
  */
 public  void  remove(ITermTransformer transformer)
 {
  transformers_.remove(transformer);
 }

 
 
  /**
  *return iterator throught transformers
  */
 public  Iterator<ITermTransformer> iterator()
  { return transformers_.iterator(); }

 
 
 /**
  * println name and descriptions of transformers to <code> out </code>
  */
 public  void printHelp(PrintStream out)
 { 
     Iterator it=transformers_.iterator();
     while(it.hasNext()) {
         ITermTransformer tr=(ITermTransformer)it.next();
         out.println(tr.getName());
         out.println(tr.getDescription());
     }
 }
 
 /**
  * print names of transformers to <code> out </code>
  */
 /*
 public void dump(PrintStream out)
 {
     Iterator it=transformers_.iterator();
     boolean frs=true;
     while(it.hasNext()) {
         ITermTransformer tr=(ITermTransformer)it.next();
         if (!frs) {
             out.print(",");
         }else{
             frs=false;
         }
         out.print(tr.getName());    
     }
     out.println();
 }
  */
 
 private ArrayList<ITermTransformer>  transformers_;

}
 