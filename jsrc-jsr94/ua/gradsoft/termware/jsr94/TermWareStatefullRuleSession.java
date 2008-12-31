/*
 * TermWareStatefullRuleSession.java
 *
 */

package ua.gradsoft.termware.jsr94;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.rules.Handle;
import javax.rules.InvalidHandleException;
import javax.rules.InvalidRuleSessionException;
import javax.rules.ObjectFilter;
import javax.rules.RuleRuntime;
import javax.rules.RuleSessionCreateException;
import javax.rules.StatefulRuleSession;
import javax.rules.admin.RuleExecutionSet;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareSymbols;
import ua.gradsoft.termware.exceptions.AssertException;
import ua.gradsoft.termware.jsr94.admin.TermWareRuleExecutionSet;

/**
 *Implementation of stateful rule session
 * @author Ruslan Shevchenko
 */
public class TermWareStatefullRuleSession extends TermWareRuleSession implements StatefulRuleSession
{
    
    TermWareStatefullRuleSession(TermWareRuleRuntime runtime,RuleExecutionSet executionSet,Map properties) throws RuleSessionCreateException
    {
        super(runtime,(TermWareRuleExecutionSet)executionSet,properties);
        currentHandleIndex_=0;
        objects_=new HashMap();
        //Term systemTerm=getSystemTerm();        
    }
   
   
    public static class IntHandle implements Handle
    {
      public int value;  
      
      public int hashCode()
      { return value; }
      
      public boolean equals(Object o)
      {
          if (o instanceof IntHandle) {
              return hashCode()==o.hashCode();
          }else{
              return false;
          }
      }
      
      public IntHandle(int x)
      { value=x; }
      
      public String toString()
      {
          return "h"+Integer.toString(value);
      }
      
    }
    
    public int getType() throws java.rmi.RemoteException, javax.rules.InvalidRuleSessionException {
        return RuleRuntime.STATEFUL_SESSION_TYPE;
    }

    public void release() throws RemoteException, InvalidRuleSessionException {
        objects_.clear();
    }

    public Handle addObject(Object obj) throws RemoteException, InvalidRuleSessionException {
        IntHandle h=new IntHandle(currentHandleIndex_++);
        objects_.put(h,obj);
        return h;
    }

    public List addObjects(List list) throws RemoteException, InvalidRuleSessionException {
        Iterator it=list.iterator();
        ArrayList retval=new ArrayList();
        while(it.hasNext()) {
            retval.add(addObject(it.next()));
        }
        return retval;
    }

    public boolean containsObject(Handle handle) throws RemoteException, InvalidRuleSessionException, InvalidHandleException 
    {
      if (handle instanceof IntHandle) {
          return objects_.containsKey(handle);
      }else{
          throw new InvalidHandleException("Invalid type of javax.rules.Handle");
      }  
    }

    public void executeRules() throws RemoteException, InvalidRuleSessionException 
    {
     try {   
      TermSystem system=getTermSystem();
      Iterator it=objects_.entrySet().iterator();
      Term inputTerm=system.getInstance().getTermFactory().createNIL();
      while(it.hasNext()){
          Map.Entry me=(Map.Entry)it.next();
          Object o=me.getValue();
          Term t=system.getInstance().getTypeConversion().adopt(o);
          t=TermHelper.setAttribute(t, "handle",system.getInstance().getTermFactory().createInt(((IntHandle)me.getKey()).value));          
          inputTerm=system.getInstance().getTermFactory().createTerm(TermWareSymbols.CONS_STRING,t,inputTerm);
      }
      inputTerm=TermHelper.reverseList(system.getInstance(), inputTerm);      
      
      inputTerm=applyPassVia(inputTerm);
      
      Term outputTerm=system.reduce(inputTerm);      
      HashMap newObjects=new HashMap();
      while(outputTerm.isComplexTerm() && !outputTerm.isNil()) {
          if (!outputTerm.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
              break;
          }
          if (outputTerm.getArity()!=2) {
              throw new AssertException("Arity of cons must be 2");
          }
          Term t=outputTerm.getSubtermAt(0);
          outputTerm=outputTerm.getSubtermAt(1);
          if (t.isNil()) {
              continue;
          }
          Term attr=TermHelper.getAttribute(t, "handle");          
          IntHandle intHandle;
          if (attr.isNil()) {
              intHandle=new IntHandle(currentHandleIndex_++);
          }else{
              intHandle=new IntHandle(attr.getInt());
          }  
          Object o=system.getInstance().getTypeConversion().getAsObject(t);
          newObjects.put(intHandle,o);
      }   
      if (!outputTerm.isNil()) {
          //if (newObjects.isEmpty()) {
              // return set is not list.                            
          //}
          Term attr=TermHelper.getAttribute(outputTerm, "handle");
          IntHandle intHandle;
          if (attr.isNil()) {
              intHandle=new IntHandle(currentHandleIndex_++);
          }else{
              intHandle=new IntHandle(attr.getInt());
          }  
          Object o=system.getInstance().getTypeConversion().getAsObject(outputTerm);
          newObjects.put(intHandle,o);          
      }
      objects_=newObjects;
     }catch(TermWareException ex){
         throw new InvalidRuleSessionException(ex.getMessage(),ex);
     }
    }

    public List getHandles() throws RemoteException, InvalidRuleSessionException {
       ArrayList retval = new ArrayList();
       retval.addAll(objects_.keySet());
       return retval;
    }

    public Object getObject(Handle handle) throws RemoteException, InvalidHandleException, InvalidRuleSessionException 
    {
      if (handle instanceof IntHandle) {
        Object retval=objects_.get(handle);
        return retval;
      }else{
         throw new InvalidHandleException("Invalid type of javax.rules.Handle");
      }
    }

    public List getObjects() throws RemoteException, InvalidRuleSessionException {
        Iterator it=objects_.entrySet().iterator();
        LinkedList retval=new LinkedList();
        while(it.hasNext()) {
            Map.Entry me=(Map.Entry)it.next();
            // TODO: may be we must store not ibjects, but pair object/boolean ?
            retval.add(me.getValue());            
        }
        return retval;
    }

    public List getObjects(ObjectFilter objectFilter) throws RemoteException, InvalidRuleSessionException 
    {
      Iterator it=objects_.entrySet().iterator();
      LinkedList retval=new LinkedList();
      while(it.hasNext()) {
          Map.Entry me=(Map.Entry)it.next();
          Object filteredObject=objectFilter.filter(me.getValue());
          if (filteredObject!=null){
              retval.add(filteredObject);
          }
      }
      return retval;
    }

    public void removeObject(Handle handle) throws RemoteException, InvalidHandleException, javax.rules.InvalidRuleSessionException 
    {
      if (handle instanceof IntHandle) {
          objects_.remove(handle);
      }else{
          throw new InvalidHandleException("invalid object handle:"+handle.toString());
      }    
    }

    public void reset() throws RemoteException, InvalidRuleSessionException {
        objects_.clear();
    }

    public void updateObject(Handle handle, Object obj) throws RemoteException, InvalidRuleSessionException, InvalidHandleException {
        if (!objects_.containsKey(handle)) {
            throw new InvalidHandleException("handle is invalid:"+handle.toString());
        }else{
            objects_.remove(handle);
            objects_.put(handle, obj);
        }
    }
    
    
    /**
     * keys of hashmap are handles, values are objects, passed to input.
     */
    private HashMap     objects_;
    private int         currentHandleIndex_;
            
    
    
}
