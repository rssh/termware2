/*
 * TermTest.java
 * JUnit based test
 *
 */

package ua.gradsoft.termwaretests;

import junit.framework.*;
import java.io.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;

/**
 *Test basic term operations
 * @author Ruslan Shevchenko
 */
public class TermTest extends TestCase {
    
    Term intTerm0;
    Term charTerm0;
    Term shortTerm0;
    Term doubleTerm0;
    Term floatTerm0;
    Term atomTermQQQ;
    Term atomTerma1;
    Term atomTerma2;
    Term nilTerm;
    Term falseTerm;
    Term trueTerm;
    Term complexTermAB;
    Term bigDecimalTerm1;
    Term bigDecimalTerm2;
    Term bigIntegerTerm1;
    Term bigIntegerTerm0;
    
    public TermTest(String testName) {
        super(testName);
    }

    protected void setUp() throws java.lang.Exception {        
        TermWare.getInstance().init();
        TermFactory termFactory=TermWare.getInstance().getTermFactory();
        intTerm0=termFactory.createInt(0);
        charTerm0=termFactory.createChar('\0');
        shortTerm0=termFactory.createShort((short)0);
        atomTermQQQ=termFactory.createAtom("QQQ");
        atomTerma1=termFactory.createAtom("a1");
        atomTerma2=termFactory.createAtom("a2");
        nilTerm=termFactory.createNIL();
        falseTerm=termFactory.createBoolean(false);
        trueTerm=termFactory.createBoolean(true);
        bigDecimalTerm1=termFactory.createBigDecimal(BigDecimal.valueOf(1));
        bigDecimalTerm2=termFactory.createBigDecimal(BigDecimal.valueOf(2));
        bigIntegerTerm1=termFactory.createBigInteger(BigInteger.ONE);
        bigIntegerTerm0=termFactory.createBigInteger(BigInteger.ZERO);
        
        complexTermAB=termFactory.createTerm("AB",atomTermQQQ,intTerm0);
    }

    protected void tearDown() throws java.lang.Exception {
    }

    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(TermTest.class);
        
        return suite;
    }

    /**
     * Test of getPrimaryType0 method, of class ua.gradsoft.termware.Term.
     */
    public void testGetPrimaryType0() {
        
        
        assertTrue(intTerm0.getPrimaryType0()==PrimaryTypes.INT);
        assertTrue(shortTerm0.getPrimaryType0()==PrimaryTypes.SHORT);
        assertTrue(atomTermQQQ.getPrimaryType0()==PrimaryTypes.ATOM);
        assertTrue(nilTerm.getPrimaryType0()==PrimaryTypes.NIL);
        assertTrue(trueTerm.getPrimaryType0()==PrimaryTypes.BOOLEAN);
        assertTrue(bigDecimalTerm1.getPrimaryType0()==PrimaryTypes.BIG_DECIMAL);
    }

    /**
     * Test of getPrimaryType1 method, of class ua.gradsoft.termware.Term.
     */
    public void testGetPrimaryType1() {
        

        assertTrue(shortTerm0.getPrimaryType1()==PrimaryTypes.NUMBER);

    }

    /**
     * Test of isNil method, of class ua.gradsoft.termware.Term.
     */
    public void testIsNil() {
        

        assertTrue(nilTerm.isNil());
        assertFalse(atomTermQQQ.isNil());
    }

    /**
     * Test of isAtom method, of class ua.gradsoft.termware.Term.
     */
    public void testIsAtom() {
        
        
        assertTrue(atomTermQQQ.isAtom());
        assertFalse(nilTerm.isAtom());
        assertFalse(shortTerm0.isAtom());
        
    }

    /**
     * Test of isBoolean method, of class ua.gradsoft.termware.Term.
     */
    public void testIsBoolean() {
        

        assertTrue(trueTerm.isBoolean());
        assertFalse(nilTerm.isBoolean());
    }

    /**
     * Test of getBoolean method, of class ua.gradsoft.termware.Term.
     */
    public void testGetBoolean() {
        //System.out.println("testGetBoolean");
        
        assertTrue(trueTerm.getBoolean()==true);
        assertTrue(falseTerm.getBoolean()==false);
        
    }

    /**
     * Test of isChar method, of class ua.gradsoft.termware.Term.
     */
    public void testIsChar() {
        //System.out.println("testIsChar");
        
        assertFalse(atomTermQQQ.isChar());
        assertTrue(charTerm0.isChar());
        
        // TODO add your test code below by replacing the default call to fail.
        //fail("The test case is empty.");
    }

    /**
     * Test of getChar method, of class ua.gradsoft.termware.Term.
     */
    public void testGetChar() {
        
        assertTrue(charTerm0.getChar()=='\0');
        //System.out.println("testGetChar");
        
    }

    /**
     * Test of getAsChar method, of class ua.gradsoft.termware.Term.
     */
    public void testGetAsChar() {
        
        
        // TODO add your test code below by replacing the default call to fail.
        //fail("The test case is empty.");
    }

    /**
     * Test of isNumber method, of class ua.gradsoft.termware.Term.
     */
    public void testIsNumber() {
        
        
        assertTrue(intTerm0.isNumber());
        assertTrue(shortTerm0.isNumber());
        
        assertFalse(atomTermQQQ.isNumber());
        assertFalse(falseTerm.isNumber());
        
        assertTrue(bigDecimalTerm1.isNumber());
        
    }

    /**
     * Test of getNumber method, of class ua.gradsoft.termware.Term.
     */
    public void testGetNumber() {
        
        Number n=intTerm0.getNumber();
        // TODO add your test code below by replacing the default call to fail.
        //fail("The test case is empty.");
    }

    /**
     * Test of getAsNumber method, of class ua.gradsoft.termware.Term.
     */
    public void testGetAsNumberBigDecimal() throws ConversionException {
        
        Number n=bigDecimalTerm1.getAsNumber(TermWare.getInstance());
        
        // TODO add your test code below by replacing the default call to fail.
        //fail("The test case is empty.");
    }

  
    /**
     * Test of getBigDecimal method, of class ua.gradsoft.termware.Term.
     */
    public void testGetBigDecimalShort() {
  
        try {
            BigDecimal bd=shortTerm0.getBigDecimal();
            fail("shortTerm0.getBigDecimal must throw exception");
        }catch(UnsupportedOperationException ex){
            // nothing: success
        }
        
    }

    /**
     * Test of getAsBigDecimal method, of class ua.gradsoft.termware.Term.
     */
    public void testGetAsBigDecimal() throws TermWareException
    {
        
        BigDecimal dc=intTerm0.getAsBigDecimal(TermWare.getInstance());
        
        assertEquals(BigDecimal.valueOf(0),dc);
        
    }

  
    /**
     * Test of getAsBigInteger method, of class ua.gradsoft.termware.Term.
     */
    public void testGetAsBigInteger()  throws TermWareException
    {
        
        BigInteger bi=shortTerm0.getAsBigInteger(TermWare.getInstance());
        
        assertEquals(BigInteger.ZERO,bi);
        
    }

    /**
     * Test of isByte method, of class ua.gradsoft.termware.Term.
     */
    public void testIsByte() {
        
        assertFalse(shortTerm0.isByte());
        
    }

    /**
     * Test of isInt method, of class ua.gradsoft.termware.Term.
     */
    public void testIsInt() {
        
        assertTrue(intTerm0.isInt());
        
    }


    /**
     * Test of getShort method, of class ua.gradsoft.termware.Term.
     */
    public void testGetShort() {
        
        
        assertEquals((short)0,shortTerm0.getShort());
        
    }

    /**
     * Test of getAsShort method, of class ua.gradsoft.termware.Term.
     */
    public void testGetAsShort() throws TermWareException {

        assertEquals((short)0,shortTerm0.getAsShort(TermWare.getInstance()));
        
    }

    /**
     * Test of isString method, of class ua.gradsoft.termware.Term.
     */
    public void testIsString() {

        assertFalse(atomTermQQQ.isString());
        
    }


    /**
     * Test of isX method, of class ua.gradsoft.termware.Term.
     */
    public void testIsX() {

        assertFalse(shortTerm0.isX());
        
    }


    /**
     * Test of isComplexTerm method, of class ua.gradsoft.termware.Term.
     */
    public void testIsComplexTerm() {

        assertFalse(atomTermQQQ.isComplexTerm());
        assertTrue(complexTermAB.isComplexTerm());
        
    }

    /**
     * Test of getName method, of class ua.gradsoft.termware.Term.
     */
    public void testGetName() {
        
        assertEquals("QQQ",atomTermQQQ.getName());
        assertEquals("AB",complexTermAB.getName());
    }

    /**
     * Test of isJavaObject method, of class ua.gradsoft.termware.Term.
     */
    public void testIsJavaObject() {

        assertFalse(trueTerm.isJavaObject());
    
    }


    /**
     * Test of termCompare method, of class ua.gradsoft.termware.Term.
     */
    public void testTermCompare() throws TermWareException
    {

        assertTrue(intTerm0.termCompare(shortTerm0)==0);
        assertTrue(shortTerm0.termCompare(intTerm0)==0);
        
        assertFalse(charTerm0.termCompare(intTerm0)==0);
       
    }

    /**
     * Test of termCompare method, of class ua.gradsoft.termware.Term.
     */
    public void testTermCompareBigDecimalBigDecimal1() throws TermWareException
    {

        assertTrue(bigDecimalTerm1.termCompare(bigDecimalTerm2)<0);
              
    }

    /**
     * Test of termCompare method, of class ua.gradsoft.termware.Term.
     */
    public void testTermCompareBigDecimalBigDecimal2() throws TermWareException
    {

        assertTrue(bigDecimalTerm2.termCompare(bigDecimalTerm1)>0);
              
    }

    public void testCompareAtoms() throws TermWareException
    {
       
       //System.out.println("a1.getNameIndex is "+atomTerma1.getNameIndex()) ;
       //System.out.println("a2.getNameIndex is "+atomTerma2.getNameIndex()) ;
       assertTrue(atomTerma1.termCompare(atomTerma2) != 0);   
       assertTrue(atomTerma1.termCompare(atomTerma1) == 0);   
       int x1=atomTerma1.termCompare(atomTerma2);
       int x2=atomTerma1.termCompare(atomTerma1);
       assertTrue(x1!=x2);
    }
    
    /**
     * Test of termCompare method, of class ua.gradsoft.termware.Term.
     */
    public void testTermCompareBigIntegerBigDecimal1() throws TermWareException
    {

        assertTrue(bigIntegerTerm1.termCompare(bigDecimalTerm1)==0);
              
    }
    
    /**
     * Test of emptyFv method, of class ua.gradsoft.termware.Term.
     */
    public void testEmptyFv() {
   
        assertTrue(complexTermAB.emptyFv());
    }

   
    
    
    
}
