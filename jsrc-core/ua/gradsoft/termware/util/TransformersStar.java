package ua.gradsoft.termware.util;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002
 * $Id: TransformersStar.java,v 1.8 2007-07-13 20:50:22 rssh Exp $
 */

import java.util.*;
import java.io.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;


                           
/**
 * this is map<String,TransformersSet>, where key is pattern-name for
 * transformers.
 **/
public class TransformersStar implements Iterable<ITermTransformer> 
{

 /**
  * constructor
  **/
 public TransformersStar()
 {          
  sets_=new HashMap<String,TransformersSet>();
  defaultTransformers_ = new TransformersSet();
  hasOtherwise_ = false;
 }

 /**
  * add transformer for name <code> patterName </code>
  *
  **/
 public  void  add(String patternName, ITermTransformer transformer) throws TermWareException
 {
  getOrCreateSet(patternName).add(transformer);
  if (patternName.equals("OTHERWISE")) {
      hasOtherwise_=false;
      if (transformer instanceof RuleTransformer) {
          RuleTransformer rl=(RuleTransformer)transformer;
          if (rl.getInPattern().isComplexTerm()) {
              if (rl.getInPattern().getArity()==1) {
                  Term x = rl.getInPattern().getSubtermAt(0);
                  if (x.isX()) {
                      hasOtherwise_=true;
                  }
              }
          }
      }
      if (!hasOtherwise_) {
          throw new AssertException("OTHERWISE entry must be in form OTHERWISE($x)->something");
      }
  }
 }

  /**
  * add transformer for default normalizers set.
  **/
 public  void  addDefault(ITermTransformer transformer) throws TermWareException
 {
  defaultTransformers_.add(transformer);
 } 

 /**
  * add rule <code> rule </code>.
  */
 public  void  addRule(Term rule) throws TermWareException
 {
  AbstractRuleTransformer ruleTransformer=null;
  if (rule.getName().equals("rule")) {
    ruleTransformer = new RuleTransformer(rule);
  }else if(rule.getName().equals("if_rule")) {
    ruleTransformer = new ConditionalRuleTransformer(rule);
  }else {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    rule.print(new PrintStream(out));
    throw new AssertException("Invalid rule term : "+out.toString());
  }
  
  String name = ruleTransformer.getInPattern().getPatternName();
  add(name,ruleTransformer);
 }

 
 ///**
 // * return iterator of transformers for this pattern name.
 // **/
 //public  TransformerIterator iterator1(String patternName )
 // {
 //  TransformersSet set=getSet(patternName);
 //  if (set==null) return new EmptyTransformerIterator();
 //  else return set.iterator1();
 // }

 /**
  * return iterator of transformers for this pattern name.
  **/
 public  Iterator<ITermTransformer> iterator(String patternName )
  {   
   TransformersSet set=getSet(patternName);   
   if (set==null) {
       return EMPTYITERATTOR_;
   } else {       
       return set.iterator();
   }
  }

 
// public  TransformerIterator iterator1()
// {
//   return new SetTransformerIterator(sets_);  
// }

 public  Iterator<ITermTransformer> iterator()
 {
   return new TransformersStarIterator(sets_);  
 }
 


// public  TransformerIterator defaultIterator()
//  {
//   return defaultTransformers_.iterator();
//  }

 /**
  * print help, embedded into rules to <code> out </code>
  */
 public   void   printHelp(PrintStream out)
 {
  Iterator it=sets_.entrySet().iterator();
  while(it.hasNext()) {
      Map.Entry entry = (Map.Entry)it.next();
      out.print(entry.getKey());
      out.print(":");
      TransformersSet s=(TransformersSet)entry.getValue();
      s.printHelp(out);
      out.println();
  }
 }
 
 /**
  * get collection of all rule patterns.
  *@return Collection, where each element is a String (name of pattern).
  */
 public SortedSet<String> getNamePatterns()
 {
   TreeSet<String> retval=new TreeSet<String>();
   retval.addAll(sets_.keySet());
   return retval;
 }
 
 
 public   boolean hasOtherwise()
 {
  return hasOtherwise_;   
 }
 
 /**
  * get or create set of transformers for pattern <code> patternName </code>
  */
 private  TransformersSet  getOrCreateSet(String patternName)
 { 
  Object o = sets_.get(patternName);
  TransformersSet set = (TransformersSet)o;
  if (o==null) {
    set = new TransformersSet();         
    sets_.put(patternName,set);
  }
  return set;
 }

 private  TransformersSet  getSet(String patternName)
 { 
  Object o = sets_.get(patternName);
  return (TransformersSet)o;
 }

 private boolean  hasOtherwise_;
 private HashMap<String,TransformersSet>  sets_;                 
 private TransformersSet defaultTransformers_;
 private TermWareInstance instance_;

 
 private static final Iterator<ITermTransformer> EMPTYITERATTOR_=new EmptyTransformerIterator();
 
}
 