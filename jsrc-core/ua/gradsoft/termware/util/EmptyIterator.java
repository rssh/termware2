/*
 * EmptyIterator.java
 *
 * Created  6, 2005, 1:24
 *
 * Copyright (c) 2002-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *Singleton for empty iterator
 * @author Ruslan Shevchenko
 */
public final class EmptyIterator<X> implements Iterator<X>
{
    
    private EmptyIterator()
    {}
    
    public final boolean hasNext()
    { return false; }
    
    public final X next()
    {
        throw new NoSuchElementException();
    }
    
    public final void remove()
    {
        throw new UnsupportedOperationException();
    }
    
    public static final EmptyIterator<?> getInstance()
    { return instance_; }
    
    static final EmptyIterator<?> instance_=new EmptyIterator();
    
}
