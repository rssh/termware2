
package ua.gradsoft.termware.parsers.terms.util;

import java.util.HashMap;
import java.util.Map;

public class StringIndex
{

 public  int  getIndex(String s)
 {
  if (map_==null) map_ = new HashMap<String,Integer>();
  Integer indexObject = map_.get(s);
  if (indexObject==null) {
    int retval=map_.size();
    map_.put(s,new Integer(retval));
    return retval;
  }else{
    return indexObject.intValue();
  }
 }

 public Map<String,Integer> getMap()
 { 
     if (map_==null) map_ = new HashMap<String,Integer>();
     return map_; 
 }

 private HashMap<String,Integer> map_=null;

};