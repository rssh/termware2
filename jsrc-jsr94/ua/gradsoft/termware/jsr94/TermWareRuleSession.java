/*
 * TermWareRuleSession.java
 *(C) Grad-Soft Ltd, Kiev, Ukraine.
 *http://www.gradsoft.ua
 *2002 - 2005
 */

package ua.gradsoft.termware.jsr94;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.rules.InvalidRuleSessionException;
import javax.rules.ObjectFilter;
import javax.rules.RuleExecutionSetMetadata;
import javax.rules.RuleSession;
import javax.rules.RuleSessionCreateException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareInstance;

import ua.gradsoft.termware.jsr94.admin.TermWareRuleExecutionSet;

/**
 *
 * @author Ruslan Shevchenko
 */
public abstract class TermWareRuleSession implements RuleSession
{
    
    TermWareRuleSession(TermWareRuleRuntime runtime,TermWareRuleExecutionSet ruleExecutionSet,Map properties) throws RuleSessionCreateException
    {
        runtime_=runtime;
        ruleExecutionSet_=ruleExecutionSet;
        properties_=properties;                
        instance_=TermWare.createNewInstance();
        handleInstanceProperties();
        
        Term systemTerm=ruleExecutionSet_.getSystemTerm();
        Term currentSystemTerm=systemTerm;
        List domains=new ArrayList();
        String name=null;
        while(currentSystemTerm.getName().equals("domain")) {
            domains.add(currentSystemTerm.getSubtermAt(0));  
            currentSystemTerm=currentSystemTerm.getSubtermAt(1);
        }
        if (currentSystemTerm.getName().equals("system")){
            Term nameTerm=currentSystemTerm.getSubtermAt(0);
            if (nameTerm.isAtom()) {
                name=nameTerm.getName();
            }else if (nameTerm.isString()) {
                name=nameTerm.getString();
            }else{
                throw new RuleSessionCreateException("Invalid system term: system must have arity > 1");
            }
        }else{
            throw new RuleSessionCreateException("Invalid system term: it does not contains 'system' clause");
        }
        Term[] names=new Term[domains.size()+1];
        for(int i=0; i<domains.size();++i) {
            names[i]=(Term)domains.get(i);
        }
        names[domains.size()]=instance_.getTermFactory().createAtom(name);
        try {
          Term nameTerm=instance_.getTermFactory().createTerm("_name",names);
          instance_.getSysSystem().reduce(systemTerm);
          termSystem_=instance_.resolveSystem(nameTerm);
        }catch(TermWareException ex){
            throw new RuleSessionCreateException("Can't perform creation of term system",ex);
        }
        handleSystemProperties();
    }
    
    public RuleExecutionSetMetadata getRuleExecutionSetMetadata() throws InvalidRuleSessionException, RemoteException {
      return ruleExecutionSet_.getMetadata();
    }

    private void handleInstanceProperties() throws RuleSessionCreateException
    {
      if (properties_!=null) {  
        Object o=properties_.get("RoundingMode");
        if (o!=null) {
          if (o instanceof Integer) {
             Integer i=(Integer)o;
             instance_.setRoundingMode(i.intValue());
          }else{
              throw new RuleSessionCreateException("Invalid property 'RoundingMode' - must be Integer");
          }          
        }
        o=properties_.get("DecimalScale");
        if (o!=null) {
          if (o instanceof Integer) {
              Integer i=(Integer)o;
              instance_.setDecimalScale(i.intValue());
          }else{
              throw new RuleSessionCreateException("Invalid property 'DecimalScale' - must be Integer");              
          }
        }
      }
    }

    
    private void handleSystemProperties()
    {
        // nothing yet.
    }

    protected void addOutputIfChecked(TermSystem system, Term t, ObjectFilter objectFilter, List retval) throws TermWareException
    {
      if (!t.isNil()) {
        Object o = system.getInstance().getTypeConversion().getAsObject(t);
         if (objectFilter!=null) {
            o=objectFilter.filter(o);   
         }
         if (o!=null) {
             retval.add(o);
         }
      }
    }

    public TermWareRuleExecutionSet getTermWareRuleExecutionSet()
    {
      return ruleExecutionSet_;  
    }
    
    public TermSystem getTermSystem()
    {
     return termSystem_;      
    }

    public Term applyPassVia(Term t) throws TermWareException
    {          
      Term passViaTerm = TermHelper.getAttribute(ruleExecutionSet_.getSystemTerm(),"passVia");
      if (passViaTerm.isString()) {
          String passVia = passViaTerm.getString();
          if (!passVia.equals("list")) {
             t=termSystem_.getInstance().getTermFactory().createTerm(passVia,t);
          }
      }
      return t;
    }

    
    private TermSystem       termSystem_=null;
    private TermWareInstance instance_=null;
    
    private TermWareRuleExecutionSet ruleExecutionSet_;
    private TermWareRuleRuntime      runtime_;
    private Map                      properties_;
    
    
    
    
}
