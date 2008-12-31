/*
 * UtilTestSuite.java
 *
 */

package ua.gradsoft.termwaretests.util;

import junit.framework.*;

/**
 *Test Suite for utils.
 */
public class UtilTestSuite extends TestSuite
{
    
    /** Creates a new instance of UtilTestSuite */
    public UtilTestSuite() {        
             addTestSuite(SetOfTermsTest.class);
    }
    
}
