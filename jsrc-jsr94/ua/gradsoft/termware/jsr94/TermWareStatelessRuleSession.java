/*
 * TermWareStatelessRuleSession.java
 *
 */

package ua.gradsoft.termware.jsr94;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.rules.InvalidRuleSessionException;
import javax.rules.ObjectFilter;
import javax.rules.RuleRuntime;
import javax.rules.RuleSessionCreateException;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.RuleExecutionSet;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareSymbols;
import ua.gradsoft.termware.exceptions.AssertException;
import ua.gradsoft.termware.jsr94.admin.TermWareRuleExecutionSet;

/**
 *
 * @author Ruslan Shevchenko
 */
public class TermWareStatelessRuleSession extends TermWareRuleSession implements StatelessRuleSession
{
    
    /** Creates a new instance of TermWareStatelessRuleSession */
    public TermWareStatelessRuleSession(TermWareRuleRuntime runtime,RuleExecutionSet executionSet,Map properties)  throws RuleSessionCreateException
    {
       super(runtime,(TermWareRuleExecutionSet)executionSet,properties); 
    }


    public int getType() throws RemoteException, InvalidRuleSessionException {
        return RuleRuntime.STATELESS_SESSION_TYPE;
    }

    public void release() throws RemoteException, InvalidRuleSessionException {
    }

    public List executeRules(List list, ObjectFilter objectFilter) throws InvalidRuleSessionException, RemoteException 
    {        
     try {      
      TermSystem system=getTermSystem();
      Term inputTerm=system.getInstance().getTermFactory().createNIL();
      Iterator it=list.iterator();
      while(it.hasNext()) {
          Term t=system.getInstance().getTypeConversion().adopt(it.next());
          inputTerm=system.getInstance().getTermFactory().createTerm("cons",t,inputTerm);
      }
      inputTerm=TermHelper.reverseList(system.getInstance(),inputTerm);      
      inputTerm=applyPassVia(inputTerm);
      
     // if (getTermWareRuleExecutionSet().)
      Term outputTerm=system.reduce(inputTerm);
      ArrayList retval=new ArrayList();
      while(!outputTerm.isNil() && outputTerm.isComplexTerm() && outputTerm.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {     
          if (outputTerm.getArity()==2) {
              Term t=outputTerm.getSubtermAt(0);
              addOutputIfChecked(system,t,objectFilter,retval);
              outputTerm = outputTerm.getSubtermAt(1);
          }else{
              throw new AssertException("arity of cons must be 2");
          }
      }   
      if (!outputTerm.isNil()) {
          if (retval.isEmpty()) {
              // it means, that we transform term not to list.
              // so, just return one.
              addOutputIfChecked(system,outputTerm,objectFilter,retval);
          }else{
              // end of list is not nil.
              //  impossible, but be patient
              addOutputIfChecked(system,outputTerm,objectFilter,retval);
          }
      }
      return retval;
     }catch(TermWareException ex){
         // TODO: use some tunable log instead.
         ex.printStackTrace(getTermSystem().getEnv().getLog());                 
         throw new InvalidRuleSessionException(ex.getMessage(),ex);
     }
    }
    

    public List executeRules(List list) throws InvalidRuleSessionException, RemoteException 
    {
      return executeRules(list,null);  
    }
    
    
}
