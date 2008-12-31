package ua.gradsoft.termware.envs;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import ua.gradsoft.termware.IEnv;
import ua.gradsoft.termware.TermWareException;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2007
 * (C) Grad-Soft Ltd, Kiev, Ukraine.
 * http://www.gradsoft.ua
 * $Id: SystemEnv.java,v 1.4 2007-08-04 08:54:42 rssh Exp $
 */


/**
 * environment, which write output to <code> System.out </code>,
 * read input from <code> System.in </code> and write log to <code> System.err </code>
 */
public class SystemEnv implements IEnv {
    
    public SystemEnv() {
        output_=new PrintWriter(System.out,true);
        input_=new InputStreamReader(System.in);
        log_=new PrintWriter(System.err,true);
    }
    
    /**
     * return System.out
     */  
    public PrintWriter   getOutput() {
        return output_;
    }
    
    
    /**
     * return System.in
     */
    public Reader   getInput() {
        return input_;
    }
    
    /**
     * return System.err
     */   
    public PrintWriter   getLog() {
        return log_;
    }
    
    /**
     * print exceptions on standard output.
     */  
    public void          show(TermWareException ex) {
        System.err.println(ex.toString());
    }
    
    /**
     * flush output and error stream
     */  
    public void close() {
        log_.flush();
        output_.flush();
    }
    
    
    private PrintWriter output_;
    private Reader      input_;
    private PrintWriter log_;
    
}
