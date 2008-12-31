package ua.gradsoft.termware.util;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2008
 * $Id: ConditionalRuleTransformer.java,v 1.11 2008-03-24 22:33:10 rssh Exp $
 */

import java.util.*;
import ua.gradsoft.termware.*;
import ua.gradsoft.termware.debug.ConditionDebugStub;
import ua.gradsoft.termware.debug.DebugStubRunHelper;
import ua.gradsoft.termware.debug.SetFactsDebugStub;
import ua.gradsoft.termware.debug.SubstitutionDebugStub;
import ua.gradsoft.termware.debug.UnificationDebugStub;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.transformers.general.LetTransformer;

                           

/**
 * Transformer for if_rule (i. e. rules with conditions)
 */
public class ConditionalRuleTransformer extends AbstractRuleTransformer
{

 public ConditionalRuleTransformer(Term rule) throws TermWareException
 {
   if (!rule.getName().equals("if_rule")) 
     throw new AssertException("ConditionalRuleTransformer constructor argument must be if_rule");
   if (rule.getArity()<3 || rule.getArity() > 5) {
     throw new AssertException("if_rule arity must be between 3 and 5");
   }
   rule_=rule;
   inPattern_=rule.getSubtermAt(0);
   if (isWherePattern(inPattern_)) {
       inWhere_=inPattern_.getSubtermAt(1);
       inPattern_=inPattern_.getSubtermAt(0);
   }else{
       inWhere_=null;
   }
   if (isLetPattern(inPattern_)) {
       inLet_=inPattern_.getSubtermAt(0);
       inPattern_=inPattern_.getSubtermAt(1);
   }
   condition_=rule.getSubtermAt(1);
   outPattern_=rule.getSubtermAt(2);
   if (rule.getArity() > 3) {
       elifConditions_=rule.getSubtermAt(3);       
   }else{
       elifConditions_=null; //TermWare.getInstance().getTermFactory().createNIL();
   }
   if (rule.getArity() > 4) {
       failOutPattern_=rule.getSubtermAt(4);
   }else{
       failOutPattern_=null; //TermWare.getInstance().getTermFactory().createNIL();
   }
   Term nameAttribute=TermHelper.getAttribute(rule, "name");  
    if (nameAttribute.isNil()) {
       name_=inPattern_.getName();
    }else{
       name_=TermHelper.termToString(nameAttribute);
    }
    Term descriptionAttribute=TermHelper.getAttribute(rule,"description");
    description_=TermHelper.termToString(descriptionAttribute);
    source_=null;
 }

 public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
   return non_static_transform(inPattern_,outPattern_,condition_,elifConditions_,failOutPattern_,t,system,ctx);    
 }
 
 public  Term  non_static_transform(Term inPattern, Term outPattern, Term condition, Term elifConditions, Term failOutPattern,Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
 
   if (system.isLoggingMode()) {
     if (system.checkLoggedEntity("All") || system.checkLoggedEntity("Rules") || system.checkLoggedEntity(ConditionalRuleTransformer.class.getName())) {
       system.getEnv().getLog().print(ConditionalRuleTransformer.class.getName());           
       system.getEnv().getLog().print(":apply rule, term=");
       t.print(system.getEnv().getLog());
       system.getEnv().getLog().print("\nrule:");
       inPattern.print(system.getEnv().getLog());
       system.getEnv().getLog().print("[");
       condition.print(system.getEnv().getLog());
       system.getEnv().getLog().print("]->");
       outPattern.print(system.getEnv().getLog());
       system.getEnv().getLog().print("\n");
     }
   }

   boolean x;
   if (TermWare.isInDebug()) {
     Class<? extends UnificationDebugStub> stub = getUnificationDebugStubClass(system);
     x=DebugStubRunHelper.runUnificationDebugStub(stub,t,inPattern,ctx.newCurrentSubstitution());
   }else{
     x=inPattern.freeUnify(t,ctx.newCurrentSubstitution());
   }

   boolean changed=false;
   
   if (x) {
     boolean svChanged=ctx.isChanged();         
     Substitution unifySubstitution = ctx.getCurrentSubstitution().cloneSubstitution();
     Term fixedCondition = condition.subst(ctx.getCurrentSubstitution()); 
     ctx.newCurrentSubstitution();
     Substitution inWhereSubstitution = null;
     Substitution inLetSubstitution = null;
     if (inWhere_!=null) {
         fixedCondition = LetTransformer.static_transform_letexpr(inWhere_,fixedCondition,system,ctx);
         inWhereSubstitution = ctx.getCurrentSubstitution().cloneSubstitution();
     }     
     if (inLet_!=null) {
         fixedCondition = LetTransformer.static_transform_letexpr(inLet_,fixedCondition,system,ctx);
         inLetSubstitution = ctx.getCurrentSubstitution().cloneSubstitution();
     }
     ctx.newCurrentSubstitution();
     boolean checkResult;
     if (TermWare.isInDebug()) {
       Class<? extends ConditionDebugStub> stub = getConditionDebugStubClass(system);
       checkResult=DebugStubRunHelper.runConditionDebugStub(stub,system,fixedCondition,ctx);
     }else{
       checkResult=system.checkFact(fixedCondition,ctx);
     }     
     
     if (!checkResult) {
       if (system.isLoggingMode()) {  
          LogHelper.log(system,ConditionalRuleTransformer.class,":condition failed");  
       }
       //ctx.setChanged(svChanged);
       if (elifConditions!=null) {
         Term currentElifs = elifConditions;
         Iterator<ElifDebugStubClasses> stubIt=null;
         if (TermWare.isInDebug()) {
             stubIt = getElifDebugStubClasses(system).iterator();
         }
         while(!currentElifs.isNil() && !checkResult) {
           Term currentElif=currentElifs.getSubtermAt(0);
           Term currentCondition=currentElif.getSubtermAt(0);
           fixedCondition = currentCondition.subst(unifySubstitution);
           if (inWhereSubstitution!=null) {
               fixedCondition = fixedCondition.subst(inWhereSubstitution);
           }
           if (inLetSubstitution!=null) {
               fixedCondition = fixedCondition.subst(inLetSubstitution);
           }
           ctx.newCurrentSubstitution();
           
           if (system.isLoggingMode()) {
               LogHelper.log(system,ConditionalRuleTransformer.class,"check next condition:", currentCondition);               
           }          
           ElifDebugStubClasses currentStubs=null;
           if (TermWare.isInDebug()) {
             currentStubs=stubIt.next();
             Class<? extends ConditionDebugStub> stub = currentStubs.conditionStubClass_;
             checkResult=DebugStubRunHelper.runConditionDebugStub(stub,system,fixedCondition,ctx);
           } else {
             checkResult=system.checkFact(fixedCondition,ctx);
           }
           
           if (!checkResult) {
             currentElifs=currentElifs.getSubtermAt(1);
             if (system.isLoggingMode()) {
                 LogHelper.log(system,ConditionalRuleTransformer.class,"condition failed");               
             }                          
           }else{
             ctx.setChanged(true);  
             changed=true;
             Term currentOutPattern = currentElif.getSubtermAt(1);             
             t=currentOutPattern;
             if (TermWare.isInDebug()) {
                Class<? extends SubstitutionDebugStub> stub = currentStubs.substutionStubClass_;
                t=DebugStubRunHelper.runSubstitutionDebugStub(stub,t,unifySubstitution);                
             }else{
                t=t.subst(unifySubstitution);                 
             }
             if (inWhereSubstitution!=null) {
                 t=t.subst(inWhereSubstitution);
             }
             if (inLetSubstitution!=null) {
                 t=t.subst(inLetSubstitution);
             }
             if (!ctx.getCurrentSubstitution().isEmpty()) {  // it's from checkFacts
                 if (TermWare.isInDebug()) {
                    Class<? extends SubstitutionDebugStub> stub = currentStubs.substutionStubClass_; 
                    t=DebugStubRunHelper.runSubstitutionDebugStub(stub,t,ctx.getCurrentSubstitution());
                 }else{
                     t=t.subst(ctx.getCurrentSubstitution()); 
                 }
             }

             
             if (isWherePattern(t)) {
                 t=LetTransformer.static_transform_letexpr(t.getSubtermAt(1),t.getSubtermAt(0),system,ctx);
             }
             if (isLetPattern(t)) {
                 t=LetTransformer.static_transform_letexpr(t.getSubtermAt(0),t.getSubtermAt(1),system,ctx);
             }
                          
             //
             //ctx.newCurrentSubstitution();

             if (t.isComplexTerm() && t.getNameIndex().equals(TermWareSymbols.ACTION_INDEX)) {
                 if (TermWare.isInDebug()) {
                    Class<? extends SetFactsDebugStub> stub = currentStubs.actionStubClass_;
                    DebugStubRunHelper.runSetFactsDebugStub(stub,system,t.getSubtermAt(1),ctx);
                 }else{
                    system.setFact(t.getSubtermAt(1),ctx);
                 }
                 t=currentOutPattern.getSubtermAt(0);
                 if (TermWare.isInDebug()) {
                    Class<? extends SubstitutionDebugStub> stub = currentStubs.actionSubstitutionStubClass_; 
                    t=DebugStubRunHelper.runSubstitutionDebugStub(stub,t,ctx.getCurrentSubstitution());
                 }else{
                    t=t.subst(ctx.getCurrentSubstitution());
                 }
                 //
                 //ctx.newCurrentSubstitution();
             }
           }
         }         
       }         
       if (checkResult) {
           ctx.setChanged(true);
           changed=true;
       }else{
          if (failOutPattern!=null) {
            if (TermWare.isInDebug()) {
               Class<? extends SubstitutionDebugStub> stub = getFailOutSubstitutionDebugStubClass(system);
               t=DebugStubRunHelper.runSubstitutionDebugStub(stub,failOutPattern,unifySubstitution);             
            }else{
               t=failOutPattern.subst(unifySubstitution);
            }
            if (inWhereSubstitution!=null) {
                t=t.subst(inWhereSubstitution);
            }
            if (inLetSubstitution!=null) {
                t=t.subst(inLetSubstitution);
            }
            
            if (isWherePattern(t)) {
                t=LetTransformer.static_transform_letexpr(t.getSubtermAt(1),t.getSubtermAt(0),system,ctx);
            }
            if (isLetPattern(t)) {
                t=LetTransformer.static_transform_letexpr(t.getSubtermAt(0),t.getSubtermAt(1),system,ctx);
            }
            
            if (isActionPattern(t)) {
                if (TermWare.isInDebug()) {
                    Class<? extends SetFactsDebugStub> stub = getFailOutActionDebugStubClass(system);
                    DebugStubRunHelper.runSetFactsDebugStub(stub,system,t.getSubtermAt(1),ctx);
                }else{
                    system.setFact(t.getSubtermAt(1),ctx);
                }
                t=t.getSubtermAt(0);
                if (TermWare.isInDebug()) {
                   Class<? extends SubstitutionDebugStub> stub = getFailOutActionSubstitutionDebugStubClass(system);
                   t=DebugStubRunHelper.runSubstitutionDebugStub(stub,t,ctx.getCurrentSubstitution());
                }else{
                   t=t.subst(ctx.getCurrentSubstitution());
                }
            }
            ctx.setChanged(true);
            changed=true;
          }else{
            ctx.setChanged(svChanged);      
            if (system.isLoggingMode()) {
                LogHelper.log(system,"Rules",ConditionalRuleTransformer.class,"condition failed");
            }
          } 
       }       
     }else{      
       LogHelper.log(system,"Rules",ConditionalRuleTransformer.class,":succesfull condition=",fixedCondition);  
       LogHelper.log(system,"Rules",ConditionalRuleTransformer.class,":unify substitution=",unifySubstitution);  
       LogHelper.log(system,"Rules",ConditionalRuleTransformer.class,":check substitution=",ctx.getCurrentSubstitution());  
       
       ctx.setChanged(true);
       changed=true;
       t=outPattern;
       if (TermWare.isInDebug()) {
          Class<? extends SubstitutionDebugStub> stub = getSubstitutionDebugStubClass(system);
          t=DebugStubRunHelper.runSubstitutionDebugStub(stub,t,unifySubstitution);
       }else{
          t=t.subst(unifySubstitution); 
       }
       
       if (inWhereSubstitution!=null) {
           t=t.subst(inWhereSubstitution);
       }       
       if (inLetSubstitution!=null) {
           t=t.subst(inLetSubstitution);
       }
       
       if (!ctx.getCurrentSubstitution().isEmpty()) {
           if (TermWare.isInDebug()) {
              Class<? extends SubstitutionDebugStub> stub = getSubstitutionDebugStubClass(system); 
              t=DebugStubRunHelper.runSubstitutionDebugStub(stub,t,ctx.getCurrentSubstitution());
           }else{
              t=t.subst(ctx.getCurrentSubstitution());               
           }
       }
   
       if (isWherePattern(t)) {
           t=LetTransformer.static_transform_letexpr(t.getSubtermAt(1),t.getSubtermAt(0),system,ctx);
       }
       if (isLetPattern(t)) {
           t=LetTransformer.static_transform_letexpr(t.getSubtermAt(0),t.getSubtermAt(1),system,ctx);
       }
       
       if (isActionPattern(t)) {
           ctx.newCurrentSubstitution();
           if (TermWare.isInDebug()) {
              Class<? extends SetFactsDebugStub> stub = getActionDebugStubClass(system);                            
              DebugStubRunHelper.runSetFactsDebugStub(stub,system,t.getSubtermAt(1),ctx);
           }else{
              system.setFact(t.getSubtermAt(1),ctx);
           }
           t=t.getSubtermAt(0);
           if (TermWare.isInDebug()) {
              Class<? extends SubstitutionDebugStub> stub = getActionSubstitutionDebugStubClass(system);
              t=DebugStubRunHelper.runSubstitutionDebugStub(stub,t,ctx.getCurrentSubstitution());
           }else{
              t=t.subst(ctx.getCurrentSubstitution());
           }           
       }
     }
   }else{
     LogHelper.log(system,"Rules",ConditionalRuleTransformer.class,":substitution failed");        
   }
   
   if (system.isLoggingMode()) {
     if (changed) {
        LogHelper.log(system,"Rules",ConditionalRuleTransformer.class,"result=",t); 
     }
   }
   
   //ctx.newCurrentSubstitution();
   return t; 
 }


 public final Term getInPattern()
        { return inPattern_; }

 public final Term getOutPattern()
        { return outPattern_; }

 public final Term getCondition()
        { return condition_; }

 public String getDescription() {
     return description_;
 } 

 public String getName() {
     return name_;
 }
 
 public Term getTerm() throws TermWareException 
 {
   return rule_;  
 }
 
 
 //TODO: add attributes as annotations.
 public String getSource() {
     if (source_==null) {
      try {   
         source_ = TermHelper.termToPrettyString(getTerm());
      }catch(TermWareException ex){
         source_ = TermHelper.termToString(rule_);
      }
     }
     return source_;
 }

 private Class<? extends UnificationDebugStub> getUnificationDebugStubClass(TermSystem system) throws TermWareException
 {
   if (unificationDebugStubClass_==null) {
       unificationDebugStubClass_=system.getInstance().getDebugStubGenerator().generateUnificationDebugStub(inPattern_);
   }  
   return unificationDebugStubClass_;
 }

 private Class<? extends ConditionDebugStub> getConditionDebugStubClass(TermSystem system) throws TermWareException
 {
   if (conditionDebugStubClass_==null) {
       conditionDebugStubClass_=system.getInstance().getDebugStubGenerator().generateConditionDebugStub(condition_);
   }  
   return conditionDebugStubClass_;
 }
 
 private Class<? extends SubstitutionDebugStub> getSubstitutionDebugStubClass(TermSystem system)  throws TermWareException
 {
   if (substitutionDebugStubClass_==null)  {
       substitutionDebugStubClass_=system.getInstance().getDebugStubGenerator().generateSubstitutionDebugStub(outPattern_);
   }
   return substitutionDebugStubClass_;
 }

 private Class<? extends SetFactsDebugStub> getActionDebugStubClass(TermSystem system) throws TermWareException
 {
    if (actionDebugStubClass_==null) {
        actionDebugStubClass_=system.getInstance().getDebugStubGenerator().generateSetFactsDebugStub(outPattern_.getSubtermAt(1));
    } 
    return actionDebugStubClass_;
 }
 
 private Class<? extends SubstitutionDebugStub> getActionSubstitutionDebugStubClass(TermSystem system)  throws TermWareException
 {
   if (actionSubstitutionDebugStubClass_==null)  {
       actionSubstitutionDebugStubClass_=system.getInstance().getDebugStubGenerator().generateFactsSubstitutionDebugStub(outPattern_.getSubtermAt(0));
   }
   return actionSubstitutionDebugStubClass_;     
 }
 
 private Class<? extends SubstitutionDebugStub> getFailOutSubstitutionDebugStubClass(TermSystem system)  throws TermWareException
 {
   if (failOutSubstitutionDebugStubClass_==null)  {
       failOutSubstitutionDebugStubClass_=system.getInstance().getDebugStubGenerator().generateSubstitutionDebugStub(failOutPattern_);
   }
   return failOutSubstitutionDebugStubClass_;
 }
 
 private Class<? extends SetFactsDebugStub> getFailOutActionDebugStubClass(TermSystem system)  throws TermWareException
 {
   if (failOutActionDebugStubClass_==null)  {
       failOutActionDebugStubClass_=system.getInstance().getDebugStubGenerator().generateSetFactsDebugStub(failOutPattern_.getSubtermAt(1));
   }
   return failOutActionDebugStubClass_;
 }

 private Class<? extends SubstitutionDebugStub> getFailOutActionSubstitutionDebugStubClass(TermSystem system)  throws TermWareException
 {
   if (failOutActionSubstitutionDebugStubClass_==null)  {
       failOutActionSubstitutionDebugStubClass_=system.getInstance().getDebugStubGenerator().generateFactsSubstitutionDebugStub(failOutPattern_.getSubtermAt(0));
   }
   return failOutActionSubstitutionDebugStubClass_;
 }
 

 
 
 private List<ElifDebugStubClasses>  getElifDebugStubClasses(TermSystem system)  throws TermWareException
 {
   if (elifStubs_==null) {  
     LinkedList<ElifDebugStubClasses> retval = new LinkedList<ElifDebugStubClasses>();
     Term ct = elifConditions_;
     if (ct!=null) {
         while(!ct.isNil()) {
             Term elif = ct.getSubtermAt(0);
             ct=ct.getSubtermAt(1);
             Term condition = elif.getSubtermAt(0);
             Term outPattern = elif.getSubtermAt(1);
             Term action = null;
             if (outPattern.getNameIndex().equals(TermWareSymbols.ACTION_INDEX)) {
                 action = outPattern.getSubtermAt(1);
             }
             ElifDebugStubClasses stubs = new ElifDebugStubClasses();
             stubs.conditionStubClass_ = system.getInstance().getDebugStubGenerator().generateConditionDebugStub(condition);
             stubs.substutionStubClass_ = system.getInstance().getDebugStubGenerator().generateSubstitutionDebugStub(outPattern);
             if (action!=null) {
               stubs.actionStubClass_ = system.getInstance().getDebugStubGenerator().generateSetFactsDebugStub(action);
               stubs.actionSubstitutionStubClass_ = system.getInstance().getDebugStubGenerator().generateFactsSubstitutionDebugStub(action);
             }
             retval.addLast(stubs);
         }
     }
     elifStubs_=retval;
   }
   return elifStubs_;
 }
 
 private String name_;
 private String description_;
 private String source_;

 // overall rule. used for source-pront
 private Term rule_;
 
 private Term inPattern_;
 private Term inLet_;
 private Term inWhere_;
 private Term outPattern_;
 private Term condition_;
 
 /**
  * elifConditions:  cons( elif(condition,outPattern), ... )
  */
 private Term elifConditions_=null;
 private Term failOutPattern_=null;
 
 private TermWareInstance instance_;
 
 private Class<? extends UnificationDebugStub> unificationDebugStubClass_=null;
 private Class<? extends ConditionDebugStub> conditionDebugStubClass_=null;
 private Class<? extends SetFactsDebugStub>  actionDebugStubClass_=null;
 private Class<? extends SubstitutionDebugStub>  substitutionDebugStubClass_=null;
 private Class<? extends SubstitutionDebugStub>  actionSubstitutionDebugStubClass_=null;
 
 private Class<? extends SubstitutionDebugStub> failOutSubstitutionDebugStubClass_=null;
 private Class<? extends SetFactsDebugStub>   failOutActionDebugStubClass_=null;
 private Class<? extends SubstitutionDebugStub> failOutActionSubstitutionDebugStubClass_=null;
 
 
 private static class ElifDebugStubClasses {
     Class<? extends ConditionDebugStub> conditionStubClass_;
     Class<? extends SubstitutionDebugStub> substutionStubClass_;
     Class<? extends SetFactsDebugStub>   actionStubClass_;
     Class<? extends SubstitutionDebugStub> actionSubstitutionStubClass_;
 }
 private List<ElifDebugStubClasses> elifStubs_=null;
 
 
 
 /** true, when outPattern_ have form action(x,y) */
 // private boolean withAction_;
 
 /** true, when failOutPattern_ have form action(x,y) */
 //private boolean failWithAction_;

}
 