/*
 * AbstractBuildinTransformer.java
 *
 */

package ua.gradsoft.termware;



import ua.gradsoft.termware.annotations.TransformerDescription;
import ua.gradsoft.termware.annotations.TransformerName;

/**
 * Abstract transformer for build-in operations.
 * @author  Ruslan Shevchenko
 */
public abstract class AbstractBuildinTransformer implements ITermTransformer 
{
    
   
    /**
     * return source. in our case return string 'build-in'.
     */
    public String getSource()
    { return "build-in"; }
   
    /**
     * get name of transformer.
     *Default behaviour -- return the value of annotation @TransformerName binded to this.getClass().
     *@see ua.gradsoft.termware.annotations.TransformerName
     */
    public String getName()
    { 
        TransformerName name = this.getClass().getAnnotation(TransformerName.class);
        return name.value();        
    }

    /**
     * get description of transformer.
     *Default behaviour -- return the value of annotation @TransformerDescription binded to this.getClass().
     *@see ua.gradsoft.termware.annotations.TransformerDescription
     */    
    public String getDescription()
    { return ((TransformerDescription)(this.getClass().getAnnotation(TransformerDescription.class))).value(); }
    
    
}
