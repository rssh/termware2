/*
 * PrimaryTypes.java
 *
 */

package ua.gradsoft.termware;

/**
 *Enumerations and some utils for primary type system of terms.
 * @author  Ruslan Shevchenko
 */
public class PrimaryTypes {
    
    //public static final int UNKNOWN=-1;
    
    public static final int UNIFICATION_MASK=0xFF00;
    
    public static final int NIL=        0x0100;
    
    public static final int NUMBER     =0x0200;
    public static final int BIG_INTEGER=0x0201;
    public static final int BYTE=       0x0202;
    public static final int INT=        0x0203;
    public static final int LONG=       0x0204;
    public static final int SHORT=      0x0205;
    
    public static final int PARTIALLY_MASK=0xFFF0;    
    
    public static final int BIG_DECIMAL=0x0210;    
    public static final int DOUBLE=     0x0211;
    public static final int FLOAT=      0x0212;
    
    
    public static final int ATOM=       0x0300;
    public static final int X =         0x0400;
    public static final int STRING=     0x0500;
    public static final int CHAR=       0x0600;
    public static final int BOOLEAN=    0x0700;
    
    public static final int JAVA_OBJECT=0x0800;
    
    public static final int COMPLEX_TERM=0x1000;
    
    
    /**
     *is  <tp> is primitive type ?
     *@return true, if tp represent primitive. (i. e. simple value or Atom or NIL)
     */
    public static final boolean isPrimitive(int tp)
    {
     return (tp!=COMPLEX_TERM) && (tp!=X) ;
    }
    
    /**
     *is  <tp> is number type ?
     *@return true, if tp represent number type
     */
    public static final boolean isNumber(int tp)
    {
     return (tp & UNIFICATION_MASK)==NUMBER;
    }
    
}
