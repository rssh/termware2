/*
 * Customer.java
 *
 * Created 9/05/2005, 7:12
 */

package ua.gradsoft.termwaretests.systems.helpers;

/**
 *The definition of customer, copied from JSR094 TCK
 * @author Ruslan Shevchenko
 */
public class Customer {
    
	// The name of the customer.
	private String name;
	// The credit limit of the customer.
	private int creditLimit;
	
	/** Create an instance of the Customer class.
	 *
	 * @param name The name of the customer.
	 */
	public Customer(String name)
	{
		this.name = name;
                this.creditLimit=0;
	}

	/** Get the name of this customer. */
	public String getName()
	{
		return this.name;
	}

	/** Set the name of this customer. */
	public void setName(String name)
	{
		this.name = name;
	}
	/** Get the credit limit of this customer. */
	public int getCreditLimit()
	{
		return this.creditLimit;
	}

	/** Set the credit limit for this customer. */
	public void setCreditLimit(int creditLimit)
	{
		this.creditLimit = creditLimit;
	}
    
       
        
}
