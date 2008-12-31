/*
 * Facts.java
 *
 */

package ua.gradsoft.termwaredemos.labels;

import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Facts database for label example.
 * @author rssh
 */
public class Facts extends DefaultFacts {
    
    public Facts() throws TermWareException
    {super(); }

    public int addName(String name)
    {
      Entry e = entries.get(name); 
      if (e!=null) {
          System.err.println("warning - double definition of label "+name);
          return e.index;
      }else{
          e = new Entry(name,++nextIndex);
          entries.put(name,e);
          return nextIndex;
      }
    }
    
    public boolean isDefined(String name)
    { return entries.get(name)!=null; }
    
    public int getNumber(String name) throws AssertException
    {
      Entry e = entries.get(name);
      if (e==null) {
          throw new AssertException("Entry "+name+" is not defined");
      }
      return e.index;
    }
    
    private static class Entry
    {
        Entry(String name, int index)
        { this.name=name; this.index=index; }
        String name;
        int    index;
    }
    
    private Map<String,Entry> entries = new TreeMap<String,Entry>();
    private int nextIndex=0;
    
}
