package ua.gradsoft.termware;

/*
 * (C) Ruslan Shevchenko  <Ruslan@Shevchenko.Kiev.UA> 2002,2003, 2004
 * $Id: ITermTransformer.java,v 1.2 2006-07-13 20:38:41 rssh Exp $
 */

import java.util.*;
import java.io.*;

import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.util.*;

                           
/**
 * interface for build-in operations or rules.
 * Build-in operation is represented as class, which implement ITermTransformer.
 * Rules are instances of RuleTransformer
 **/
public interface ITermTransformer
{
 
 ///**
 // * true, if term to transform by this transformer must be
 // *at first transformed by strategy.
 // *Usefull, for build-in transformers.
 // *used by strategies.
 // */   
 //public boolean internalsAtFirst();
    
 /**
  * attempt to transform term <code> t </code>.
  *@param t - term to transform
  *@param system - system which give us access to ebvironment.
  *@param ctx - context of transformation.
  *@return - transformed term.
  **/   
 public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException;

 /**
  *return name of transformer. (usually  is a name of top-level reduced term).
  *this name is not used internally by termware engine, but can be printed in
  *system description for human.
  *@return name of transformer.  
  **/
 public  String getName();
 
 /**
  * return human-readable description of transformer
  **/
 public  String getDescription();
 
 /**
  * return source-code of this term, if one is aviable.
  **/
 public  String getSource();
 
}
 