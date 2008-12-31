/*
 * Invoice.java
 *
 * Created 9, 05, 2005, 7:09
 */

package ua.gradsoft.termwaretests.systems.helpers;

/**
 *Model class for invoice.
 *(The definition of  invoice class is copied from jsr94 TCK)
 * @author Ruslan Shevchenko
 */
public class Invoice {
    

    	// The description of the Invoice.
	private String description;
	// The amount to pay for the Invoice.
	private int amount;
	// The status of the Invoice.
	private String status;
	
	/** Create an instance of the Invoice class.
	 *
	 * @param description The description of the invoice.
	 */
	public Invoice(String description)
	{
		this.description = description;
		this.status = "unpaid";
	}

	/** Get the description of this invoice. */
	public String getDescription()
	{
		return this.description;
	}

	/** Set the description for this invoice. */
	public void setDescription(String description)
	{
		this.description = description;
	}
	/** Get the payment amount for this invoice. */
	public int getAmount()
	{
		return this.amount;
	}

	/** Set the payment amount for this invoice. */
	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	/** Get the status for this invoice. */
	public String getStatus()
	{
		return this.status;
	}

	/** Set the status for this invoice. */
	public void setStatus(String status)
	{
		this.status = status;
	}
            
    
}
