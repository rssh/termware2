/*
 * TermWareLocalRuleExecutionSetProvider.java
 *
 */

package ua.gradsoft.termware.jsr94.admin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import ua.gradsoft.termware.IParser;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.ConversionException;


/**
 *TermWare implementation of LocalRuleExecutionSetProvider
 * @author Ruslan Shevchenko
 */
public class TermWareLocalRuleExecutionSetProvider implements LocalRuleExecutionSetProvider {
    
    /** Creates a new instance of TermWareLocalRuleExecutionSetProvider */
    TermWareLocalRuleExecutionSetProvider(TermWareRuleAdministrator administrator) {
        administrator_=administrator;
    }

    
    public  RuleExecutionSet createRuleExecutionSet(Object ast,Map properties) throws RuleExecutionSetCreateException
    {
        // object is a instance of term
        Term t=null;
        if (ast instanceof Term) {
            t=(Term)ast;
        }else{
            throw new RuleExecutionSetCreateException("supplied object is not a instance of ua.gradsoft.TermWare.Term");
        }
        if (t.isComplexTerm() && t.getName().equals("domain")) {                       
            
            Term domainNameTerm = t.getSubtermAt(0);
            String domainString;
            if (domainNameTerm.isAtom()) {
                domainString=domainNameTerm.getName();
            }else if (domainNameTerm.isString()) {
                domainString=domainNameTerm.getString();
            }else{
                throw new RuleExecutionSetCreateException("domain name must be a string");
            }
            if (properties!=null) {
              Object svDomainProperty = properties.get("domain");
              if (!domainString.equals("root")) {
                if (svDomainProperty==null) {
                  properties.put("domain",domainString);
                }else{
                  properties.put("domain", (String)svDomainProperty+"/"+domainString);
                }
              }
            }else{
                properties = new HashMap();
                if (!domainString.equals("root")) {
                    properties.put("domain",domainString);
                }                
            }
        
            RuleExecutionSet retval=null;
            for(int i=1;i<t.getArity();++i) {
               RuleExecutionSet current=createRuleExecutionSet(t.getSubtermAt(i),properties);
               if (retval==null) {
                   retval=current;
               }
            }
            if (retval==null) {
                throw new RuleExecutionSetCreateException("system term is not found in supplied domain");
            }
            return retval;
        } else if (t.isComplexTerm() && t.getName().equals("system")) {
            Term nameTerm=t.getSubtermAt(0);
            String name=null;
            if (nameTerm.isAtom()) {
                name=nameTerm.getName();
            }else if (nameTerm.isString()) {
                name=nameTerm.getString();
            }else{
                throw new RuleExecutionSetCreateException("Invalid system term: name must be atom or string");
            }
            
            Object uri=null;
            if (properties!=null) {
              uri=properties.get("uri");
              if (uri==null) {
                Object domainName = properties.get("domain");
                if (domainName!=null) {
                    uri=(String)domainName+"/"+name;
                }else{
                   uri=name;
                }
              }
            }else{
              uri=name;  
            }
            Term descriptionTerm;
        //    try {
              descriptionTerm=TermHelper.getAttribute(t, "description");
        //    }catch(TermWareException ex){
        //        throw new RuleExecutionSetCreateException("can't get description attribute",ex);                
        //    }
            
            Object description=null;
            if (properties!=null) {
              description=properties.get("description");
            }
            if (description==null) {
              if (descriptionTerm.isAtom()) {
                  description=descriptionTerm.getName();
              }else if(descriptionTerm.isString()) {
                  description=descriptionTerm.getString();
              }else{
                  try {
                     description=descriptionTerm.getAsString(TermWare.getInstance());
                  } catch (ConversionException ex) {
                      throw new RuleExecutionSetCreateException("can't convert description attribute",ex);
                  }
              }
            }            
                                    
            return new TermWareRuleExecutionSet(t,name,(String)uri,(String)description);
        }else{
            try {
              throw new RuleExecutionSetCreateException("supplied object is not domain or system definition term :"+TermHelper.termToPrettyString(t));
            }catch(TermWareException ex){
               throw new RuleExecutionSetCreateException("supplied object is not domain or system definition term :error during print :"+ex.getMessage()); 
            }
        }
    }
    
    public RuleExecutionSet createRuleExecutionSet(InputStream inputStream, Map map) throws RuleExecutionSetCreateException, IOException 
    {
      try {  
        IParser tp=TermWare.getInstance().getParserFactory("XMLTermWare").createParser(new InputStreamReader(inputStream,Charset.forName("UTF-8")),"<in>",
                                                                                    TermWare.getInstance().getTermFactory().createNil(),TermWare.getInstance());
        Term t = tp.readTerm();
        return createRuleExecutionSet(t,map);
      }catch(TermWareException ex){
          throw new RuleExecutionSetCreateException("can't create term from inputStream:"+ex.getMessage(),ex);
      }
    }
    
    public RuleExecutionSet createRuleExecutionSet(Reader reader, Map map) throws RuleExecutionSetCreateException, IOException {
      try { 
        IParser tp=TermWare.getInstance().getParserFactory("XMLTermWare").createParser(reader,"<in>",TermWare.getInstance().getTermFactory().createNil(),TermWare.getInstance());
        Term t = tp.readTerm();
        return createRuleExecutionSet(t,map);
      }catch(TermWareException ex){
          throw new RuleExecutionSetCreateException("can't create term from reader",ex);
      }
    }
    
    
    private TermWareRuleAdministrator administrator_;        
    
}
