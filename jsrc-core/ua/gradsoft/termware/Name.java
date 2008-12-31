/*
 * Name.java
 *
 * Copyright (c) 2006-2008 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware;

import java.io.Serializable;

/**
 *Holds names and indexes, which are synchronized with state of SymbolTable.
 * @author Ruslan Shevchenko
 */
public final class Name implements Serializable
{
    
    public Name(String value, SymbolAdoptionPolicy symbolAdoptionPolicy)
    {
      stringValue_=value;
      adopt(symbolAdoptionPolicy);
    }
    
    public String getStringValue()
    { return stringValue_; }
    
    public Object getIndex()
    { 
        if (index_!=null) {
            return index_;
        }else{
            if (SymbolTable.getSymbolTable().getLastModificationTime() > lastWatchTime_) {
                adopt(SymbolAdoptionPolicy.LAZY);
                if (index_!=null) return index_;
            }
            return stringValue_;
        }
    }
    
    private void adopt(SymbolAdoptionPolicy symbolAdoptionPolicy)  {
        if (index_==null) {
            boolean addNames=(symbolAdoptionPolicy==SymbolAdoptionPolicy.ADD);
            SymbolTable.Entry e = SymbolTable.getSymbolTable().adoptName(stringValue_, addNames);
            lastWatchTime_=System.currentTimeMillis();
            if (e!=null) {
                stringValue_=e.getName();
                index_=e.getIndex();
            }
        }
    }
    

    public  long getLastWatchTime()
    { return lastWatchTime_; }
   
    private  String  stringValue_=null;
    private  transient Object index_=null;
    private  long   lastWatchTime_=0;
    
    private static final long serialVersionUID = 20080111;
        
}
