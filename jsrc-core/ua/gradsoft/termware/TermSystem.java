package ua.gradsoft.termware;


/*
 * (C) Ruslan Shevchenko, 2002 - 2008
 * (C) Grad-Soft Ltd, Kiev, Ukraine 2002 - 2008
 */

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import ua.gradsoft.termware.debug.JavaCompiledFileAndLine;
import ua.gradsoft.termware.debug.SourceCodeLocation;



/**
 * TermSystem is set <code> &lt; rules,facts,strategy,env &gt; </code> where
 *<ul>
 * <li> rules - set of rewriting rules.</li>
 * <li> strategy - strategy for applying of this rules. </li>
 * <li> facts - deductive database of facts. </li>
 * <li> env - environment for input/output. </li>
 *</ul>
 *The main sence of term system -  reduce terms with help of given rules and
 *in given environment by some strategy.
 *@see IFacts
 *@see ITermRewritingStrategy
 *@see IEnv
 **/
public class TermSystem
{

  /**
   * create new instance of ITermSystem
   *@param strategy  - strategy
   *@param facts  - database of facts
   *@param instance    - instance of TermWare environment.
   **/  
   public TermSystem(ITermRewritingStrategy strategy, IFacts facts, TermWareInstance instance)
  {
   strategy_=strategy;
   facts_=facts;
   reduceFacts_=true;
   env_=instance.getEnv();
   instance_=instance;
   loggedEntities_=new HashSet<String>();
   loggingMode_=false;
  }

/**
   * create new instance of ITermSystem in default TermWare environment.
   *@param strategy  - strategy
   *@param facts  - database of facts
   **/  
   public TermSystem(ITermRewritingStrategy strategy, IFacts facts)
   {
    this(strategy,facts,TermWare.getInstance());   
   }
   

  /**
   * apply system to term x.
   *   (try to reduce, on error return NIL)
   *@param x  term to reduce
   *@return reduced term, or nil if reduce was failed.
   **/
  public Term apply(Term x)
  {
   //this.getEnv().getLog().println("TermSystem.apply begin, x="+TermHelper.termToString(x));
   try {
     x=reduce(x);
   }catch(TermWareException ex){
     env_.show(ex);
     x=TermFactory.createNIL();
   }
   //this.getEnv().getLog().println("TermSystem.apply end, x="+TermHelper.termToString(x));
   return x;
  }

  /**
   * synonim of <code> appply(ITermFactory.createParsedTerm(x)) </code>
   *@param x  string, with term to reduce.
   *@return reduced Term, or NIL if parsing was failed.
   **/
  public final Term apply(String x)
  {
   try {
    return reduce(instance_.getTermFactory().createParsedTerm(x,JavaCompiledFileAndLine.deduceFileAndLine(3)));
   }catch(TermWareException ex){
     env_.show(ex);
     return TermFactory.createNIL();
   }
  }

  /**
   * synonim of <code> appply(ITermFactory.createParsedTerm(x,scl)) </code>
   *@param x  string, with term to reduce.
   *@param scl source code location of x.
   *@return reduced Term, or NIL if parsing was failed.
   **/
  public final Term apply(String x,SourceCodeLocation scl)
  {
   try {
    return reduce(instance_.getTermFactory().createParsedTerm(x,scl));
   }catch(TermWareException ex){
     env_.show(ex);
     return TermFactory.createNIL();
   }
  }
  
  
  /**
   * check fact <code> t </code> in associated facts database.
   *(after reducing of t, if ReduceFacts property set to true).
   *@param t  fact to check.
   *@param ctx  transformation context of system.
   *@return true if fact <code> t </code> is checked, false otherwise.
   *@see TermSystem#isReduceFacts
   *@see TermSystem#setReduceFacts
   *@see TermSystem#setFact
   **/
  public boolean checkFact(Term t,TransformationContext ctx) throws TermWareException
  {
    if (isLoggingMode()) {
       if (checkLoggedEntity("All")||checkLoggedEntity(getClass().getName())) {
         getEnv().getLog().print("checkFact, t=");
         t.print(getEnv().getLog());
         getEnv().getLog().println();
       }
    }
    if (reduceFacts_) {
       //Substitution s = ctx.getCurrentSubstitution();
       //ctx.newCurrentSubstitution();
       t=reduce(t,ctx);
    }
    return facts_.check(t,ctx);
  }
   

  /**
   * synonim of <code> checkFact(ITermFactory.createParsedTerm(x)) </code>
   *@param  s  - sring with facts to reduce.
   *@return true if fact is checked.
   **/
  public boolean checkFact(String s) throws TermWareException
  {
    return checkFact(instance_.getTermFactory().createParsedTerm(s,JavaCompiledFileAndLine.deduceFileAndLine(3)),new TransformationContext());
  }


  /**
   * set fact <code> t </code>
   **/
  public void setFact(Term t,TransformationContext ctx) throws TermWareException
  {
    if (reduceFacts_) {
      t=reduce(t,ctx);
    }
    facts_.set(t,ctx);             
  }

  /**
   *set fact <code> s </code>.
   */
  public final void setFact(String s) throws TermWareException
  {
    setFact(instance_.getTermFactory().createParsedTerm(s,JavaCompiledFileAndLine.deduceFileAndLine(3)),new TransformationContext());           
  }

  
  /**
   * true, if facts are reduced by system before passing to facts database
   *in <code> checkFact </code> and <code> setFact </code> methods.
   *@see TermSystem#setReduceFacts
   */
  public boolean isReduceFacts()
  {
      return reduceFacts_;
  }
  
  /**
   * if set to true, then facts are reduced by system before passing to facts database
   *in <code> checkFact </code> and <code> setFact </code> methods.
   *otherwise, facts terms are passed to facts database directly.
   *@see TermSystem#isReduceFacts
   *@see TermSystem#checkFact
   *@see TermSystem#setFact
   */
  public void setReduceFacts(boolean reduceFacts)
  {
      reduceFacts_=reduceFacts;
  }
  
  

  /**
   * add rule <code> t </code> to system with default priority.
   **/
  public void addRule(Term t) throws TermWareException
  {
    strategy_.getStar().addRule(t);
    if (strategy_.hasOtherwise()) {
        reduceFacts_=false;
    }
    //rules_.add(t);
  }

  /**
   * add rule <code> s </code> to system.
   *alias for <code> addRule(ITermFactory.createParsedTerm(s)); </code>
   */
  public final void addRule(String s) throws TermWareException
  {
    addRule(instance_.getTermFactory().createParsedTerm(s,JavaCompiledFileAndLine.deduceFileAndLine(3)));
  }

  /**
   * add normalizer <code> transformer </code> for terms with name <code> termName </code>
   */
   public void addNormalizer(String termName, ITermTransformer transformer) throws TermWareException
  {    
    strategy_.getStar().add(termName, transformer);
  }


  /**
   * reduce term <code> x </code>
   */
  public Term reduce(Term x) throws TermWareException
  {
   TransformationContext ctx = new TransformationContext();
   return reduce(x,ctx);
  }

  /***
   * reduce term in given transformation context.
   *(This method used for shared transformation context between many systems,
   * as implementation of apply)
   */
  public Term reduce(Term x, TransformationContext ctx) throws TermWareException
  {
   boolean otherwise=false;
   boolean changed=false;
   boolean globalChanged=false;
   setStop(false);
   //setLoggingMode(true);
   //setLoggedEntity("All");
   do{
     do{
       ctx.setChanged(false);
       //System.err.println("before strategy.transform, name is "+strategy_.getName());
       x=strategy_.transform(x,this,ctx);  
       //System.err.println("after strategy.transform");
       if (ctx.isChanged()) {
         if (isLoggingMode()) {
            if (checkLoggedEntity("All")||checkLoggedEntity("SystemReductions")||checkLoggedEntity(this.getClass().getName())){
              ctx.getEnv().getLog().print(this.getClass().getName());
              ctx.getEnv().getLog().print(":");
              x.println(ctx.getEnv().getLog());
            }
         }
         changed=true;
         globalChanged=true;
       }       
     }while(ctx.isChanged()&&!ctx.isStop()&&!isStop());
     
     if (!changed && !isStop()) {
       if (strategy_.hasOtherwise()) {
          if (!(x.isComplexTerm() && x.getName().equals("OTHERWISE"))) {
            x=instance_.getTermFactory().createTerm("OTHERWISE", x);
            otherwise=true;
            if (isLoggingMode()) {
                if (checkLoggedEntity("All")||checkLoggedEntity(this.getClass().getName())){
                  ctx.getEnv().getLog().print("create OTHERWISE term:");
                  x.print(ctx.getEnv().getLog());
                  ctx.getEnv().getLog().println();
                  
                  ctx.getEnv().getLog().println("otherwise is "+otherwise);
                  ctx.getEnv().getLog().println("isStop is "+isStop());
                  ctx.getEnv().getLog().println("ctx.isStop is "+ctx.isStop());

                }
            }
          }else{
            otherwise=false;
          }
       }
     }
   }while(otherwise && !isStop() && !ctx.isStop() );
   ctx.setChanged(globalChanged);
   return x; 
  }

  /**
   * get environment for input/output operations.
   * If one was not set, than environment from TermWare instance is returned.
   *@return i/o environment.
   *@see IEnv
   *@see TermWareInstance#getEnv
   *@see TermSystem#setEnv
   */
  public final IEnv  getEnv()
   { return env_; }
  
  /**
   * set environment for input/output operations.
   *@see IEnv
   *@see TermWareInstance#getEnv
   *@see TermSystem#getEnv
   */  
  public final void  setEnv(IEnv env)
   { env_=env; }
  
  public final synchronized boolean isStop()
  {
    return stop_;
  }
  
  public final synchronized void setStop(boolean stop)
  {
   stop_=stop;
  }

  public final boolean isLoggingMode()
   { return loggingMode_; }

  public final void setLoggingMode(boolean mode)
   { loggingMode_=mode; 
     facts_.setLoggingMode(mode);
   }

  public final boolean checkLoggedEntity(String s)
  {
      return loggedEntities_.contains(s);
  }
  
  public final void  setLoggedEntity(String s)
  {
      loggedEntities_.add(s);
      facts_.setLoggedEntity(s);
  }

  public final void  unsetLoggedEntity(String s)
  {
      loggedEntities_.remove(s);
      facts_.unsetLoggedEntity(s);
  }
  
  public final Set<String>  getLoggedEntities()
  {
      return loggedEntities_;
  }
  
  public final void setLoggedEntities(Collection<String> loggedEntities)
  {  
      loggedEntities_.addAll(loggedEntities);
      facts_.setLoggedEntities(loggedEntities);
  }
  
  public final void clearLoggedEntities()
  {  
      loggedEntities_.clear();
      facts_.clearLoggedEntities();
  }
  
  
  /**
   *get strategy
   *@return strategy of the system
   **/
  public final ITermRewritingStrategy getStrategy()
  {
    return strategy_;
  }
  
  /**
   *get facts base.
   *@return facts of the system.
   */
  public final IFacts getFacts()
  {
   return facts_;
  }

  /**
   *set facts base
   *@param facts - new facts to set.
   */
  public void  setFacts(IFacts facts)
  {
   facts_ = facts;   
  }
  
  /**
   * get instance of runtime environment.
   *@return instance of TermWare runtime.
   */
  public final TermWareInstance getInstance()
  {
   return instance_;   
  }

  /**
   * print to <code> output stream name and descriptions of all rules in system.
   *@param out - where we print help messages.
   */
  public void printHelp(PrintStream out)
  {
      strategy_.getStar().printHelp(out);
  }
  
  /**
   *get patterns for all rules,  added to system.
   *@return sorted set of all patterns in system
   */
  public SortedSet<String> getPatternNames()
  {
      return strategy_.getStar().getNamePatterns();
  }


  private final String ensureSemicolon(String s)
  {
   if(s.endsWith(";")) return s;
   else return s+";";
  }

  
  
  private ITermRewritingStrategy strategy_;
  private IFacts facts_;
  private IEnv   env_;
  private TermWareInstance instance_;
  private boolean    loggingMode_=false;
  private HashSet<String>    loggedEntities_=null;
  private boolean    reduceFacts_=false;
  private volatile boolean    stop_ = false;
  

};
