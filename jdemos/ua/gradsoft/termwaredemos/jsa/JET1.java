/*
 * JET1.java
 *
 */

package ua.kiev.gradsoft.TermWareDemos.jsa;

import java.io.*;

/**
 *This is demo class, which have one problem - empty catch block.
 * TermWare will automatically find this problem for us.
 * @author  Ruslan Shevchenko
 */
public class JET1 {
    

    public static void main(String[] args)
    {
        String fname="qqq";
        File f=new File(fname);
        try {
          InputStream input = new FileInputStream(f);
          input.close();
        }catch(IOException ex){
            /* ignore - this must be catched */
        }
        
    }
    
    
}
