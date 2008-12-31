/*
 * TransformersStarIterator.java
 *
 * Created 6, 07, 2005, 1:41
 *
 *
 * Owner: Ruslan Shevchenko
 *
 * Copyright (c) 2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.util;

import java.util.Iterator;
import java.util.Map;
import ua.gradsoft.termware.ITermTransformer;

/**
 *Iterator of transformers, which walk throught all sets.
 * @author Ruslan Shevchenko
 */
public class TransformersStarIterator implements Iterator<ITermTransformer>
{

    /** Creates a new instance of SetTransformerIterator */
    TransformersStarIterator(Map<String,TransformersSet> sets) {
        itSets_=sets.entrySet().iterator();            
    }
    
    public boolean hasNext() {
        if (itTransformers_==null) {
            return reinitItTransformers();
        }else if(itTransformers_.hasNext()){
            return true;
        }else{
            return reinitItTransformers();
        }
    }
    
    /**
     *get next transformer
     *@return next transformer in star
     */
    public ITermTransformer next() {
        return itTransformers_.next();       
    }
    
    public void remove() {
        itTransformers_.remove();
    }
    
    private boolean reinitItTransformers()
    {
       if (itSets_.hasNext()) {
                Object o=itSets_.next();
                Map.Entry me=(Map.Entry)o;
                TransformersSet set=(TransformersSet)me.getValue();
                itTransformers_=set.iterator();
                if (!itTransformers_.hasNext()) return reinitItTransformers();
                return true;
       }else{
           return false;
       }
    }
    
    private Iterator<Map.Entry<String,TransformersSet> > itSets_;
    private Iterator<ITermTransformer> itTransformers_=null;
    
    
}
