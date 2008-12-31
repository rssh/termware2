/*
 * TermWareRuleExecutionSetProvider.java
 *
 */

package ua.gradsoft.termware.jsr94.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.TreeMap;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import javax.rules.admin.RuleExecutionSetProvider;
import org.w3c.dom.DOMException;
import ua.gradsoft.termware.IParser;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.jsr94.XMLUtil;

/**
 *TermWare implementation of RuleExecutionSetProvider.
 *(transform serializable resouces to local and delegate all
 *actual work to LocalRuleExecutionSetProvider)
 * @author Ruslan Shevchenko
 */
public class TermWareRuleExecutionSetProvider implements RuleExecutionSetProvider
{
    
    /** Creates a new instance of TermWareRuleExecutionSetProvider */
    public TermWareRuleExecutionSetProvider(TermWareRuleAdministrator administrator) 
    {
      administrator_=administrator;  
    }

    public RuleExecutionSet createRuleExecutionSet(Serializable serializable, Map map) throws RuleExecutionSetCreateException, RemoteException {
        return administrator_.getLocalRuleExecutionSetProvider(map).createRuleExecutionSet(serializable, map);
    }

    public RuleExecutionSet createRuleExecutionSet(String str, Map map) throws RuleExecutionSetCreateException, IOException, RemoteException {
        try {
          Object o = null;
          if (map!=null) {
            o = map.get("ParseXML");
          } 
          boolean useXML=false;
          if (o!=null) {
              if (o instanceof Boolean) {
                  Boolean bo = (Boolean)o;
                  useXML=bo.booleanValue();       
              }else{
                  throw new RuleExecutionSetCreateException("'ParseXML' property must be boolean");
              }
          } else {
              // by default -> true
              useXML = true;
          }
          Term t=null;
          if (useXML) {
            StringReader stringReader = new StringReader(str);  
            IParser tp=TermWare.getInstance().getParserFactory("XMLTermWare").createParser(stringReader,"<in>",
                                                                                    TermWare.getInstance().getTermFactory().createNil(),TermWare.getInstance());
            t = tp.readTerm();           
          }else{
              t=TermWare.getInstance().getTermFactory().createParsedTerm(str);
          }
          return administrator_.getLocalRuleExecutionSetProvider(map).createRuleExecutionSet(t, map);
        }catch(TermWareException ex){
            throw new RuleExecutionSetCreateException("can't parse term.",ex);
        }
    }

    public RuleExecutionSet createRuleExecutionSet(org.w3c.dom.Element element, Map map) throws RuleExecutionSetCreateException, RemoteException {
      try {  
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        PrintStream tmpOut = new PrintStream(bs);
        XMLUtil.print(element, tmpOut,0);
        String str = bs.toString();        
        if (map==null) {
            map = new TreeMap();
        }
        map.put("ParseXML",new Boolean(true));
        return createRuleExecutionSet(str,map);
      }catch(IOException ex){
          throw new RuleExecutionSetCreateException("exception during reading of Node context:",ex);
      }catch(DOMException ex){
          throw new RuleExecutionSetCreateException("exception during reading of Node context:",ex);
      }
    }
        
    private TermWareRuleAdministrator administrator_;
    
}
