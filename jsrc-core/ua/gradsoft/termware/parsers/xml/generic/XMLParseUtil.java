/*
 * XMLParseUtil.java
 *
 * Created on: 17, 09, 2005, 18:48
 *
 *$Id: XMLParseUtil.java,v 1.1 2007-02-12 17:08:47 rssh Exp $
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.parsers.xml.generic;

import ua.gradsoft.termware.exceptions.AssertException;

/**
 * Class of usefull utilities, need during XML parsing.
 * @author Ruslan Shevchenko
 */
public class XMLParseUtil {
    
    /**
     * transfrom string to boolean:
     *<ul>
     * <li>"on","1","true","yes" -> true </li>
     * <li>"off","0","false","no" -> false <li>
     * <li> any other -> throws AssertException </li>
     *</ul>
     */
    public static boolean getBoolean(String s) throws AssertException
    {
      if (s==null) return false;
      if (s.length()==0) return false;
      if (s.equals("on")||s.equals("1")||s.equals("true")||s.equals("yes")) {
          return true;
      }else if (s.equals("off")||s.equals("0")||s.equals("false")||s.equals("no")) {
          return false;
      }else{
          throw new AssertException("Invalid boolean value:"+s);
      }
    }
    
}
