package ua.gradsoft.termware.transformers.sys;


import java.util.Map;
import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.AssertException;

                           
/**
 *Transformer for system definiton term.
 *System definition term is term
 *  system(name,facts,ruleset,strategy) - which means define into current root domain 
 *                                        system with name 'name' and facts 'facts' and
 *                                        ruleset 'ruleste' with strategy 'strategy'.
 * Note, that system term can be a part of domain term - in such way systems can be
 *   hierarchically organized by domains.
 **/
public class SystemTransformer extends AbstractBuildinTransformer
{

 public boolean internalsAtFirst() {
     return false;
 }
   
 /**
  * in <code> t </code> must be system definition.
  * Transformer transform one to true and add system to current TermWare instance.
  *If <code> t </code> does not contain system definition - leave it unchanged.
  */
 public  Term  transform(Term t, TermSystem sys, TransformationContext ctx) throws TermWareException
  { return static_transform(t,sys,ctx); }


 /**
  *@see SystemTransformer#transform
  */
 static public  Term  static_transform(Term t, TermSystem sys, TransformationContext ctx) throws TermWareException 
 { return static_transform(t,sys,ctx,sys.getInstance().getRoot()); }

 static public  Term  static_transform(Term t, TermSystem sys, TransformationContext ctx, Domain where) throws TermWareException
 {
  if (!t.getName().equals("system")) return t;
  if (sys.isLoggingMode()) {
    sys.getEnv().getLog().print("native: system ");
    t.print(sys.getEnv().getLog());
    sys.getEnv().getLog().println();
  }
  if (t.getArity()!=4) {
    sys.getEnv().getLog().println("native: system, arity !=4, return unchanged");
    return t;
  }
  Term nameTerm = t.getSubtermAt(0);
  if (!nameTerm.isString() && !nameTerm.isAtom()) {
    if (sys.isLoggingMode()) {
      sys.getEnv().getLog().println("native: system: first subterm is not name, return unchanged");
    }
    return t;
  }
  String name = nameTerm.getName();
  Term factsTerm = t.getSubtermAt(1);
  IFacts facts = null;
  if (factsTerm.isComplexTerm()) {
      if (factsTerm.getName().equals("javaFacts")) {
          JavaFactsTransformer.static_transform(factsTerm,sys,ctx,where);
          factsTerm=factsTerm.getSubtermAt(0);
      }
      facts=where.resolveFacts(factsTerm);
  }else if (factsTerm.isJavaObject()) {
      Object jo = factsTerm.getJavaObject();
      if (jo instanceof IFacts) {
         facts = (IFacts)jo;       
      }else{
         if (sys.isLoggingMode()) {
            sys.getEnv().getLog().println("native: system: second subterm is object which is not facts "+jo);
         }          
         return t;
      }
  }else{
      if (!factsTerm.isString() && !factsTerm.isAtom()) {  
         if (sys.isLoggingMode()) {
            sys.getEnv().getLog().println("native: system: second subterm is not name, return unchanged - "+TermHelper.termToString(factsTerm));
         }
         return t;
      }
      facts=where.resolveFacts(factsTerm);
  }
  Term ruleset=t.getSubtermAt(2);
  if (!ruleset.getName().equals("ruleset")) {
    if (sys.isLoggingMode()) {
      sys.getEnv().getLog().println("native: system: third subterm is not ruleset, return unchanged");
    }
    return t;
  }
    
  Term strategyTerm=t.getSubtermAt(3);
  if (!strategyTerm.isString() && !strategyTerm.isAtom()) {
    if (sys.isLoggingMode()) {
      sys.getEnv().getLog().println("native: system: fourth subterm is not name, return unchanged");
    }
    return t;
  }
  ITermRewritingStrategy strategy=TermWare.getInstance().createStrategyByName(strategyTerm.getName());
  TermSystem newSystem=new TermSystem(strategy,facts,sys.getInstance());
  newSystem.setLoggingMode(sys.isLoggingMode());
  sys.getInstance().addRuleset(where,newSystem,ruleset);
  where.addSystem(name,newSystem);
  Term descriptionTerm = TermHelper.getAttribute(t, "description");
  if (!descriptionTerm.isNil()) {
     // newSystem.setDescription
  }
  Term debugModeTerm = TermHelper.getAttribute(t, "debugMode");
  
 // if (t instanceof Attributed){
 //     Attributed at = (Attributed)t;
 //     for(Map.Entry<String,Term> e: at.getAttributes().entrySet()) {
 //         System.err.println("attribute:("+e.getKey()+TermHelper.termToString(e.getValue())+")");
 //     }
 // }
  
  if (!debugModeTerm.isNil()) {
      if (debugModeTerm.isBoolean()) {
          newSystem.setLoggingMode(debugModeTerm.getBoolean());
      }
  }
  Term loggedEntityTerm = TermHelper.getAttribute(t, "loggedEntity");
  if (!loggedEntityTerm.isNil()) {
      newSystem.setLoggingMode(true);
      newSystem.setLoggedEntity(loggedEntityTerm.getAsString(sys.getInstance()));
  }
  
  Term optionTerm = TermHelper.getAttribute(t, "option");
  if (!optionTerm.isNil()) {
      if (optionTerm.isComplexTerm()) {
        if (optionTerm.getName().equals("reduceInFacts")) {
            if (optionTerm.getArity()==1) {
                Term arg = optionTerm.getSubtermAt(0);
                if (arg.isBoolean()) {
                    newSystem.setReduceFacts(arg.getBoolean());
                }else{
                    throw new AssertException("argument of rediceInFacts must be boolean");
                }
            }else{
                throw new AssertException("reduceInFacts option must have arity 1");
            }                    
        }else if (optionTerm.getName().equals("loggedEntity")){
            newSystem.setLoggingMode(true);
            for(int i=0; i<optionTerm.getArity(); ++i) {
               Term arg = optionTerm.getSubtermAt(i);
               if (arg.isString()) {
                   newSystem.setLoggedEntity(arg.getString());
               }else if (arg.isAtom()) {
                   newSystem.setLoggedEntity(arg.getName());
               }else{
                   throw new AssertException("Invalid loogedEntity:"+TermHelper.termToString(arg));
               }                     
            }            
        }else{
            throw new AssertException("Unknown system option:"+TermHelper.termToString(optionTerm));
        }
      }
  }
  
  if (sys.isLoggingMode()) {
     sys.getEnv().getLog().println("native: system: -- added.");
  }
  ctx.setChanged(true);
  return TermFactory.createBoolean(true);
 }


 public String getDescription() {
     return staticDescription_;
 }
 
 public String getName() {
     return "system";
 }
 
 private static final String staticDescription_=
  "  system(name,facts,ruleset,strategy) - which means import into current root domain "+
  "                                        system with name 'name' and facts 'facts' and" +
  "                                        ruleset 'ruleste' with strategy 'strategy'." +
  " Note, that system term can be a part of domain term - in such way systems can be " +
  "   hierarchically organized by domains. ";

    
 
}
 