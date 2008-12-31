/*
 * TransformerName.java
 *
 */

package ua.gradsoft.termware.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *This annotation is used to set name of transformer.
 * @author rssh
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TransformerName {
    
    /**
     * Creates a new instance of TransformerName
     */
    public String value();    
    
}
