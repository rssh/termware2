/*
 * TermSAXContentHandler.java
 *
 * Created 24, 07 2005, 7:28
 *
 * Copyright (c) 2002-2005 Ruslan Shevchenko
 * All Rights Reserved
 */


package ua.gradsoft.termware.parsers.xml.terms;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareInstance;
import ua.gradsoft.termware.exceptions.AssertException;
import ua.gradsoft.termware.parsers.xml.generic.XMLParseUtil;

/**
 *
 * @author Ruslan Shevchenko
 */
public class TermSAXContentHandler extends DefaultHandler
{
    
    /** Creates a new instance of TermSAXContentHandler */
    public TermSAXContentHandler(TermWareInstance instance) {     
        instance_=instance;
        terms_=new LinkedList<Term>();
        domainsStack_=new LinkedList<DomainRecord>();
    }
    
    public void setDocumentLocator(Locator locator)
    {        
        super.setDocumentLocator(locator);
        locator_=locator;  
    }
    
    public void startDocument()
    {
      state_=TOP;  
      currentText_=new StringBuffer();
    }
    
    public void endDocument() throws SAXException
    {
        if (state_!=TOP) {
            throw new SAXParseException("no close tag for root element",locator_);
        }
    }
    
    public void startElement(String uri,String localName,String qName,Attributes attributes)
                  throws SAXException
    {    
      String name = qName;
      switch(state_) {
          case TOP:
              if (name.equals("termware")) {
                  state_=TERMWARE;
              }else if (name.equals("termware-text")) {
                  state_=TERMWARE_TEXT;
              }else{
                  throw new SAXException("root element must be 'termware' or 'termware-text'");
              }
              break;
          case TERMWARE:
              if (name.equals("domain")) {
                  startDomain(attributes);
              }else{
                  throw new SAXException("termware must include domain entries");
              }
              break;
          case TERMWARE_TEXT:
              throw new SAXException("'terware-text' element must not contain child elements");              
          case DOMAIN:
              if (name.equals("domain")) {
                 startDomain(attributes);
              }else if (name.equals("system")) {
                 startSystem(attributes); 
              }else if (name.equals("system-text")) {
                  startSystemText(attributes);
              }else{
                  throw new SAXException("domain must include one of: 'domain','system','system-text'");
              }
              break;
          case SYSTEM:
              if (name.equals("rule-text")) {
                  state_=RULE_TEXT;
              }else if (name.equals("import-ruleset")) {
                  state_=IMPORT_RULESET;
              }else{
                  throw new SAXException("'system' must have only 'rule-text' and 'import-ruleset' child elements");
              }                                
              break;
          case SYSTEM_TEXT:
              throw new SAXException("'system-text' element must not contain child elements");
          case RULE_TEXT:
              throw new SAXException("'rule-text' element must not contain child elements");              
          default:
              break;
      }        
    }

    public void endElement(String uri,String localName,String qName)
                  throws SAXException
    {     
     String name=qName;   
     try {   
      switch(state_) {
          case TERMWARE:          
              // nothing.              
              if (name.equals("termware")) {
                  state_=TOP;
              }else{
                  throw new SAXException("open tag must be 'termware' instead "+name);
              }
              break;
          case TERMWARE_TEXT:
          { String lastPCData = currentText_.toString();            
            if (lastPCData!=null) {                 
                  Term t = instance_.getTermFactory().createParsedTerm(lastPCData);
                  terms_.add(t);
            }
            currentText_=new StringBuffer();
            state_=TOP;
          }          
          break;
          case DOMAIN:
          {
              DomainRecord dr = (DomainRecord)domainsStack_.removeLast();
              Term lastDomainTerm=dr.getDomainTerm();
              if (domainsStack_.isEmpty()) {
                  terms_.add(lastDomainTerm);
                  state_=TERMWARE;
              }else{
                  DomainRecord ldr=(DomainRecord)domainsStack_.getLast();
                  ldr.addSubterm(lastDomainTerm);
                  state_=DOMAIN;
              }
          }
          break;
          case SYSTEM:
              endSystem();
          break;
          case SYSTEM_TEXT:
          {              
              SystemRecordText sr=(SystemRecordText)currentSystemRecord_;
              sr.setSystemText(currentText_.toString());
              currentText_=new StringBuffer();
              endSystem();
          }
          break;
          case RULE_TEXT:
          {
              SystemRecordRules sr=(SystemRecordRules)currentSystemRecord_;
              sr.addRuleText(currentText_.toString());
              currentText_=new StringBuffer();
              state_=SYSTEM;
          }
          break;
          case IMPORT_RULESET:
              // nothing, we do all in start
              break;
          default:
              throw new SAXException("Invalid state");
      }  
     }catch(TermWareException ex){
         throw new SAXException("exception during text analysis",ex);
     }     
    }
    
    public void characters(char[] ch,int start, int length)
    {      
        currentText_.append(ch,start,length);     
    }
    
    public void ignorableWhitespace()
    {        
        switch(state_) {
            case TERMWARE_TEXT:
            case SYSTEM_TEXT:
            case RULE_TEXT:
                currentText_.append(" ");
                break;
            default:
                // do nothing.
                break;
        }     
    }
    
    
    public List getTerms()
    { return terms_; }
    
    private void  startDomain(Attributes attributes) throws SAXException
    {
      state_=DOMAIN;
      String name=attributes.getValue("name");
      if (name==null) {
         throw new SAXException("name attribute is mandatory for domain");
      }
      domainsStack_.addLast(new DomainRecord(name));        
    }
    
    private void startSystem(Attributes attributes) throws SAXException
    {
      state_=SYSTEM;
      getSystemAttributes(attributes,"system");
      
    }

    private void startSystemText(Attributes attributes) throws SAXException
    {
      state_=SYSTEM_TEXT;
      getSystemAttributes(attributes,"system-text");
    }

    private void getSystemAttributes(Attributes attributes, String elementName) throws SAXException
    {
      String name=attributes.getValue("name");
      if (name==null) {
                      throw new SAXException("name attribute is mandatory for element "+elementName);
      }
      String facts=attributes.getValue("facts");
      if (facts==null) {
                      throw new SAXException("facts attribute is mandatory for element "+elementName);
      }
      String strategy=attributes.getValue("strategy");
      if (strategy==null) {
                      throw new SAXException("strategy attribute is mandatory for element "+elementName);
      }
      currentSystemRecord_ = new SystemRecordRules(name, facts,strategy);    
      String debugMode=attributes.getValue("debug");
      if (debugMode!=null) {
          try {
              boolean v = XMLParseUtil.getBoolean(debugMode);
              currentSystemRecord_.setDebugMode(v);
          }catch(AssertException ex){
              throw new SAXException("debug attribute must be boolean value");
          }
      }
      String debugEntity=attributes.getValue("debug-entity");
      if (debugEntity!=null) {
          currentSystemRecord_.setDebugEntity(debugEntity);
      }
      String description=attributes.getValue("description");
      if (description!=null) {
          currentSystemRecord_.setDescription(description);
      }        
      String passVia=attributes.getValue("pass-via");
      if (passVia!=null) {
          currentSystemRecord_.setPassVia(passVia);
      }
    }
    
    private void endSystem() throws TermWareException
    {                    
               if (domainsStack_.isEmpty()) {                  
                   terms_.add(currentSystemRecord_.getSystemTerm());                   
                   state_=TERMWARE;
               }else{                   
                   DomainRecord dr=(DomainRecord)domainsStack_.getLast();
                   dr.addSubterm(currentSystemRecord_.getSystemTerm());
                   state_=DOMAIN;
               }      
    }
    
    private abstract class SystemRecordBase
    {
        SystemRecordBase(String name,String facts,String strategy)
        {
          name_=name;
          facts_=facts;
          strategy_=strategy;
          debugMode_=false;
          debugEntity_=null;
          description_=null;
          passVia_=null;
        }

        public String getDescription()
        { return description_; }
        
        public void setDescription(String description)
        { description_=description; }
        
       public String getDebugEntity()
        { return debugEntity_; }
        
        public void  setDebugEntity(String debugEntity)
        { debugEntity_=debugEntity; }

        public boolean getDebugMode()
        { return debugMode_; }
        
        public void setDebugMode(boolean debugMode)
        { debugMode_=debugMode; }
        
        public String getPassVia()
        { return passVia_; }
        
        public void   setPassVia(String passVia)
        { passVia_=passVia; }
        
        public Term getSystemTerm() throws TermWareException
        {
          Term systemTerm = getSystemTermInternal();
          if (debugMode_) {
              systemTerm=TermHelper.setAttribute(systemTerm,"debugMode",instance_.getTermFactory().createBoolean(true));
              if (debugEntity_!=null) {
                  systemTerm=TermHelper.setAttribute(systemTerm,"debugEntity",instance_.getTermFactory().createString(debugEntity_));
              }
          }          
          if (description_!=null) {
              systemTerm=TermHelper.setAttribute(systemTerm,"description",instance_.getTermFactory().createString(description_));
          }
          if (passVia_!=null) {
              systemTerm=TermHelper.setAttribute(systemTerm,"passVia",passVia_);
          }          
          
          return systemTerm;
        }
                
        
        public abstract Term getSystemTermInternal() throws TermWareException;
        
        protected String name_;
        protected String facts_;
        protected String strategy_;
        
        protected boolean   debugMode_;
        protected String    debugEntity_;
        protected String    description_;
        
        protected String    passVia_;

    }
    
    private class SystemRecordText extends SystemRecordBase
    {
        SystemRecordText(String name,String facts,String strategy)
        {
          super(name,facts,strategy);
        }

        public Term getSystemTermInternal() throws TermWareException
        {
           Term[] body=new Term[4];
           String rulesetString = "ruleset("+systemText_+")";
           Term rulesetTerm = instance_.getTermFactory().createParsedTerm(rulesetString);
           body[0]=instance_.getTermFactory().createAtom(name_);
           body[1]=instance_.getTermFactory().createParsedTerm(facts_);
           body[2]=rulesetTerm;
           body[3]=instance_.getTermFactory().createParsedTerm(strategy_);
           Term retval=instance_.getTermFactory().createTerm("system",body);
           return retval;
        }
        
        public void setSystemText(String systemText)
        {
          systemText_=systemText;  
        }
        
        public String getSystemText()
        { return systemText_; }
        
        private String systemText_;
    }
    
    private class SystemRecordRules extends SystemRecordBase
    {
        SystemRecordRules(String name,String facts,String strategy)
        {
          super(name,facts,strategy);
          rules_=new ArrayList<Term>();
          importRulesets_=new ArrayList<String>();
        }
        
        
        public void addRuleText(String ruleText)  throws TermWareException
        {
          Term t=instance_.getTermFactory().createParsedTerm(ruleText);  
          rules_.add(t);
        }
        
        public void addImport(String importSystem)
        {
            importRulesets_.add(importSystem);
        }
        
        
        
        public Term getSystemTermInternal() throws TermWareException
        {
         try {   
          Term[] body = new Term[4];
          body[0]=instance_.getTermFactory().createAtom(name_);
          body[1]=instance_.getTermFactory().createParsedTerm(facts_);
          body[3]=instance_.getTermFactory().createParsedTerm(strategy_);
          int rulesetArity = rules_.size()+importRulesets_.size();
          Term[] rulesetBody = new Term[rulesetArity];         
          for(int i=0; i<importRulesets_.size(); ++i) {
              rulesetBody[i]=instance_.getTermFactory().createTerm("import",(String)importRulesets_.get(i));
          }
          for(int j=0; j<rules_.size();++j)
          {
              rulesetBody[j+importRulesets_.size()]=rules_.get(j);
          }
          body[2]=instance_.getTermFactory().createTerm("ruleset",rulesetBody);
          Term systemTerm=instance_.getTermFactory().createTerm("system",body);          
          return systemTerm;
         }catch(RuntimeException ex){
             ex.printStackTrace();
             throw ex;
         }
        }
        
        public List<Term> getRules()
        { return rules_; }
        
        public List<String> getImports()
        { return importRulesets_; }
        
        private ArrayList<Term> rules_;
        private ArrayList<String> importRulesets_;
        
    }
    
    private class DomainRecord
    {
        DomainRecord(String name)
        {
          name_=name;
          subterms_=new ArrayList<Term>();
        }
        
        public void addSubterm(Term term)
        {
          subterms_.add(term);  
        }
        
        public Term getDomainTerm() throws TermWareException
        {
          int arity=subterms_.size()+1;
          Term[] body = new Term[arity];
          body[0]=instance_.getTermFactory().createAtom(name_);
          for(int i=1; i<arity;++i) {
              body[i]=subterms_.get(i-1);
          }
          return instance_.getTermFactory().createComplexTerm("domain",body);
        }
        
        private String name_;
        private ArrayList<Term> subterms_;
    }
    
       
  //  private void printAttributes(Attributes attributes)
  //  {
  //      
  //  }
    
    
    private LinkedList<Term> terms_;
    
    
    public static final int TOP = 1;
    public static final int TERMWARE = 2;
    public static final int TERMWARE_TEXT = 3;
    public static final int DOMAIN = 4;
    public static final int SYSTEM_TEXT = 5;
    public static final int SYSTEM = 6;
    public static final int RULE_TEXT = 7;
    public static final int IMPORT_RULESET = 8;
    

    
    private int state_;
    
    private LinkedList<DomainRecord> domainsStack_;
    private SystemRecordBase currentSystemRecord_;    
    
    private TermWareInstance instance_;
    
    private StringBuffer currentText_;
    private Locator      locator_;
    
    
    
}
