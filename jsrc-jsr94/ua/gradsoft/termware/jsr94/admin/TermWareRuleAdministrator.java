/*
 * TermWareRuleAdministrator.java
 *
 */

package ua.gradsoft.termware.jsr94.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetProvider;

import ua.gradsoft.termware.jsr94.TermWareRuleRuntime;


/**
 * RuleAdministrator for termWare systems.
 * @author Ruslan Shevchenko
 */
public class TermWareRuleAdministrator implements RuleAdministrator
{
    
    /** Creates a new instance of TermWareRuleAdministrator */
    public TermWareRuleAdministrator(TermWareRuleRuntime runtime) {
        runtime_=runtime;
    }
    

    public RuleExecutionSetProvider getRuleExecutionSetProvider(Map map) 
    {
       synchronized(this) { 
        if (ruleExecutionSetProvider_==null) {
           ruleExecutionSetProvider_=new TermWareRuleExecutionSetProvider(this);    
        }
       }
       return ruleExecutionSetProvider_;
    }

    public LocalRuleExecutionSetProvider getLocalRuleExecutionSetProvider(Map map) {
        synchronized(this) {
            if (localRuleExecutionSetProvider_==null) {
                localRuleExecutionSetProvider_=new TermWareLocalRuleExecutionSetProvider(this);
            }
        }
        return localRuleExecutionSetProvider_;
    }

    public void registerRuleExecutionSet(String str, RuleExecutionSet ruleExecutionSet, Map map) 
    {
       synchronized(this) {
          if (ruleExecutionSets_==null) {
              ruleExecutionSets_= new HashMap();
          }
       }
       ((TermWareRuleExecutionSet)ruleExecutionSet).setUri(str);
       synchronized(ruleExecutionSets_){             
         ruleExecutionSets_.put(str,ruleExecutionSet);              
       }
    }

    public void deregisterRuleExecutionSet(String str, Map map) 
    {
        if (ruleExecutionSets_!=null) {
            synchronized(ruleExecutionSets_) {
               ruleExecutionSets_.remove(str);
            }
        }
    }
    
    public RuleExecutionSet findRuleExecutionSet(String str)
    {
        if (ruleExecutionSets_!=null) {
            synchronized(ruleExecutionSets_) {
              Object o = ruleExecutionSets_.get(str);
              if (o!=null) {
                return (RuleExecutionSet)o;
              }
            }
        }
        return null;
    }
    
    /**
     *Retrieves a List of the URIs that currently have RuleExecutionSets associated with them
     */
    public List getRegistrations()
    {
        List retval=new ArrayList();
        if (ruleExecutionSets_!=null) {
            synchronized(ruleExecutionSets_) {
              Iterator it=ruleExecutionSets_.entrySet().iterator();
              while(it.hasNext()){
                Map.Entry me = (Map.Entry)it.next();
                retval.add(me.getKey());                
              }
            }
        }
        return retval;
    }
    
    private HashMap ruleExecutionSets_;
    
    private transient TermWareRuleExecutionSetProvider      ruleExecutionSetProvider_=null;
    private transient TermWareLocalRuleExecutionSetProvider localRuleExecutionSetProvider_=null;
    private transient TermWareRuleRuntime  runtime_;
    
}
