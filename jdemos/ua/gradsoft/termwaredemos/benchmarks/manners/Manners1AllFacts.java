/*
 * Manners1Facts.java
 *
 */


package ua.gradsoft.termwaredemos.benchmarks.manners;

import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author Ruslan Shevchenko
 */
public class Manners1AllFacts extends DefaultFacts
{
    
    /** Creates a new instance of Manners1Facts */
    public Manners1AllFacts() throws TermWareException
    {
      numberOfSolutions_=0;  
    }
    
    public int getNumberOfSolutions()
    { return numberOfSolutions_; }
    
    
    public void found()
    { ++numberOfSolutions_; }
    
    int numberOfSolutions_;
}
