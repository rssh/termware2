/*
 * EmptyTransformerIterator.java
 *
 */

package ua.gradsoft.termware.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import ua.gradsoft.termware.ITermTransformer;

/**
 *Empty transformer iterator
 * @author  Ruslan Shevchenko
 */
public final class EmptyTransformerIterator implements Iterator<ITermTransformer>
{
    
    
    public boolean hasNext() {
        return false;
    }
    
    public ITermTransformer next() {
        throw new NoSuchElementException();
    }
    
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
}
