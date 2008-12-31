package ua.gradsoft.termware;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2007
 *(C) Grad-Soft Ltd, Kiev, Ukraine.
 *http://www.gradsoft.ua
 * $Id: IEnv.java,v 1.3 2007-08-04 08:54:42 rssh Exp $
 */



import java.io.PrintWriter;
import java.io.Reader;



/**
 * Interface which incapsulate environment of  application input/output/log. 
 *  (passed to TermWare engine during creation of Term System).
 **/                           
public interface IEnv
{     

 /**
  * get standard output stream.
  **/                              
 public PrintWriter getOutput();

 /**
  * get standard input stream.
  **/
 public Reader   getInput();
     
 /**
  * get standard log stream.
  **/                            
 public PrintWriter   getLog();

 /**
  * show message of exception ex
  **/
 public void          show(TermWareException ex);

 /**
  * called before shutown of term system
  **/
 public void          close();

}
