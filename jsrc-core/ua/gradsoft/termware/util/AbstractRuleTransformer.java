package ua.gradsoft.termware.util;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002
 * $Id: AbstractRuleTransformer.java,v 1.2 2007-08-04 08:54:44 rssh Exp $
 */

import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;

                           
/*
 * Base class for rewriting rule.
 */
public abstract class AbstractRuleTransformer implements ITermTransformer
{

 public  abstract Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException;

 /**
  * return representation of this rule as term
  */
 public  abstract Term  getTerm() throws TermWareException;

 /**
  * get input pattern
  */
 public  abstract Term getInPattern();

 /**
  * get output pattern
  */
 public  abstract Term getOutPattern();
 
 /**
  *Helper method: check if <code> pattern </code> have form action(x,y).
  */
  protected static boolean isActionPattern(Term pattern)
 {
   return pattern.getArity()==2 && pattern.getNameIndex().equals(TermWareSymbols.ACTION_INDEX);
 }

/**
  *Helper method: check if <code> pattern </code> have form where(x,y).
  */
  protected static boolean isWherePattern(Term pattern)
 {
   return pattern.getArity()==2 && pattern.getNameIndex().equals(TermWareSymbols.WHERE_INDEX);
 }
  
/**
  *Helper method: check if <code> pattern </code> have form let(x,y).
  */
  protected static boolean isLetPattern(Term pattern)
 {
   return pattern.getArity()==2 && pattern.getNameIndex().equals(TermWareSymbols.LET_INDEX);
 }

  
}
 