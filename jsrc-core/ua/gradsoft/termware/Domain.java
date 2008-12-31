package ua.gradsoft.termware;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA>
 * $Id: Domain.java,v 1.10 2008-03-24 22:33:08 rssh Exp $
 */


import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;
import ua.gradsoft.termware.exceptions.AssertException;
import ua.gradsoft.termware.exceptions.EnvException;
import ua.gradsoft.termware.exceptions.InvalidDomainNameException;
import ua.gradsoft.termware.exceptions.InvalidFactsNameException;
import ua.gradsoft.termware.exceptions.InvalidSystemNameException;
import ua.gradsoft.termware.exceptions.ResourceNotFoundException;




/**
 * Domain - hierarchical placement of Term Systems and Facts
 **/
public class Domain
{

 /**
  *create domain with name <code> name </code>
  *in TermWareInstance <code> instance </code>
  */   
 Domain(String name,TermWareInstance instance)
 {
  this(null,name,instance);
 }


 Domain(Domain parent, String name,TermWareInstance instance)
 {
  parent_=parent;
  name_=name;
  subdomains_=new HashMap<String,Domain>();
  systems_=new HashMap<String,TermSystem>();
  facts_=new HashMap<String,IFacts>();  
  instance_=instance;
 }

 /**
  * return domain name.
  */
 public String getName()
   { return name_; }

 /**
  * return parent of this domain
  */
 public Domain getParent()
   { return parent_; }
 

 public Domain getOrCreateSubdomain(Term name) throws TermWareException
  { return getOrCreateSubdomain(name,0); }

 /*
  * name is string or atom or term like _name(x1,..x_n);
  */
 Domain getOrCreateSubdomain(Term name, int level) throws TermWareException
 {
  if (level==0) {
    if (name.isAtom()||name.isString()) {
      return getOrCreateDirectSubdomain(name.getName());
    }else if(name.isComplexTerm()) {
        Domain directSubdomain=getOrCreateDirectSubdomain(name.getSubtermAt(0).getName());
        if (name.getArity()==1) {
            return directSubdomain;
        }else if (name.getArity()>1) {
            return directSubdomain.getOrCreateSubdomain(name,level+1);
        }else{
            throw new InvalidDomainNameException(name);
        }
    }else{
        throw new InvalidDomainNameException(name);
    }
  }else if (name.getArity()<level) {
    if (name.isComplexTerm()) {
      Domain directSubdomain = getOrCreateDirectSubdomain(name.getSubtermAt(level).getName());
      if (level==name.getArity()-1) {  
        return directSubdomain;
      }else{
        return directSubdomain.getOrCreateSubdomain(name,level+1);
      }
    }else{
      throw new InvalidDomainNameException(name);  
    }    
  }else{
    throw new AssertException("Impossible: name.getArity()>=level");
  }                                  
 }

 /**
  * get  subdomain with name <code> name </code>
  *If such subdomain does not exists - it's created.
  **/
 public Domain getOrCreateDirectSubdomain(String name)
 {
  Domain o = subdomains_.get(name);
  if (o==null) {
    o=new Domain(this,name,instance_);
    subdomains_.put(name,o);
  }
  return (Domain)o;
 }

 public Domain getDirectSubdomain(String name) throws InvalidDomainNameException
 {
  Object o = subdomains_.get(name);
  if (o==null) {
      // try to find
      throw new InvalidDomainNameException(name);
  }
  return (Domain)o;
 }
  
 
 /**
  * get subdomain, which can be non-direct: i. e. <code> name </code>
  * can be complex name, where entries separated by <code> / </code>
  * (slash) symbol.
  */
 public Domain getSubdomain(String name) throws InvalidDomainNameException
 {
   String names[] = name.split("/");
   Domain current = this;
   for(int i=0; i<names.length; ++i) {
       current=current.getDirectSubdomain(names[i]);
   }
   return current;
 }
 
 
 /**
  * get or create subdomain, which can be non-direct: i. e. <code> name </code>
  * can be complex name, where entries separated by <code> / </code>
  * (slash) symbol.
  * Note, that
  */
 public Domain getOrCreateSubdomain(String name)
 {
   String names[] = name.split("/");
   Domain current = this;
   for(int i=0; i<names.length; ++i) {
       current=current.getOrCreateDirectSubdomain(names[i]);
   }
   return current;
 }
 
 
 
 public void addSystem(String name, TermSystem system)
 {
  systems_.put(name,system);
 }

 public void addSystem(Term name, TermSystem system) throws TermWareException
 {
  if (name.isAtom()||name.isString()) {
    addSystem(name.getName(),system);
  }else{
    addSystem(name,system,0);
  }
 }

 private void addSystem(Term name, TermSystem system, int level) throws TermWareException
 {
  if (level < name.getArity()-1) {
    Domain d = getOrCreateDirectSubdomain(name.getSubtermAt(level).getName());
    d.addSystem(name,system,level+1);
  }else if (level == name.getArity()-1) {
    systems_.put(name.getSubtermAt(level).getName(),system);
  }else{
    if (level==0 && (name.isAtom() || name.isString()) ) {
      systems_.put(name.getName(),system);
    }else{
      throw new AssertException("addSystem: level >= name.getArity()");
    }
  }
 }


 /**
  * resolve system.
  *@param t  _name term.
  **/
 public TermSystem resolveSystem(Term t) throws TermWareException
 {
  if (t.isAtom()||t.isString()) {
    return resolveSystem(t.getName());
  }else{
    return resolveScopedSystem(t,0);
  }
 }

 /**
  * resolve system with name <code> name </code>
  *Note, that scope of system in this domain is determinatd by path.separators
  */
 public TermSystem resolveSystem(String name) throws TermWareException
 {  
  String[] nameComponents=name.split("/");
  if (nameComponents.length > 1) {
      Term nameTerm=instance_.getTermFactory().createTerm("_name",nameComponents);
      return resolveScopedSystem(nameTerm,0);
  }
     
  Object o = systems_.get(name);
  if (o==null) {
    String fname=createFname(name);
    //System.err.println("check fname:"+fname);
    boolean loaded=false;
    try {
      Term st=instance_.load(fname);
      //instance_.getSysSystem().setDebugMode(true);
      //instance_.getSysSystem().setDebugEntity("All");
      instance_.sysReduce(st);
      loaded=true;
    }catch(EnvException ex){
        // print.
       throw ex;
    }catch(ResourceNotFoundException ex){
        // do nothing, try to load in root domain
    }

    if (loaded) {
      o=systems_.get(name);
      if (o==null) {
        //System.err.println("Systems:" + systems_.keySet().toString());
        throw new AssertException("file "+fname+" must contain system "+name);
      }else{
        return (TermSystem)o;
      }
    }
    if (parent_==null) {
      throw new InvalidSystemNameException(name);
    }else{
      return parent_.resolveSystem(name);
    }
   }else{
    return (TermSystem)o;
   }
 }

 public void removeSystem(Term name) throws TermWareException
 {
  removeSystem(name,0);   
 }

 public void removeSystem(String name)
 {
  systems_.remove(name);
 }


 private void removeSystem(Term name, int level) throws TermWareException
 {
  if (name.isAtom()||name.isString()) {
    if (level==0) {
      removeSystem(name.getName());
    }else{
      throw new AssertException("level > name.arity()");
    }
  }else if(name.isComplexTerm() && name.getName().equals("_name")) {
    if (level < name.getArity()-1) {
      Domain d = getDirectSubdomain(name.getSubtermAt(level).getName());      
      d.removeSystem(name,level+1);
    }else if(level==name.getArity()-1) {
      removeSystem(name.getSubtermAt(level).getName());
    }
  }
 }

 /**
  * resolve facts.
  *@param t  _name term.
  **/
 public IFacts resolveFacts(Term t) throws TermWareException
 {
  if (t.isAtom()||t.isString()) {
    return resolveFacts(t.getName());
  }else{
    return resolveScopedFacts(t,0);
  }
 }

 /**
  * resolve facts.
  *@param name -  name of facts database to find. 
  **/
 public IFacts resolveFacts(String name) throws TermWareException
 {
  Object o = facts_.get(name);
  if (o==null) {
    String fname=createFname(name);
    boolean loaded=false;
    try {
      Term st=instance_.load(fname);
      instance_.sysReduce(st);
      loaded=true;
    }catch(ResourceNotFoundException ex){
      /* ignore*/
    }
    if (loaded) {
      o=facts_.get(name);
      if (o==null) {
        throw new AssertException("file "+fname+" must contain facts "+name);
      }else{
        return (IFacts)o;
      }
    }
    if (parent_==null) {
      throw new InvalidFactsNameException(name);
    }else{
      return parent_.resolveFacts(name);
    }
  }else{
    return (IFacts)o;
  }
 }

 /**
  *get names of direct subdomains.
  *@return names of direct subdomains, sorted in lexicographical order.
  */
 public SortedSet<String> getNamesOfDirectSubdomains()
 {
  TreeSet<String> retval=new TreeSet<String>();
  retval.addAll(subdomains_.keySet());
  return retval;
 }
 
 /**
  *get names of systems, registered in this domain, but not in subdomens.
  *@return names of systems, sorted in lexicographical order.
  */
 public SortedSet<String> getNamesOfSystems()
 {
  TreeSet<String> retval=new TreeSet<String>();
  retval.addAll(systems_.keySet());
  return retval;
 }

 /**
  * remove facts with name <code> name </code>
  */
 public void removeFacts(Term name) throws TermWareException
 {
  removeFacts(name,0);   
 }

 public void removeFacts(String name)
 {
  facts_.remove(name);
 }

 /**
  * add facts database with name <code> name </code> to domain.
  *@param name - name of facts database
  *@param facts - instance of facts database.
  */
 public void addFacts(String name, IFacts facts)
 {  
  facts_.put(name,facts);
 }

 
 public void addFacts(Term name, IFacts facts) throws TermWareException
 {
  if (name.isAtom()||name.isString()) {
    addFacts(name.getName(),facts);
  }else{
    addFacts(name,facts,0);
  }
 }

 private void addFacts(Term name, IFacts facts, int level) throws TermWareException
 {
  if (level < name.getArity()-1) {
    Domain d = getOrCreateDirectSubdomain(name.getSubtermAt(level).getName());
    d.addFacts(name,facts,level+1);
  }else if (level == name.getArity()-1) {
    facts_.put(name.getSubtermAt(level).getName(),facts);
  }else{
    if (level==0 && (name.isAtom() || name.isString()) ) {
      facts_.put(name.getName(),facts);
    }else{
      throw new AssertException("addFacts: level >= name.getArity()");
    }
  }
 }
 


 private void removeFacts(Term name, int level) throws TermWareException
 {
  if (name.isAtom()||name.isString()) {
    if (level==0) {
      removeFacts(name.getName());
    }else{
      throw new AssertException("removeFacts: level > name.arity()");
    }
  }else if(name.isComplexTerm() && name.getName().equals("_name")) {
    if (level < name.getArity()-1) {
      Domain d = getDirectSubdomain(name.getSubtermAt(level).getName());      
      d.removeFacts(name,level+1);
    }else if(level==name.getArity()-1) {
      removeFacts(name.getSubtermAt(level).getName());
    }
  }
 }


 /**
  * resove scoped system.
  *@param t - name term in whhich we search name of the system
  *@param level - level in name, for which we search  (starting from 0)
  *
  */
 private TermSystem resolveScopedSystem(Term t, int level) throws TermWareException
 {
  if (t.isAtom()||t.isString()) {
    if (level==0) {
      return resolveSystem(t);
    }else{       
      throw new InvalidSystemNameException(t.getName());
    }
  }
  if(level < t.getArity()) {
    Term current=t.getSubtermAt(level);
    if (level==t.getArity()-1) {
      Object o=systems_.get(current.getName());
      if (o==null) {
         // try load from external storage:
         String fname=createFname(current.getName());
         boolean loaded=false;
         try {
            Term st=instance_.load(fname);
            instance_.sysReduce(st);
            loaded=true;
         }catch(EnvException ex){
             throw ex;
         }
         if (loaded) {
            o=systems_.get(current.getName());
            if (o==null) {
              throw new AssertException("invalid domain or system name in "+fname);
            }
         }else{
            throw new InvalidSystemNameException(t.getSubtermAt(level).getName());
         }
      }
      return (TermSystem)o;
    }
    if (current.isAtom()) {
      Domain d=getOrCreateDirectSubdomain(current.getName());
      // TODO: cleanup subdomain, if not exists.
      if (d!=null) {
        try {
          return d.resolveScopedSystem(t,level+1);
        }catch(InvalidSystemNameException ex){
          /* ignore, try later */
        }
      }
      if (parent_!=null) {
         return parent_.resolveScopedSystem(t,level);
      }
    }else{
      throw new AssertException("elements of _name term must be atoms");  
    }
  }
  throw new InvalidSystemNameException(TermHelper.termToString(t));
 }

 private IFacts resolveScopedFacts(Term t, int level) throws TermWareException
 {
  if (t.isAtom()||t.isString()) {
    if (level==0) {
      return resolveFacts(t);
    }else{
      throw new InvalidFactsNameException(t.getName());
    }
  }else if(level < t.getArity()) {
    Term current=t.getSubtermAt(level);
    if (level == t.getArity()-1) {
      Object o = facts_.get(current.getName());
      if (o==null) {
         String fname=createFname(current.getName());
         boolean loaded=false;
         try {
            instance_.load(fname);
            loaded=true;
         }catch(EnvException ex){
            /*ignore*/
         }
         if (loaded) {
           o = facts_.get(current.getName());
           if (o==null) {
             throw new AssertException("Facts "+current.getName()+" is not defined in file "+fname);
           }
         }else{
           throw new InvalidFactsNameException(current.getName());
         }
      }
      return (IFacts)o;
    }
    if (current.isAtom()) {
      Domain d=(Domain)subdomains_.get(current.getName());
      if (d!=null) {
        try {
          return d.resolveScopedFacts(t,level+1);
        }catch(InvalidFactsNameException ex){
          /* ignore, try later */
        }
      }
      if (parent_!=null) {
         return parent_.resolveScopedFacts(t,level);
      }
    }
  }
  throw new InvalidFactsNameException(TermHelper.termToString(t));
 }

 private String createFname(String fname)
 {
  if (parent_==null) return fname+".def";
  return createPath()+"/"+fname+".def";
 }

 private String createPath()
 {
  if (parent_==null) return "";
  else if(parent_.getName().equals("root")) {
    return name_;
  }else{
    return parent_.createPath()+"/"+name_;
  }
 }


 private String name_;
 private Domain parent_;
 private HashMap<String,Domain> subdomains_;

 private HashMap<String,TermSystem> systems_;
 private HashMap<String,IFacts>  facts_;
 
 private TermWareInstance instance_;

}