/*
 * TXMLTestSuite.java
 *
 * Created on: 9, 08, 2005, 2:36
 *
 * Copyright (c) 2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termwaretests.txml;

import junit.framework.TestSuite;

/**
 *
 * @author Ruslan Shevchenko
 */
public class TXMLTestSuite extends TestSuite
{
    
    /** Creates a new instance of TXMLTestSuite */
    public TXMLTestSuite() {
        this.addTestSuite(TXMLTestCase.class);
    }
    
}
