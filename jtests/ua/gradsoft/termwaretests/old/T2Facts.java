/*
 * T2Facts.java
 *
 */

package ua.gradsoft.termwaretests.old;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;

/**
 *
 * @author  Ruslan Shevchenko
 */
public class T2Facts extends DefaultFacts 
{
    
    /** Creates a new instance of T2Facts */
    public T2Facts() throws TermWareException
    {
        super("T2");
        flag_=false;
    }
    
    public boolean getFlag()
    {
        return flag_;
    }
    
    public void setFlag(boolean flag)
    {
        //System.out.println("setFlag called");
        flag_=flag;
    }
    
    /** print message on screen - can be called from action */
    public void print(String message)
    {
      System.out.println("message:"+message);  
    }
    
    private boolean flag_=false;
    
}
