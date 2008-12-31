/*
 * Attributed.java
 *
 * Copyright (c) 2004-2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware;

import java.util.Map;

/**
 * Interface for entities, which can holds attributes.
 * @author Ruslan Shevchenko
 */
public interface Attributed {
    
    /**
     *set attribute with name <code> name </code> and value <code> value </code>.
     *If <code> value </code> is NIL, then attribute is erased from set.
     */
    void setAttribute(String name,Term value) ;

    /**
     *get attribute with name <code> name </code>.
     * When such attribute does not exists -- return NIL.
     */
    public  Term getAttribute(String name) ;
      
    /**
     * return map, where keys are attribute names, value - values of attributes.
     */
    public  Map<String,Term> getAttributes();
    
}
