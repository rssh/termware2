/*
 * SymbolTable.java
 *
 * Created  24, 08, 2004, 1:08
 */

package ua.gradsoft.termware;

import java.util.*;
import ua.gradsoft.termware.exceptions.RuntimeAssertException;



/**
 * Symbol table for TermWare.
 * We have one symbol table per JVM instance.
 * @author  Ruslan Shevchenko
 */
public class SymbolTable 
{
    
    /** Creates a new instance of SymbolTable */
    private SymbolTable() {
        namesTable_=Collections.synchronizedMap(new HashMap<String,Entry>());
        reverseTable_=new ArrayList<String>();
        counter_=0;
        lastTouch_=System.currentTimeMillis();
    }
    
    /**
     * entry for name in symbol table.
     */
    public static final class Entry
    {
        Entry(Integer mark,String name)
        {
            mark_=mark;
            name_=name;
        }
        private Integer mark_;
        private String name_;
        
        public Integer getIndex()
        { return mark_; }
        
        public String  getName()
        { return name_; }
    }
    
    
    
    
    /**
     * find name in symbol table and return entry. If <code> isAdd </code> parameter is true,
     *then insert name in symbol table. 
     *@param name - name to ssearch or insert.
     *@param isAdd - if set to true, than we add name which was not in table.
     *@return found or inserted Entry or null, if  name was not found and <code> isAdd </code> set to false.
     */
    public Entry  adoptName(String name, boolean isAdd) 
    {
        // TODO: insert rd-only lock when will bree aviable.
        Object o=namesTable_.get(name);
        if (o==null) {
            if (isAdd) {
              synchronized(this) {
                Integer newMark=new Integer(counter_);
                Entry newEntry = new Entry(newMark,name);               
                namesTable_.put(name,newEntry);
                int newReverseIndex=newMark.intValue();
                if (reverseTable_.size() == newReverseIndex) { 
                  reverseTable_.add(name);
                }else{
                    throw new RuntimeAssertException("Internal error: unexpected ArrayList behaviour");
                }      
                ++counter_;
                lastTouch_=System.currentTimeMillis();
                return newEntry;
              }
            }else{
                return null;
            }
        }else{
            Entry e=(Entry)o;
            return e;
        }        
    }
    
//    public Integer adoptName(String name)
//    {
//        Object o=namesTable_.get(name);
//        if (o==null) {
//            synchronized(this) {
//                Integer newMark=new Integer(counter_);
//                namesTable_.put(name,new Entry(newMark,name));
//                reverseTable_.set(newMark.intValue(),name);
//                ++counter_;
//                return newMark;
//            }
//        }else{
//            return ((Entry)o).mark_;
//        }
 //   }
    
    /**
     *get index of existing name in table.
     *@return index of name, if one exists in table, otherwise null
     */
    public Integer getNameIndex(String name)
    {
       Object o = namesTable_.get(name);
       if (o==null) return null;
       return ((Entry)o).mark_;
    }
    
    
    /**
     *get name of existing index.
     *@param index - index of name.
     *@return name, stored in tabele
     *@exception IndexOutOfBoundsException if index is not in name.
     */
    public String getName(Integer index)
    {   
     return reverseTable_.get(index.intValue());   
    }
    
    /**
     * return time in milliseconds, when Symbol Table was last updated.
     */
    public long getLastModificationTime()
    {
        return lastTouch_;
    }
    
    /**
     * return instance of SymbolTable in current JVM
     */
    public  static SymbolTable getSymbolTable()
    {
     if (singleton_==null) {
         synchronized(SymbolTable.class) {
             if (singleton_==null) {
                 singleton_=new SymbolTable();
             }
         }
     }   
     return singleton_;
    }
    
    private static SymbolTable singleton_ = null;
    
    private int counter_;
    
    private Map<String,Entry> namesTable_;
    private ArrayList<String> reverseTable_;
    
    private transient long lastTouch_;
}
