package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;


/**
 * Conversion is throwed, on type mismatch
 * when we call TypeConversion method on
 * term of inconvertable type. 
 **/
public class ConversionException extends TermWareException
{

 public ConversionException(Object obj,Class from,Class to,String msg)
 {
  obj_=obj;
  classFromConvert_=from;
  classToConvert_=to;
  msg_=msg;
 }
 
 
 public  String getMessage()
 {
   return "Can't convert "+obj_.toString()+" from "+classFromConvert_.getName() + " to "+classToConvert_.getName()+":"+msg_;
 }
 
 private Object obj_;
 private Class classFromConvert_;
 private Class classToConvert_;
 private String  msg_;
 
 
 
}
