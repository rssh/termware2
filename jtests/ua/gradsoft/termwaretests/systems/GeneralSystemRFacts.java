/*
 * GeneralSystemRFacts.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termwaretests.systems;

import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termwaretests.systems.helpers.Customer;

/**
 *Facts with few simple java methods.
 * @author Ruslan Shevchenko
 */
public class GeneralSystemRFacts extends DefaultFacts
{
    
    GeneralSystemRFacts() throws TermWareException
    {
        super();
    }
    
    public int getIntValue()
    { return 1; }
    
    public Customer createCustomer(String name)
    { return new Customer(name); }
    
    public Customer getQQQCustomer()
    { return qqqCustomer; }
    
    
    private Customer qqqCustomer = new Customer("qqq");         
            
}
