/*
 * TermWareRuleRuntime.java
 *
 */

package ua.gradsoft.termware.jsr94;

import java.util.List;
import java.util.Map;
import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.RuleRuntime;
import javax.rules.RuleSession;
import javax.rules.RuleSessionCreateException;
import javax.rules.RuleSessionTypeUnsupportedException;
import javax.rules.admin.RuleExecutionSet;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareInstance;
import ua.gradsoft.termware.exceptions.AssertException;


/**
 *
 * @author Ruslan Shevchenko
 */
public class TermWareRuleRuntime implements RuleRuntime
{
    
    
    public TermWareRuleRuntime(RuleServiceProviderImpl serviceProvider)
    {
      serviceProvider_=serviceProvider;  
    }
    
    
    public RuleSession  createRuleSession(String uri, Map properties, int ruleSessionType) 
                                                            throws RuleExecutionSetNotFoundException,
                                                                   RuleSessionTypeUnsupportedException,
                                                                   RuleSessionCreateException
    {
      RuleExecutionSet executionSet = serviceProvider_.getTermWareRuleAdministrator().findRuleExecutionSet(uri);  
      if (executionSet==null) {
          throw new RuleExecutionSetNotFoundException("execution set not found:"+uri);
      }
      switch(ruleSessionType) {
          case RuleRuntime.STATEFUL_SESSION_TYPE:
              return new TermWareStatefullRuleSession(this,executionSet,properties);
          case RuleRuntime.STATELESS_SESSION_TYPE:
              return new TermWareStatelessRuleSession(this,executionSet,properties);
          default:
              throw new RuleSessionTypeUnsupportedException("Unknown session type:"+ruleSessionType);
      }  
    }
    
    
    TermSystem  createTermSystem(Term systemTerm,TermWareInstance instance) throws TermWareException
    {
        if (systemTerm.getArity()<1) {
            throw new AssertException("arity of system term must be greater than one");
        }
        Term nameTerm=systemTerm.getSubtermAt(0);
        String name;
        if (nameTerm.isAtom()) {
            name=nameTerm.getName();
        }else if (nameTerm.isString()) {
            name=nameTerm.getString();
        }else{
            throw new AssertException("name of system must be atom or string");
        }
        instance.sysReduce(systemTerm);
        return instance.resolveSystem(name);
    }
    
    /**
     * retrieve set of uri with registered RuleExecutionSets
     */
    public List getRegistrations()
    {
     return serviceProvider_.getTermWareRuleAdministrator().getRegistrations();   
    }
    
    public RuleServiceProviderImpl getTermWareRuleServiceProvider()
    { return serviceProvider_; }
 
    RuleServiceProviderImpl serviceProvider_;
   // TermWareInstance instance_;
 
}
