/*
 * TransformationContext.java
 *part of TermWare 2.
 *(C) Grad-Soft Ltd, Kiev, Ukraine
 *http://www.gradsoft.ua
 *$Id: TransformationContext.java,v 1.4 2007-07-13 20:50:17 rssh Exp $
 */

package ua.gradsoft.termware;

import ua.gradsoft.termware.envs.*;

/**
 * Context of term transformation.
 */
public class TransformationContext {
    
      /** Creates a new instance of TransformationContext */
      public TransformationContext() {
      }
    
     /**
      * return substitutions for currently-evaluated term. <br>
      * Used for manipulation with substitutions of current rule inside
      *  actions (check and set methods).
      *example:
      *<pre>
      *class MyFacts extends DefaultFacts {
      *    ..
      *   void setPropositionalVariabel(TransformationContext ctx, Term t,int x) throws TermWareException
      *   {
      *    if (!t.isX) {
      *        throw new AssertException("setPropositionalVariable must receive propositional variable as argument");
      *    }
      *    Substitution s=ctx.getCurrentSubstitution();
      *      if (s!=null) s.put(t,ITermFactory.createInt(x));
      *   }
      *    
      *</pre>
      *in multi-threading environment can refer to thread-local storage.
      **/
      public  Substitution getCurrentSubstitution()
      {
          if (currentSubstitution_==null) currentSubstitution_=new Substitution();
          return currentSubstitution_;
      }
 
      
      public Substitution newCurrentSubstitution()
      {
        if (currentSubstitution_==null) {
            currentSubstitution_=new Substitution();
        } else {
            currentSubstitution_.clear();
        }
        return currentSubstitution_;
      }
      
     /**
      * set current substitution to <s> and return previously setted substitution,
      *  if such exists. (otherwise - null)  Used by RuleEngine.
      *<br>
      */
   //   public  Substitution swapCurrentSubstitution(Substitution s)
   //   {
   //      Substitution prev=currentSubstitution_;
   //      currentSubstitution_=s;
   //      return prev;         
   //   }
 
 
     /**
      * get stop flag for currently-evaluated term.
      *  (strategy must stop evaluation of this term after setting stop flag to true)
      */
      public  boolean isStop()
      { return stop_; }
 
      /**
       * set current stop flag 
       */
      public  void    setStop(boolean stop)
      { stop_=stop; }

      
      public boolean       isChanged()
      { return changed_; }
      
      public  void         setChanged(boolean changed)
      { changed_=changed; }
      
      
      
      /**
       * return environment for writing output and error messages.
       */
      public  IEnv         getEnv()
      { 
          if (env_==null) env_=new SystemEnv();
          return env_; 
      }
      
      /**
       * set environment
       */
      public  void         setEnv(IEnv env)
      { env_=env; }
      
      
      
      private Substitution currentSubstitution_=null;
      private boolean      stop_=false;
      private boolean      changed_=false;
      private IEnv         env_=null;
    
}
