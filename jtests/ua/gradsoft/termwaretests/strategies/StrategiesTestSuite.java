/*
 * StrategiesTestSuite.java
 *
 * Copyright (c) 2004-2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termwaretests.strategies;

import junit.framework.TestSuite;

/**
 *
 * @author Ruslan Shevchenko
 */
public class StrategiesTestSuite extends TestSuite
{
    
    /** Creates a new instance of StrategiesTestSuite */
    public StrategiesTestSuite() {
        this.addTestSuite(BTStrategyTest.class);
    }
    
}

