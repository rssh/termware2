package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.*;
                        

public class SubtermNotFoundException extends TermWareException
{
    
 public SubtermNotFoundException(Term t, Object where)
 { super("Subterm not found "); 
   t_=t;
   where_=where;
 }

 public Term getSubterm()
  { return t_; }

 public Object getTerm()
  { return where_; }

 private Term t_;
 private Object where_; 

}
                                                    