package ua.gradsoft.termware;


/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2008
 * $Id: IFacts.java,v 1.3 2008-03-24 22:33:08 rssh Exp $
 */


import java.util.Collection;

/**
 * Interface for facts database, called from rules.
 ***/                           
public interface IFacts
{     
                       

 /**
  * check fact (called from conditions in left part of rules)
  */
 public boolean  check(Term t,  TransformationContext ctx) throws TermWareException;


 /**
  * set fact (called from actions in right part of rules)
  **/
 public void     set(Term t, TransformationContext ctx) throws TermWareException;

 
 /**
  * get logging mode
  */
 public boolean  isLoggingMode();
 
 /**
  * enable/disable logging
  */
 public void     setLoggingMode(boolean mode);
 
 
 /**
  * set entity to log. (one of "All", "Facts" and so on)
  */
 public void     setLoggedEntity(String s);
 
 /**
  * unset entity to log.
  */
 public void   unsetLoggedEntity(String s);

 /**
  * set collection of entities to log
  **/  
  public void  setLoggedEntities(Collection<String> entities);
     
  /**
   * clear entities to log
   **/
  public void clearLoggedEntities();
 
}
