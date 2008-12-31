package ua.gradsoft.termware;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2004
 * $Id: ITermRewritingStrategy.java,v 1.1.1.1 2004-12-11 19:41:27 rssh Exp $
 */

import java.util.*;

import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.util.*;

/**
 * general interface for rewriting strategy
 **/                           
public interface ITermRewritingStrategy extends ITermTransformer
{

 /**
  * transform t
  *@param t - term to transform
  *@param system - system which we use
  *@param ctx - context of transformation
  **/   
 public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException;

 /**
  *get transformer star
  *@return star of transformers
  */
 public  TransformersStar  getStar();

 
 /**
  *if current ruleset contains OTHERWISE($x) entry ?
  *return true if current ruleset contains rule OTHERWISE($x) -> something
  */
 public  boolean           hasOtherwise();

}
                                  