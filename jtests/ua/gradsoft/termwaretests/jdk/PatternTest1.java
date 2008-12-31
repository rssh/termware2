/*
 * PatternTest1.java
 *
 * Created 15, 02, 2004, 13:29
 */

package ua.gradsoft.termwaretests.jdk;

/**
 *Shows error in Java pattern mathing in JDK-1.4.1
 * @author  Ruslan Shevchenko
 */
public class PatternTest1 {
    
    /**
     * illustrate bug in Sun JDK.
     *TODO:
     *  submit via http://java.sun.com/cgi-bin/bugreport.cgi
     *<pre>
     *  String s="x+\"y\"";
     *  String p="\\";
     *  String r="\\\\";
     *  String t=s.replaceAll(p,r);
     *  System.out.println(t);
     *</pre>
     */
    
    public static void main(String[] args)
    {
     String s="x+\"y\"";
     String p="\\";
     String r="\\\\";
     String t=s.replaceAll(p,r);
     System.out.println(t);
    }
    
}
