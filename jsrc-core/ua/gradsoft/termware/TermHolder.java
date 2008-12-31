package ua.gradsoft.termware;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2004
 * (C) Grad-Soft Ltd. 2003-2004
 * http://www.gradsoft.ua
 * $Id: TermHolder.java,v 1.1.1.1 2004-12-11 19:41:28 rssh Exp $
 */

                           

/**
 * Holder for Term
 **/
public final class TermHolder
{

    public TermHolder(Term value)
  { value_ = value ; }


 public Term getValue() 
  { return value_; }

 public Term get()
  { return value_; }

 public void setValue(Term value) 
  { value_ = value; }

 public void set(Term value)
  { value_ = value; }

 private Term value_;

}                                                                  
