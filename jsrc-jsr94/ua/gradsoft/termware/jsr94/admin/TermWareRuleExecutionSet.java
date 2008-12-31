/*
 * TermWareRuleExecutionSet.java
 *
 */

package ua.gradsoft.termware.jsr94.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.rules.RuleExecutionSetMetadata;
import javax.rules.RuleSessionCreateException;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import ua.gradsoft.termware.ITermTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.jsr94.TermWareRuleRuntime;
import ua.gradsoft.termware.jsr94.TermWareStatelessRuleSession;
import ua.gradsoft.termware.util.TransformersStar;

/**
 * RuleExecutionSet of termware rules.
 *  (contans sytem definition)
 */
public class TermWareRuleExecutionSet implements RuleExecutionSet
{
    
    
    public class Metadata implements RuleExecutionSetMetadata
    {
      public String getName()
      { return name_; }
      
      public String getDescription()
      { return description_; }
      
      public String getUri()
      { return uri_; }
      
    }
    
    /** Creates a new instance of TermWareRuleExecutionSet */
    public TermWareRuleExecutionSet(Term systemTerm, String name, String uri, String description) 
                                                                            throws RuleExecutionSetCreateException
    {
        systemTerm_=systemTerm;
        name_=name;
        uri_=uri;
        description_=description;
        defaultObjectFilter_=null;
        try {
          rules_=getRulesInternal();
        }catch(RuleSessionCreateException ex){
            Throwable xex=ex.getCause();
            if (xex instanceof Exception){
               throw new RuleExecutionSetCreateException(ex.getMessage(),(Exception)xex);
            }else{
               throw new RuleExecutionSetCreateException(ex.getMessage(),ex); 
            }            
        }
        properties_ = new HashMap();
    }

    public String getDefaultObjectFilter() {
        return defaultObjectFilter_;
    }

    public String getDescription() {
        return description_;
    }

    public String getName() {
        return name_;
    }

    public List getRules()
    { return rules_; }
    
    /**
     * get rules, creating trmporary session.
     */
    private List getRulesInternal() throws RuleSessionCreateException
    {
      ArrayList retval=new ArrayList(); 
      TermWareStatelessRuleSession tmpSession = new TermWareStatelessRuleSession(runtime_,this,null);
 
      TransformersStar ts=tmpSession.getTermSystem().getStrategy().getStar();     
      Iterator it=ts.iterator();
      while(it.hasNext()) {
          ITermTransformer current=(ITermTransformer)it.next();
          retval.add(new TermWareRule(current));
      }
      return retval;
    }

    public void setDefaultObjectFilter(String str) {
        defaultObjectFilter_=str;
    }
    
    
    public Object getProperty(Object obj) {
        return properties_.get(obj);
    }

    
    public void setProperty(Object obj, Object obj1) {
        properties_.put(obj,obj1);
    }
        
    
    public Metadata getMetadata()
    {
      if (metadata_==null) {
          metadata_=new Metadata();
      }
      return metadata_;
    }
    
    public Term  getSystemTerm()
    { return systemTerm_; }
    
    String getUri()
    { return uri_; }
    
    void  setUri(String uri)
    { uri_=uri; }
    
    private String     name_;
    private String     uri_;
    private String     description_;
    private HashMap    properties_;
    private Term       systemTerm_;
    private TermWareRuleRuntime runtime_;
    private List       rules_;
        
    private String     defaultObjectFilter_;
    
    private transient  Metadata metadata_ = null;
    
}

