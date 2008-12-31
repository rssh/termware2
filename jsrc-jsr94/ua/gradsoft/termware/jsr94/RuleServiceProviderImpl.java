package ua.gradsoft.termware.jsr94;



import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.admin.RuleAdministrator;
import ua.gradsoft.termware.TermWareInstance;
import ua.gradsoft.termware.jsr94.admin.TermWareRuleAdministrator;



/**
 * Implemantation of jsr94 RuleServiceProvider
 */
public class RuleServiceProviderImpl extends RuleServiceProvider {
    
    
    private static TermWareRuleAdministrator  ruleAdministrator_=null;
    private static TermWareRuleRuntime        defaultRuleRuntime_=null;
    
    
    
    public RuleAdministrator  getRuleAdministrator()
    {
        if (ruleAdministrator_==null) {
            synchronized(this) {
                  if (ruleAdministrator_==null) {
                      ruleAdministrator_=new TermWareRuleAdministrator(getTermWareRuleRuntime());
                  }
            }
        }
        return ruleAdministrator_;
    }

    
    public RuleRuntime getRuleRuntime() throws javax.rules.ConfigurationException {
        return getTermWareRuleRuntime();
    }
    
    public TermWareRuleRuntime getTermWareRuleRuntime()
    {
         if (defaultRuleRuntime_==null) {
             synchronized(this) {
                 if (defaultRuleRuntime_==null) {
                     defaultRuleRuntime_=new TermWareRuleRuntime(this);
                 }
             }
         }        
         return defaultRuleRuntime_;
    }

    // extensions.
    
    public TermWareRuleAdministrator getTermWareRuleAdministrator()
    {
        return (TermWareRuleAdministrator)getRuleAdministrator();
    }
    
}


