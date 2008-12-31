/*
 * Guest.java
 *
 * Created on: 14, 08, 2005, 13:53
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termwaredemos.benchmarks.manners;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareSymbols;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *
 * @author Ruslan Shevchenko
 */
public class Guest {
    
    /** Creates a new instance of Guest */
     public Guest(Term t) throws TermWareException
    {
      if (t.isComplexTerm()) {
          if (!t.getName().equals("guest")) {
              throw new AssertException("guest must be created from guest term");
          }
          if (t.getArity()!=3) {
              throw new AssertException("guest term must have arity 3");
          }
          name_=t.getSubtermAt(0).getAsString(TermWare.getInstance());
          sex_=t.getSubtermAt(1).getAsString(TermWare.getInstance());
          
          hobbies_=t.getSubtermAt(2);
          if (!hobbies_.getNameIndex().equals(TermWareSymbols.SET_INDEX)) {
              throw new AssertException("hobbies must be a set");
          }
      }   
    }
    
    public String getName()
    { return name_; }
    
    public String    getSex()
    { return sex_; }
    
    public Term getHobbies()
    { return hobbies_; }
    
    
    public String toString()
    { return name_; }
    
    private String name_;
    private String sex_;
    private Term   hobbies_;
    
}
