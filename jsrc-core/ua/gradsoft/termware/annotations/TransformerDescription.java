/*
 * TransformerDescription.java
 *
 * Created on July 12, 2007, 2:50 AM
 *
 */

package ua.gradsoft.termware.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *Used to set description of transformer.
 *@see ua.gradsoft.termware.AbstractBuildinTransformer
 * @author rssh
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface TransformerDescription {

    public String value();
    
}
