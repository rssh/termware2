package ua.gradsoft.termware;


import java.io.*;
import java.math.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.prefs.*;
import ua.gradsoft.termware.debug.DebugStubGenerator;

import ua.gradsoft.termware.exceptions.AssertException;
import ua.gradsoft.termware.exceptions.RuntimeAssertException;
import ua.gradsoft.termware.exceptions.EnvException;
import ua.gradsoft.termware.exceptions.IllegalParserNameException;
import ua.gradsoft.termware.exceptions.IllegalPrinterNameException;
import ua.gradsoft.termware.exceptions.InvalidStrategyNameException;
import ua.gradsoft.termware.envs.SystemEnv;
import ua.gradsoft.termware.exceptions.ExternalException;
import ua.gradsoft.termware.exceptions.InvalidFactsNameException;
import ua.gradsoft.termware.exceptions.InvalidTermLoaderClassNameException;
import ua.gradsoft.termware.parsers.terms.util.TermParserFactory;
import ua.gradsoft.termware.parsers.xml.terms.XMLTermParserFactory;
import ua.gradsoft.termware.printers.terms.TermWarePrinterFactory;
import ua.gradsoft.termware.util.TransformersStar;
import ua.gradsoft.termware.util.AbstractRuleTransformer;
import ua.gradsoft.termware.util.JavaLangReflectHelper;
import ua.gradsoft.termware.strategies.FirstTopStrategy;
import ua.gradsoft.termware.strategies.TopDownStrategy;

/**
 * Instance for TermWare-wide general work.
 * (loading files, keepen domain system and so on)
 */
public class  TermWareInstance                                 
{
  
   /**
    * init instance
    */
    public synchronized void init()
    {        
      inInit_=true;
      initSymbolTable();      
      try {
        initTermLoader();
      }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }
      try {
          parseOptions();
      }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }
      
      addJavaStrategy("FirstTop","ua.gradsoft.termware.strategies.FirstTopStrategy");
      addJavaStrategy("TopDown","ua.gradsoft.termware.strategies.TopDownStrategy");
      addJavaStrategy("BottomTop","ua.gradsoft.termware.strategies.BTStrategy");
      addJavaStrategy("NFirstTops","ua.gradsoft.termware.strategies.NFirstTopsStrategy");
      
      try {
          
          addJavaFacts("default",new DefaultFacts());
      
          addParserFactory("TermWare",termParserFactory_);
          addPrinterFactory("TermWare",new TermWarePrinterFactory());

          addParserFactory("XMLTermWare", new XMLTermParserFactory());
     
          IFacts facts;
          facts=new DefaultFacts();
          sysSystem_=new TermSystem(new FirstTopStrategy(),facts,this);
          TermWare.addGenSysTransformers(sysSystem_);
          addSystem("sys",sysSystem_);
          
          generalSystem_=new TermSystem(new TopDownStrategy(),facts,this);
          TermWare.addGeneralTransformers(generalSystem_);   
          addSystem("general",generalSystem_);

          IFacts nullFacts=new NullFacts();
          
          stringSystem_=new TermSystem(new FirstTopStrategy(), facts,this);
          TermWare.addStringTransformers(stringSystem_);
          addSystem("String",stringSystem_);

          listSystem_=new TermSystem(new FirstTopStrategy(),facts,this);
          TermWare.addListTransformers(listSystem_);
          addSystem("List",listSystem_);
          
          addJavaFacts("null", new NullFacts());
          addJavaFacts("default", new DefaultFacts());

          
      }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }
      
      String debugProperty = System.getProperty("termware.debug");
      if (debugProperty!=null) {
          if (debugProperty.equals("true")||debugProperty.equals("yes")||debugProperty.equals("1")) {
              TermWare.setInDebug(true);
          }else if (debugProperty.equals("false")||debugProperty.equals("no")||debugProperty.equals("0")){
              TermWare.setInDebug(false);
          }else{
              throw new TermWareRuntimeException(new AssertException("termware.debug must be one of 'true','false','yes','no','1','0' "));
          }
      }

      inInit_=false;
      initialized_=true;
    }

    public void setOptions(String[] args)
    {
      options_=args;
    }
    
    /**
     * parse command-line options.
     */
    private void parseOptions() throws TermWareException
    {
       //parse options
      if (options_==null) return;  
      for(int i=0; i<options_.length; ++i) {
        if (options_[i].equals("-props")) {
           if (i==options_.length-1) {
               throw new TermWareException("-props option require argument");
           }else{
               importProperties(options_[i+1]);
               ++i;
           }
       }else if(options_[i].equals("-I")) {
           if (i==options_.length-1) {
               throw new TermWareException("-I option require argument");
           }else{
               if (termLoader_==null) {
                   initTermLoader();
               }
               termLoader_.addSearchPath(options_[i+1]);
               ++i;
           }
       }else{
           // skip unknown options.
       }
        
      }
    }

    /**
     * initialize (and parse options before)
     */
    public void init(String[] args) throws TermWareException
    {
      setOptions(args);
      init();
    }
    
  /**
   *add ruleset <code> ruleset </code> to system <code> system </code>
   *in domain <code> d </code>.  System <code> system </code> must exists
   *in <code> d </code>
   *TODO: move to domain.
   */
  public  void addRuleset(Domain d, TermSystem system, Term ruleset)
                                              throws TermWareException 
  {
    if (!ruleset.isComplexTerm()) {
       throw new AssertException("ruleset must be complex term");
    }
    if (!ruleset.getName().equals("ruleset")) {
       throw new AssertException("ruleset term expected");
    }
    for(int i=0; i<ruleset.getArity(); ++i) {
      Term current = ruleset.getSubtermAt(i);
      
      if (current.isComplexTerm() && current.getName().equals("import")) {
         if (current.getArity()==1) {
            TermSystem fromSys = d.resolveSystem(current.getSubtermAt(0));
            TransformersStar transformersStar=fromSys.getStrategy().getStar();
            
            Set<String> namePatterns=transformersStar.getNamePatterns();         
            for(String namePattern : namePatterns) {
               Iterator<ITermTransformer> it = transformersStar.iterator(namePattern); 
               while(it.hasNext()) {
                   ITermTransformer tr = it.next();
                   system.addNormalizer(namePattern, tr);
               }
            }
         }else if(current.getArity()==2) {
            Term termPatternName=current.getSubtermAt(1);
            if (!termPatternName.isString()&&!termPatternName.isAtom()) {
                throw new AssertException("secong argument of import must be atom or string");
            }
            String patternName=termPatternName.getName();
            TermSystem fromSys = d.resolveSystem(current.getSubtermAt(0));
            TransformersStar star=fromSys.getStrategy().getStar();
            Iterator it=star.iterator(patternName);
            boolean added=false;
            while(it.hasNext()) {
                ITermTransformer tr=(ITermTransformer)it.next();
                system.addNormalizer(patternName, tr);
                added=true;
            }
            if (!added) {
                throw new AssertException("can't find transfromer '"+patternName+"' for import from system "+TermHelper.termToString(current.getSubtermAt(0)));///*TermHelper.termToPrettyString(current.getSubtermAt(0)*/, "TermWare", TermFactory.createNIL())+" pattern "+patternName );
            }
         }else{
           throw new AssertException("import in ruleset must have arity 1 or 2");
         }
      }else if(current.isComplexTerm() && current.getName().equals("importTransformed")) {
        if (current.getArity()!=2) {
           throw new AssertException("arity of importTransformed must be 2");
        }
        if ( ! (current.getSubtermAt(0).isAtom()||current.getSubtermAt(0).isString())) {
           throw new AssertException("first argument of importTransformed mus be system name");
        }

        ITermRewritingStrategy strategy=new FirstTopStrategy();

        IFacts facts=root_.resolveFacts("default");

        TermSystem tmpSys=new TermSystem(strategy,facts,this);
        tmpSys.setLoggingMode(system.isLoggingMode());
        addRuleset(d,tmpSys,current.getSubtermAt(1));

        TermSystem fromSys = d.resolveSystem(current.getSubtermAt(0));

        Iterator it = fromSys.getStrategy().getStar().iterator();
        
        while(it.hasNext()) {
           Object o=it.next();
           Term rule=null;
           if (o instanceof AbstractRuleTransformer) {
               rule=((AbstractRuleTransformer)o).getTerm();
           }else{
               continue;
           }
           rule=TermHelper.escapeX(rule);
           rule=tmpSys.reduce(rule);
           rule=TermHelper.unescapeX(rule);
           if (tmpSys.isLoggingMode()) {
             tmpSys.getEnv().getLog().println("-------rule transformation end");
             tmpSys.getEnv().getLog().print("transformed rule is:");
             rule.print(tmpSys.getEnv().getLog());
             tmpSys.getEnv().getLog().println();
           }

           system.addRule(rule);
        }
         
      }else{
         system.addRule(current);
      }
    }
  }

  
  /**
   * add system <code> system </code> with name <code> name </code>
   *to root domain.
   */
  public void addSystem(Term name, TermSystem system)
                                              throws TermWareException 
  {
    root_.addSystem(name,system);
  }

  /**
   * add system <code> system </code> with name <code> name </code>
   *to root domain.
   */
  public void addSystem(String name, TermSystem system)
                                              throws TermWareException 
  {
    root_.addSystem(name,system);
  }

  /**
   * resolve system in root domain with name <code> t </code>
   */
  public TermSystem resolveSystem(Term t)
                                           throws TermWareException
  {
   return  root_.resolveSystem(t);
  }
  
  
  /**
   * resolve system in root domain with name <code> name </code>
   */
  public TermSystem resolveSystem(String name)
                                           throws TermWareException
  {
   return  root_.resolveSystem(name);
  }  

  /**
   * remove system with name <code> name </code> from root domain.
   */
  public void removeSystem(Term name) throws TermWareException
  {
    root_.removeSystem(name);
  }


  /**
   * add facts to root domain.
   *@param name - name of facts database.
   *@param facts - instance of implementation class.
   */
  public void addJavaFacts(String name, IFacts facts)
  {
    root_.addFacts(name, facts);   
  }
  
  /**
   * add facts to root domain
   *@param name - name of domain (atom or _name term)
   *@param facts - facts database to add.
   */
  public void addFacts(Term name, IFacts facts) throws TermWareException
  {   
   root_.addFacts(name,facts);                                                  
  }


  /**
   * resolve facts in root domain
   */
  public IFacts getFacts(Term name) throws TermWareException
  {
    try {  
      return root_.resolveFacts(name);
    }catch(InvalidFactsNameException ex){
        if (parentInstance_!=null) {
            return parentInstance_.getFacts(name);
        }else{
            throw ex;
        }
    }
  }

  /**
   * get facts database from root domain.
   *@param name - name of fact database.
   */
  public IFacts getFacts(String name) throws TermWareException
  {
    try {  
     return root_.resolveFacts(name); 
    }catch(InvalidFactsNameException ex){
        if (parentInstance_!=null) {
            return parentInstance_.getFacts(name);
        }else{
            throw ex;
        }
    }
  }
  

  public  void removeFacts(Term name) throws TermWareException
  {
    root_.removeFacts(name);
  }


  /**
   * add strategy with name <code> name </code> and implementation class
   *<code> className </code>
   *@param name -- name of strategy
   *@param className -- implementation class for strategy, which must implement ITermRewritingStrategy interface
   *@see  ua.gradsoft.termware.ITermRewritingStrategy
   */
  public  void addJavaStrategy(String name, String className)
  {
   strategies_.put(name,className);
  }

  /**
   * remove strategy with name <code> name </code> from set of strategies.
   */
  public  void removeStrategy(String name)
  {
    strategies_.remove(name);
  }

 /**
  * create instance of strategy with name <code> name </code> (instantiente appropriative
  * class, defined by TermWareaddJavaStrategy(name,name).
  */
  public ITermRewritingStrategy createStrategyByName(String name) throws TermWareException
  {
    Object o = strategies_.get(name);    
    if (o==null) {
      if (parentInstance_!=null) {
          return parentInstance_.createStrategyByName(name);
      }else{  
          throw new InvalidStrategyNameException(name);
      }
    }
    String className = (String)o;
    if (className==null) {
      throw new AssertException(name+" is not assigned to strategy");
    }
    return createJavaStrategy(className);
  }

 /**
  * create strategy from java class.
  **/
  ITermRewritingStrategy  createJavaStrategy(String className) throws TermWareException
 {
  ITermRewritingStrategy retval=(ITermRewritingStrategy) JavaLangReflectHelper.instantiateObject(className);
  if (retval==null) {
     throw new AssertException("class "+className+" not implements ITermRewritingStrategy");
  }
  return retval;                 
 }


  /**
   * get subdomain of root domain with name <code> name </code>
   *If such domain does not exists - it's created.
   *@param name - name of domain to get.
   *<code> name </code> must be string or atom or denote compound name, i. e. have form 
   *<code> _name(t1 ... tN) </code>.
   *
   *@exception  AssertException, if <code> name </code> is not name term.
   */
  public Domain getOrCreateDomain(Term name) throws TermWareException
  {
   
   if (!name.isComplexTerm()) {
     if (name.isString()) {
         return root_.getOrCreateDirectSubdomain(name.getString());
     }else if (name.isAtom()) {
         return root_.getOrCreateDirectSubdomain(name.getName());
     }else{
         throw new AssertException("Invalid name:"+TermHelper.termToString(name));
     }
   }else if(name.getName().equals("_name")) {
     if (name.getArity()>0) {
       return root_.getOrCreateSubdomain(name);
     }else{  // name.getArity()==0 
       return root_;
     }
   }else{
     throw new AssertException("Invalid scoped name:"+TermHelper.termToString(name));
   }
  }

  /**
   * get direct subdomain of root domain with name <code> name </code>
   *If such domain does not exists - it's created.
   *@param name - name of domain to get.
   */
  public Domain getOrCreateDomain(String name) throws TermWareException
  {
   return root_.getOrCreateDirectSubdomain(name);
  }



  /**
   * get domain or throw InvalidDomainName exception is such subdomen does not exists
   *in root domain of this instance.
   */
  public Domain getDomain(String name) throws TermWareException
  {
    return getRoot().getSubdomain(name);
  }
  
  /**
   * return "sys" system.
   */
  public TermSystem getSysSystem()
  {
      return sysSystem_;
  }

  /**
   * get names of domains (direct sundomens of root), loaded into current runtime.
   */
   public SortedSet  getDomainNames()
  {
      return root_.getNamesOfDirectSubdomains();
  }
  

  /**
   * load file with expression in TermWare language.
   *@param name - name of file to load.
   *
   *@return term from file
   **/
  public Term load(String name)
                                              throws TermWareException
  {
   Term retval = load(name,termParserFactory_,TermFactory.createNIL());
   return retval;
  }


  /**
   * load file with name <code> name <name> and return target term.
   *@param name - name of file to load.
   *@param parserFactory - parserFactory to use.
   *@param parserFactoryArgs - additional arguments to parserFactory.
   *
   *@return term from default term loader.
   *
   *@see ua.gradsoft.termware.TermLoader
   *@see ua.gradsoft.termware.termloaders.FileOrClassTermLoader
   *
   **/
  public Term load(String name,IParserFactory parserFactory,Term parserFactoryArgs)
                                                                           throws TermWareException
  {
   if (!initialized_) {
      init();
   }
   return termLoader_.load(name, parserFactory,parserFactoryArgs);   
  }

  

 /**
  * add parser for language <code> language-name </code> defined by
  * interface IParser.
  **/
 public  void addParserFactory(String languageName, IParserFactory parserFactory)
 {
  synchronized(parsers_) {   
     parsers_.put(languageName,parserFactory);
  }
 }

 /**
  * remove parser for language <code> language-name </code> 
  **/
 public  void removeParserFactory(String languageName)
 {
  synchronized(parsers_) {
     parsers_.remove(languageName);
  }
 }                                  


 /**
  * get parserFactory for language <code> language-name </code> 
  *@see IParserFactory
  *@see IParser
  **/
 public  IParserFactory getParserFactory(String languageName)
                                        throws IllegalParserNameException
 {   
  Object o;
  synchronized(parsers_) {
    o = parsers_.get(languageName);
  }
  if (o==null) throw new IllegalParserNameException(languageName);
  return (IParserFactory)o;
 }

 /**
  * add printer for language <code> language-name </code> defined by
  * interface IPrinter
  **/
 public  void addPrinterFactory(String languageName, 
                                             IPrinterFactory printerFactory)
 {
  synchronized(printers_) {
    printers_.put(languageName,printerFactory);
  }
 }


 /**
  * remove printer for language <code> language-name </code> 
  **/
 public  void removePrinterFactory(String languageName)
 {
  synchronized(printers_) { 
     printers_.remove(languageName);
  }
 }                                  

 /**
  * get printerFactory for language <code> language-name </code> 
  *@see IPrinterFactory
  *@see IPrinter
  **/
 public  synchronized IPrinterFactory getPrinterFactory(
                                                         String languageName)
                                                throws IllegalPrinterNameException
 {
  if (!initialized_) {
      init();
  }
  Object o = printers_.get(languageName);
  if (o==null) throw new IllegalPrinterNameException(languageName);
  return (IPrinterFactory)o;
 }

 /**
  * reduce term <code> t </code> by default <sys> system.
  **/
 public  synchronized  Term sysReduce(Term t) throws TermWareException
 {
  if (!initialized_) {
      init();
  }
  return sysSystem_.reduce(t);
 }

 /**
  * get term loader.
  */
 public   TermLoader   getTermLoader()
 {  
   return termLoader_;
 }
 
 /**
  * set classname of term loader.
  *(call of this method forse reinitialization of term-loader).
  */
 void   initTermLoader() throws InvalidTermLoaderClassNameException, ExternalException
 {
   if (termLoaderClassName_==null) {
    Preferences prefs=Preferences.userNodeForPackage(this.getClass());
    String name=prefs.get("TermLoaderClassName","ua.gradsoft.termware.termloaders.FileOrClassTermLoader");
    try {
       setTermLoaderClassName(name);       
    }catch(ClassNotFoundException ex){
       termLoaderClassName_=null; 
       throw new InvalidTermLoaderClassNameException(name);
    }catch(InstantiationException ex){
        termLoaderClassName_=null; 
        throw new ExternalException(ex);
    }catch(IllegalAccessException ex){
        termLoaderClassName_=null; 
        throw new ExternalException(ex);
    }
   }
 }
 
 /**
  * ser classname for term loader
  */
 public   void   setTermLoaderClassName(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException
 {
     termLoaderClassName_=className;
     Class cl=Class.forName(className);
     termLoader_=(TermLoader)cl.newInstance();
     termLoader_.setTermWareInstance(this);
 }

      
  
 /**
  * get rounding mode, which used in ariphmetic operations.
  *@see java.math.BigDecimal.
  */
 public int getRoundingMode()
 { return roundingMode_; }
 
 /**
  * set rounding mode, which used in ariphmetic operations.
  *@see java.math.BigDecimal.
  */
 public void setRoundingMode(int roundingMode)
 { roundingMode_=roundingMode; }
 
 /**
  * get target scale for BigDecimal operations.
  *@see java.math.BigDecimal#divide
  */
 public int  getDecimalScale()
 { return decimalScale_; }
 
 /**
  * set target scale for BigDecimal operations.
  *@see java.math.BigDecimal#divide
  */
 public void  setDecimalScale(int decimalScale)
 { decimalScale_=decimalScale; }
 
 
 
 /**
  * get root domain
  *@return root domain
  **/
 public Domain getRoot() 
  { return root_; }

 /**
  * get default environment
  */
 public  IEnv       getEnv()
 { if (env_==null) env_=new SystemEnv();
   return env_;
 }
 
 /**
  * set default environment
  */
 public  void       setEnv(IEnv env)
 { env_=env; }
 
 /**
  * get TermFactory for this instance.
  *@see TermFactory
  */
 public  TermFactory       getTermFactory()
 { if (termFactory_==null) {
       termFactory_=new TermFactory(this);
   }
   return termFactory_;
 }
  
  
 /**
  * set parent instance for this instance.
  */ 
 public synchronized void setParentInstance(TermWareInstance parentInstance)
 {
  parentInstance_=parentInstance;   
 }

 
 // work with symbol table.

 void initSymbolTable()
 {
  if (nameSymbolIndex_==null) {
      nameSymbolIndex_=SymbolTable.getSymbolTable().adoptName(TermWareSymbols.UNDERSCORE_NAME_STRING,true).getIndex();
  }   
  if (consSymbolIndex_==null) {
       consSymbolIndex_=SymbolTable.getSymbolTable().adoptName(TermWareSymbols.CONS_STRING,true).getIndex();
       if (!consSymbolIndex_.equals(TermWareSymbols.CONS_INDEX)) {
           throw new RuntimeAssertException("self-test failed for CONS_INDEX");
       }
  }
  if (setSymbolIndex_==null) {
       setSymbolIndex_=SymbolTable.getSymbolTable().adoptName(TermWareSymbols.SET_STRING,true).getIndex();
       if (!setSymbolIndex_.equals(TermWareSymbols.SET_INDEX)) {
           throw new RuntimeAssertException("self-test failed for SET_INDEX");
       }
  }  
  if (setPatternSymbolIndex_==null) {
       setPatternSymbolIndex_=SymbolTable.getSymbolTable().adoptName(TermWareSymbols.SET_PATTERN_STRING,true).getIndex();
       if (!setPatternSymbolIndex_.equals(TermWareSymbols.SET_PATTERN_INDEX)) {
           throw new RuntimeAssertException("self-test failed for SET_PATTERN_INDEX");
       }
  }  
  if (jobjectSymbolIndex_==null) {
      jobjectSymbolIndex_=SymbolTable.getSymbolTable().adoptName(TermWareSymbols.JOBJECT_STRING, true).getIndex();
      if (!jobjectSymbolIndex_.equals(TermWareSymbols.JOBJECT_INDEX)) {
           throw new RuntimeAssertException("self-test failed for JOBJECT_INDEX");
      }
  }
  if (nilSymbolIndex_==null) {
      nilSymbolIndex_=SymbolTable.getSymbolTable().adoptName(TermWareSymbols.NIL_STRING, true).getIndex();
      if (!nilSymbolIndex_.equals(TermWareSymbols.NIL_INDEX)) {
           throw new RuntimeAssertException("self-test failed for NIL_INDEX");
      }
  }
  
   classPatternSymbolIndex_=SymbolTable.getSymbolTable().adoptName(TermWareSymbols.CLASS_PATTERN_STRING,true).getIndex();      
  
   Integer symbolIndex =SymbolTable.getSymbolTable().adoptName(TermWareSymbols.ACTION_STRING, true).getIndex();
   if (!symbolIndex.equals(TermWareSymbols.ACTION_INDEX)) {
       throw new RuntimeAssertException("self-test failed for ACTION_INDEX");
   }
   
   symbolIndex = SymbolTable.getSymbolTable().adoptName(TermWareSymbols.ARGS_PATTERN_STRING, true).getIndex();
   if (!symbolIndex.equals(TermWareSymbols.ARGS_PATTERN_INDEX)) {
       throw new RuntimeAssertException("self-test failed for ARGS_PATTERN_INDEX");
   }

   symbolIndex = SymbolTable.getSymbolTable().adoptName(TermWareSymbols.LET_STRING, true).getIndex();
   if (!symbolIndex.equals(TermWareSymbols.LET_INDEX)) {
       throw new RuntimeAssertException("self-test failed for LET_INDEX");
   }

   symbolIndex = SymbolTable.getSymbolTable().adoptName(TermWareSymbols.WHERE_STRING, true).getIndex();
   if (!symbolIndex.equals(TermWareSymbols.WHERE_INDEX)) {
       throw new RuntimeAssertException("self-test failed for WHERE_INDEX");
   }
   
   
 }
    
 
 final public SymbolTable getSymbolTable()
 {   return SymbolTable.getSymbolTable(); }
 
 public final SymbolAdoptionPolicy getSymbolAdoptionPolicy()
 { return symbolAdoptionPolicy_; }
 
 public final void setSymbolAdoptionPolicy(SymbolAdoptionPolicy symbolAdoptionPolicy)
 { symbolAdoptionPolicy_ = symbolAdoptionPolicy; }
 
 public DebugStubGenerator  getDebugStubGenerator()
 { return debugStubGenerator_; }
 
 /**
  *return TypeConversion of this instance.
  */
 final public TypeConversion getTypeConversion()
 {  
     if (!initialized_) {
         init();
     }
     if (typeConversion_==null) {
       synchronized(this) {
           if (typeConversion_==null) {
             typeConversion_=new TypeConversion(this);
           }
       }
   }
   return typeConversion_;    
 }
 
 private static void importProperties(String prefsFname) throws EnvException
 {
    FileInputStream prefsStream;
    try {
        prefsStream=new FileInputStream(prefsFname);
    }catch(IOException ex){
        throw new EnvException("Can't open file '"+prefsFname+"' for reading:"+ex.getMessage());
    }
    try {
        Preferences.importPreferences(prefsStream);
    }catch(IOException ex){
        throw new EnvException("Error during reading '"+prefsFname+"'"+ex.getMessage());
    }catch(InvalidPreferencesFormatException ex){
        throw new EnvException("Can't parse +'"+prefsFname+"':"+ex.getMessage());
    }finally{
        try {
          prefsStream.close();
        }catch(IOException ex){
            logger_.warning("exception during closing preferences file:"+ex.getMessage());
        }
    }
 }
 
 
 
 private Integer consSymbolIndex_=null;
 private Integer setSymbolIndex_=null;
 private Integer setPatternSymbolIndex_=null;
 private Integer jobjectSymbolIndex_=null;
 private Integer nilSymbolIndex_=null; 
 private Integer nameSymbolIndex_=null;
 private Integer classPatternSymbolIndex_=null;
 
 private SymbolAdoptionPolicy symbolAdoptionPolicy_=SymbolAdoptionPolicy.ADD;

 // 
 private transient boolean initialized_=false;
 private transient boolean inInit_=false;
 
 private HashMap<String,String> strategies_ = new HashMap<String,String>();
 private HashMap<String,IParserFactory> parsers_ = new HashMap<String,IParserFactory>();
 private HashMap<String,IPrinterFactory> printers_ = new HashMap<String,IPrinterFactory>();
 private Domain root_ = new Domain("root",this);

 private TermSystem sysSystem_=null;
 private TermSystem generalSystem_=null;
 private TermSystem stringSystem_=null;
 private TermSystem listSystem_=null;

 private String[]   options_=null;
  
 private int        roundingMode_ = BigDecimal.ROUND_HALF_UP;
 private int        decimalScale_ = 10;
 
 private IEnv       env_ = null;

 
 /**
  * instance of term parser factory (for fast access, copy is in
  * parsers.
  **/
 private static TermParserFactory termParserFactory_ = new TermParserFactory();

 /**
  * instance of TermFactory
  */
 private TermFactory  termFactory_=null;

 
 //
 //
 //
 private TermWareInstance parentInstance_=null;
 
 /**
  * term loader.
  */
 private transient TermLoader       termLoader_=null;
 
 
 
 /**
  * name of term-loader class
  */
 private            String          termLoaderClassName_=null;

 /**
  * type conversion
  */
 private transient TypeConversion   typeConversion_=null;
 
 /**
  * debug stub generator.
  */
 private DebugStubGenerator  debugStubGenerator_=new DebugStubGenerator(this);
  

 /**
  * Logger.
  */
 private static Logger  logger_ = Logger.getLogger(TermWareInstance.class.getName());

 
};                
