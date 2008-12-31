/*
 * TermWareRule.java
 *
 */

package ua.gradsoft.termware.jsr94.admin;

import java.util.HashMap;
import javax.rules.admin.Rule;
import ua.gradsoft.termware.ITermTransformer;


/**
 *Implementation of JSR94 Rule metadata.
 * @author Ruslan Shevchenko
 */
public class TermWareRule implements Rule {
    
    /** Creates a new instance of TermWareRule */
    TermWareRule(ITermTransformer transformer) {
        transformer_=transformer;
        properties_=null;
    }
    
    /**
     * return name of transformer.
     */
    public String getName() {
        return transformer_.getName();
    }
    
    
    /**
     * return description of transformer.
     */
    public String getDescription() {
        return transformer_.getDescription();
    }
    
    
    /**
     * get previously setted property
     */
    public Object getProperty(Object obj) {
        if (properties_==null) {
          return null;
        }else{
          return properties_.get(obj);
        }
    }
    
    /**
     * set property
     */
    public void setProperty(Object obj, Object obj1) {
        if (properties_==null) {
            properties_ = new HashMap();
        }
        properties_.put(obj,obj1);
    }
    
    private ITermTransformer transformer_;
    private HashMap properties_;
    
}
