/*
 * LabelsTestCase.java
 *
 */

package ua.gradsoft.termwaretests.demos.labels;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import junit.framework.TestCase;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *
 * @author rssh
 */
public class LabelsTestCase extends TestCase {

    
    protected void setUp() throws Exception
    {
       TermWare.getInstance().init(); 
       TermSystem ts = TermWare.getInstance().getOrCreateDomain("examples").resolveSystem("LabelText");
       // set new facts, to reset references.
       ts.setFacts(new ua.gradsoft.termwaretests.demos.labels.Facts());
       
    }
    
    public void testLabelsExample1() throws TermWareException
    {
       
       String line1 = "First: @label{first} after fist, here is ref to second @ref{second}\n";
       String line2 = "nothing interesting in second line\n";
       String line3 = "let's try @ref{third} third label\n";
       String line4 = "Third line - let's define second @label{second} and ref to first @ref{first}\n";
       String line5 = "At last, let's define @ref{third} yet one refererence @label{third}";
       
       String text = line1+line2+line3+line4+line5;
       
       String retval = transformText(text);
       
       //System.err.println("retval is "+retval);
       
       assertTrue(retval.contains("[1]"));
                     
    }

    
    public static String transformText(String text) throws TermWareException
    {
      TermSystem ts = TermWare.getInstance().getOrCreateDomain("examples").resolveSystem("LabelText");
      //ts.setLoggingMode(true);
      //ts.setLoggedEntity("All");
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

    
    
    
    
}
