/*
 * NullFacts.java
 *
 */

package ua.gradsoft.termware;

import java.util.Collection;



/**
 * NullFacts - dummy facts.
 */
public class NullFacts implements IFacts
{
    
    /** Creates a new instance of NullFacts */
    public NullFacts() {
    }
    
    /**
     *@return false
     */
    public boolean check(Term t, TransformationContext ctx) throws TermWareException {
        return false;
    }
    
    /**     
     * @return false
     */
    public boolean isLoggingMode() {
        return false;
    }
    
    /**
     * do nothing.   
     **/
    public void set(Term t, TransformationContext ctx) throws TermWareException {
    }

    /**
     * do nothing.   
     **/    
    public void setLoggedEntity(String s) {
    }

    /**
     * do nothing.   
     **/        
    public void setLoggingMode(boolean mode) {
    }
    
    /**
     * do nothing.   
     **/        
    public void unsetLoggedEntity(String s) {
    }
     
    /**
     * do nothing.   
     **/            
    public void setLoggedEntities(Collection<String> s)
    {        
    }

    /**
     * do nothing.   
     **/            
    public void clearLoggedEntities()
    {        
    }
    
    
}
