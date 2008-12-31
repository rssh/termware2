/*
 * SetTransformerIterator.java
 *
 */

package ua.gradsoft.termware.util;

import java.util.*;

import ua.gradsoft.termware.ITermTransformer;

/**
 *@deprecated
 * @author  Ruslan Shevchenko
 */
public class SetTransformerIterator implements Iterator<ITermTransformer>
{
    
    
    /** Creates a new instance of SetTransformerIterator */
    SetTransformerIterator(Map<String,TransformersSet> sets) {
        it1=sets.entrySet().iterator();            
    }
    
    public boolean hasNext() {
        if (it2==null) {
            return reinitIt2();
        }else if(it2.hasNext()){
            return true;
        }else{
            return reinitIt2();
        }
    }
    
    public ITermTransformer next() {
        return it2.next();
       
    }
    
    public void remove() {
        it2.remove();
    }
    
    private boolean reinitIt2()
    {
       if (it1.hasNext()) {            
                Map.Entry<String,TransformersSet> me=it1.next();
                TransformersSet set=me.getValue();
                it2=set.iterator();
                if (!it2.hasNext()) return reinitIt2();
                return true;
       }else{
           return false;
       }
    }
    
    private Iterator<Map.Entry<String,TransformersSet> > it1;
    private Iterator<ITermTransformer> it2=null;
    
}
