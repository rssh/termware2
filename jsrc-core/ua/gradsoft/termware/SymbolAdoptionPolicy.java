/*
 * SymbolAdoptionPolicy.java
 *Part of TermWare
 *(C) Grad-Soft Ltd, 2003-2004
 *$Id: SymbolAdoptionPolicy.java,v 1.2 2008-01-13 01:09:33 rssh Exp $
 */

package ua.gradsoft.termware;

/**
 *SymbolAdoptionPolicy define constant wich determinate relations
 *of symbols (i. e. names of terms) and global termware symbol table.
 * This adoption policy is used for creation of terms.
 */
public enum SymbolAdoptionPolicy 
{
    
    /**
     * add symbol to table, if it yet not here.
     */
    ADD  (0x00),
    
    /**
     * check that symbol is here and throw unknown symbol exception.
     */
    CHECK (0x01),
    
    /**
     * not add symbol to table, but periodically check: are we have
     *such symbol in our global index table.
     */
    LAZY (0x02), 
 
    /**
     * symbol is one of preefined constants or digit or so-on.
     */
    CONSTANT (0x04);
    
    
    SymbolAdoptionPolicy(int code)
    { intCode_=code; }
    
    public int getIntCode() 
    { return intCode_; }
    
    private int intCode_;
}
