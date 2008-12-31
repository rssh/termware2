package ua.gradsoft.termware.envs;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002 - 2007
 * $Id: SystemLogEnv.java,v 1.3 2007-08-04 08:54:42 rssh Exp $
 */

import java.io.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;


/**
 * the same as SystemEnv, but in addition set input,
 * output and log streams to appropriative files.
 **/                           
public class SystemLogEnv implements IEnv
{     

 public SystemLogEnv() 
 {
   input_=new InputStreamReader(System.in);  
   output_=new PrintWriter(System.out,true);   
   log_=new PrintWriter(System.err,true);
 }

 /**
  * t must be nil, or must look like
  *<pre>
  * env(t_1,t_2,t_3)
  * where 
  *   t_1 - string or atom 'system', which means standart input, or name of
  *         input file.
  *   t_2 - string or atom 'system', which means standart output, or name of
  *         output file.
  *   t_3 - string or atom 'system', which means standart error output, or 
  *         name of log file.
  *</pre>
  **/
 public SystemLogEnv(Term t) throws IOException, TermWareException
 {
  if (!t.isNil()) { 
    if (t.getArity()!=3) {
      throw new AssertException(
        "arity of env in SystemLogEnv constructor must be 3");
    }else{
      Term t1=t.getSubtermAt(0); 
      Term t2=t.getSubtermAt(1); 
      Term t3=t.getSubtermAt(2); 
      input_=null;
      output_=null;
      log_=null;
      if (t1.isAtom() || t1.isString()) {
        if (!t1.getName().equals("system")) {
          input_=new BufferedReader(new FileReader(t1.getName()));  
        }else{
          input_=new InputStreamReader(System.in);  
        }
      }else{
       throw new AssertException(
         "SystemLogEnv: first argument must be string or atom");
      }
      if (t2.isAtom() || t2.isString()) {
        if (!t2.getName().equals("system")) {
            output_=new PrintWriter(new  BufferedWriter(new FileWriter(t2.getName())));  
        }else{
            output_=new PrintWriter(System.out,true);
        }
      }else{      
        throw new AssertException(
            "SystemLogEnv: 2-nd argument must be string or atom");
      }
      if (t3.isAtom() || t3.isString()) {
         if (!t3.getName().equals("system")) {
             log_=new PrintWriter(new BufferedWriter(new FileWriter(t3.getName())));  
         }
      }else{
         throw new AssertException(
             "SystemLogEnv: 3-rd argument must be string or atom");
      }
    }    
  }
 }
 
 /**
  * get standard output
  **/
 public PrintWriter   getOutput()
 {
  return output_;
 }
 
 /**
  *set Output Stream.
  **/
 public void  setOutput(PrintWriter output) 
 {
   if (output_==output) return;  
   if (output_!=null) {
      output_.close();
   }
   output_=output;  
 }

 /**
  *get input
  */
 public Reader   getInput()
 {
  return input_;
 }

 /**
  * set input
  */
 public void  setInput(Reader input) throws IOException
 {
   if (input_==input) return;  
   if (input_!=null) {
       input_.close();
   }
   input_=input;
 }
 
 /**
  *get log stream
  */
 public PrintWriter   getLog()
 {
  return log_;
 }

 /**
  * set log stream
  */
 public void          setLog(PrintWriter log)
 {
   if (log_==log) return;
   if (log_!=null) {
       log_.close();
   }
   log_=log;
 }
       
 /**
  * show exception.
  **/
 public void          show(TermWareException ex)
 {
  getLog().println(ex.toString());
  getLog().flush();
 }

 public void close() 
 {
  try {
    if (input_!=null) input_.close();
    if (output_!=null) output_.close();
    if (log_!=null) log_.close();
  }catch(IOException ex){
    String message="SystemLogEnv: IOException during closing\n"+
                   ex.toString();
    if (log_!=null) log_.println(message);
    else System.err.println(message);
  }
 }

 private Reader input_;
 private PrintWriter output_;
 private PrintWriter log_;

}
