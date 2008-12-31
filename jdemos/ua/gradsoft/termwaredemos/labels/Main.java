/*
 * Main.java
 *
 * Created on July 12, 2007, 4:36 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.termwaredemos.labels;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *This is toy example for task, described in 
 * <a href="http://www.developers.org.ua/archives/adept/2007/04/24/fp-lazy-evaluation/"> next article </a> *
 * @author rssh
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
        
    }

    public static void main(String[] args)
    {      
      try {  
        TermWare.getInstance().init();
        if (args.length==2) {
            transformFile(args[0],args[1]);
        }else if (args.length==1) {
            if (args[0].equals("--example")) {
                runExample();
            }else{
                help();
            }
        }else{
            help();
        }
      }catch(TermWareException ex){
          System.err.println("exception during processing:"+ex.getMessage());
          ex.printStackTrace();
      }catch(IOException ex){
          System.err.println("exception during input/output:"+ex.getMessage());
          ex.printStackTrace();
      }  
    }
    
    static void help()
    {
            System.err.println("Usage:");
            System.err.println("  label inputFname  outputFname");
            System.err.println("or");
            System.err.println("  label --example");        
    }
    
    static void transformFile(String inFname, String outFname) throws IOException, TermWareException
    {
      BufferedReader reader = new BufferedReader(new FileReader(inFname));
      StringBuilder text=new StringBuilder();
      String s;
      while((s=reader.readLine())!=null) text.append(s); 
      String output = transformText(text.toString());
      PrintWriter writer = new PrintWriter(outFname);
      writer.print(output);
      writer.close();
    }
    
    static String transformText(String text) throws TermWareException
    {
      TermSystem ts = TermWare.getInstance().getOrCreateDomain("examples").resolveSystem("LabelText");
      //ts.setDebugMode(true);
      //ts.setDebugEntity("All");
      Term inputTerm = TermWare.getInstance().getTermFactory().createTerm("INPUT",text);
      Term outputTerm = ts.reduce(inputTerm);      
      if (!outputTerm.isString()) {
          System.err.println("some entries are not defined.");
          outputTerm.println(System.err);
          writeUndefined(outputTerm);
          throw new AssertException("Undefined entries");
      }
      return outputTerm.getString();
    }
    
    static void writeUndefined(Term t)
    {
        if (t.isComplexTerm()) {
            if (t.getName().equals("MakeRef")) {
                Term name = t.getSubtermAt(0);
                System.err.println("name "+name+" is undefined");
            }else{
                for(int i=0; i<t.getArity(); ++i) {
                    writeUndefined(t.getSubtermAt(i));
                }
            }
        }
    }

    
    static void runExample() throws TermWareException
    {
       
       String line1 = "First: @label{first} after fist, here is ref to second @ref{second}\n";
       String line2 = "nothing interesting in second line\n";
       String line3 = "Third line - let's define second @label{second} and ref to first @ref{first}\n";
       String line4 = "At last, let's define @ref{third} yet one refererence @label{third}";
       
       String text = line1+line2+line3+line4;
       
       String retval = transformText(text);
       
       System.out.println(retval);
    }
    
}
